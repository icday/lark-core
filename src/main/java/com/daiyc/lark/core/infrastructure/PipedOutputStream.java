package com.daiyc.lark.core.infrastructure;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author daiyc
 */
public class PipedOutputStream extends OutputStream {
    private PipedInputStream sink;

    public PipedOutputStream(PipedInputStream sink) {
        if (sink == null) {
            throw new NullPointerException();
        }
        this.sink = sink;
    }

    public PipedOutputStream() {
    }

    public void connect(PipedInputStream sink) {
        if (sink == null) {
            throw new NullPointerException();
        } else if (this.sink != null || sink.connected) {
            throw new IllegalStateException("Already connected");
        }
        this.sink = sink;
        sink.connected = true;
    }

    @Override
    public void write(int b) throws IOException {
        if (sink == null) {
            throw new IOException("Pipe not connected");
        }

        try {
            sink.receive(b);
        } catch (InterruptedException e) {
            throw new IOException("write fail");
        }
    }

    public void write(int[] b) throws IOException {
        if (b == null) {
            return;
        }
        for (int i : b) {
            write(i);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
    }

    @Override
    public void close() throws IOException {
        sink.close();
    }
}
