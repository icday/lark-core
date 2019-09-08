package com.dyc.lark.core.session;

import com.dyc.lark.core.interaction.Interaction;
import com.dyc.lark.core.interaction.RetCode;
import com.dyc.lark.core.util.Preconditions;
import io.termd.core.tty.TtyConnection;
import io.termd.core.util.Vector;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author daiyc
 */
public abstract class BaseSession implements Session {
    private final AtomicBoolean started = new AtomicBoolean(false);
    protected Deque<Interaction> interactionStack = new LinkedList<>();
    protected final TtyConnection connection;
    private Thread thread;

    public BaseSession(TtyConnection connection) {
        if (connection == null) {
            throw new NullPointerException();
        }

        this.connection = connection;
    }

    @Override
    public void load(Interaction interaction) {
        Preconditions.checkState(Thread.currentThread() == thread);
        Preconditions.checkArgument(interaction != null);

        inactive(interaction());

        install(interaction);
        active(interaction);
    }

    protected void install(Interaction interaction) {
        Preconditions.checkArgument(interaction != null);
        Preconditions.checkArgument(interaction != interaction());

        this.interactionStack.addLast(interaction);

        interaction.install();
    }

    protected void active(Interaction interaction) {
        interaction.active();
    }

    @Override
    public Interaction unload() {
        Preconditions.checkState(Thread.currentThread() == thread);
        Preconditions.checkState(!interactionStack.isEmpty());

        inactive(interaction());
        uninstall(interaction());

        interactionStack.removeLast();

        if (interaction() == null) {
            return null;
        }

        active(interaction());
        return interaction();
    }

    protected void inactive(Interaction interaction) {
        if (interaction == null) {
            return;
        }

        interaction.inactive();
    }

    protected void uninstall(Interaction interaction) {
        interaction.uninstall();
    }

    @Override
    public Interaction interaction() {
        return interactionStack.peekLast();
    }

    @Override
    public Vector size() {
        return connection.size();
    }

    @Override
    public void send(String data) {
        connection.write(data);
    }

    @Override
    public void send(int... data) {
        connection.stdoutHandler().accept(data);
    }

    @Override
    public boolean close() {
        connection.close();
        return true;
    }

    private void mainLoop() {
        Interaction interaction = interaction();

        while (interaction != null) {
            RetCode exitCode = doLoop(interaction);
            if (exitCode == RetCode.EXIT) {
                interaction = unload();
                send("\n");
            }
            if (interaction != interaction()) {
                interaction = interaction();
            }
        }

        close();
    }

    private RetCode doLoop(Interaction eventLoop) {
        while (true) {
            RetCode code = eventLoop.loop();
            if (code != RetCode.CONTINUE) {
                return code;
            }
            if (eventLoop != interaction()) {
                return RetCode.CONTINUE;
            }
        }
    }

    @Override
    public void start(Interaction interaction) {
        Preconditions.checkState(interactionStack.isEmpty());
        if (!started.compareAndSet(false, true)) {
            throw new IllegalStateException();
        }
        thread = new Thread(() -> {
            try {
                load(interaction);
                mainLoop();
            } finally {
                close();
            }
        });
        thread.start();
    }
}
