package com.daiyc.lark.core.session;

import com.daiyc.lark.core.infrastructure.PipedInputStream;
import com.daiyc.lark.core.infrastructure.PipedOutputStream;
import io.termd.core.tty.TtyConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author daiyc
 */
public class TtyTelnetSession extends BaseSession {
    private InputStream inputStream;

    private OutputStream outputStream;

    public TtyTelnetSession(TtyConnection connection) {
        super(connection);

        init();
    }

    private void init() {
        outputStream = new InteractionOutputStream(this);

        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        PipedInputStream pipedInputStream = new PipedInputStream();
        pipedOutputStream.connect(pipedInputStream);
        inputStream = pipedInputStream;

        connection.setStdinHandler(data -> {
            try {
                pipedOutputStream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        connection.setCloseHandler((a) -> {
            try {
                pipedOutputStream.close();
                pipedInputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public InputStream inputStream() {
        return inputStream;
    }

    @Override
    public OutputStream outputStream() {
        return outputStream;
    }

    private static class InteractionOutputStream extends OutputStream {
        private volatile boolean closed = false;

        private Session session;

        public InteractionOutputStream(Session session) {
            this.session = session;
        }

        @Override
        public void write(int b) throws IOException {
            checkStreamOpening();
            session.send(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            checkStreamOpening();
            if (b == null) {
                throw new NullPointerException();
            } else if ((off < 0) || (off > b.length) || (len < 0) ||
                    ((off + len) > b.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return;
            }
            int[] copy = new int[len];
            for (int i = 0; i < len; i++) {
                copy[i] = b[off + i];
            }

            session.send(copy);
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }

        private void checkStreamOpening() throws IOException {
            if (closed) {
                throw new IOException("OutputStream was closed!");
            }
        }
    }
}
