package com.dyc.lark.core.interaction;

import com.dyc.lark.core.session.Session;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author daiyc
 */
public abstract class BaseInteraction implements Interaction {

    protected Session session;

    protected AtomicReference<STATUS> status = new AtomicReference<>(STATUS.IDLE);

    protected String prompt;

    public BaseInteraction(Session session, String prompt) {
        this.session = session;
        this.prompt = prompt == null ? "> " : prompt;
    }

    public BaseInteraction(Session session) {
        this(session, null);
    }

    @Override
    public void install() {
        if (!status.compareAndSet(STATUS.IDLE, STATUS.INACTIVE)) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void uninstall() {
        if (!status.compareAndSet(STATUS.INACTIVE, STATUS.IDLE)) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void active() {
        if (!status.compareAndSet(STATUS.INACTIVE, STATUS.ACTIVE)) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void inactive() {
        if (!status.compareAndSet(STATUS.ACTIVE, STATUS.INACTIVE)) {
            throw new IllegalStateException();
        }
    }

    enum STATUS {
        IDLE,
        ACTIVE,
        INACTIVE;
    }
}
