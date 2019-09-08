package com.dyc.lark.core.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author daiyc
 */
public class PipedInputStream extends InputStream {
    private AtomicBoolean closed = new AtomicBoolean(false);

    private static final int DEFAULT_PIPE_SIZE = 1024;

    private final int[] buffer;

    private int length = 0;

    /**
     * 下一个写入位置
     */
    private int in = 0;

    /**
     * 下一个读取位置，当in == out时为空
     */
    private int out = 0;

    boolean connected = false;

    private final int capacity;

    private final ReentrantLock lock;

    private final Condition notEmpty;

    private final Condition notFull;

    public PipedInputStream(int size) {
        buffer = new int[size];
        capacity = size;
        lock = new ReentrantLock(true);
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
    }

    public PipedInputStream() {
        this(DEFAULT_PIPE_SIZE);
    }

    protected void receive(int b) throws InterruptedException, IOException {
        checkStreamOpening();

        lock.lock();
        try {
            if (isFull()) {
                notFull.await();
            }
            checkStreamOpening();
            append(b);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    private boolean append(int b) {
        if (isFull()) {
            return false;
        }
        buffer[in++] = b;
        length++;
        in %= capacity;
        return true;
    }

    private boolean isFull() {
        return capacity == length;
    }

    private boolean isEmpty() {
        return length == 0;
    }

    @Override
    public int read() throws IOException {
        checkStreamOpening();
        lock.lock();
        try {
            if (isEmpty()) {
                notEmpty.await();
            }
            checkStreamOpening();
            int b = retrieve();
            notFull.signal();
            return b;
        } catch (InterruptedException e) {
            throw new IOException("write fail");
        } finally {
            lock.unlock();
        }
    }

    private int retrieve() {
        int b = buffer[out++];
        length--;
        out %= capacity;
        return b;
    }

    private void checkStreamOpening() throws IOException {
        if (closed.get()) {
            throw new IOException("InputStream was closed!");
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (closed.compareAndSet(false, true)) {
            lock.lock();
            try {
                notEmpty.signalAll();
                notFull.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }
}
