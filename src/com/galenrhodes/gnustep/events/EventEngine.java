package com.galenrhodes.gnustep.events;

import java.util.EventListener;
import java.util.EventObject;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class EventEngine<E extends EventObject, L extends EventListener> implements EventPublisher<E, L> {

    private final Set<L>        listeners = new LinkedHashSet<>();
    private final ReadWriteLock rwLock    = new ReentrantReadWriteLock(true);

    public EventEngine() {
        super();
    }

    @Override
    public void addEventListener(L listener) {
        rwLock.writeLock().lock();
        try { listeners.add(listener); } finally { rwLock.writeLock().unlock(); }
    }

    @Override
    public void asyncPublishEvent(E event) {
        EXECUTOR_HOLDER.EXECUTOR_SERVICE.submit(new Runnable() {
            @Override
            public void run() { publishEvent(event); }
        });
    }

    public abstract void dispatchEvent(E event, L listener);

    @Override
    public void publishEvent(E event) {
        rwLock.readLock().lock();
        try {
            for(L listener : listeners) {
                try { dispatchEvent(event, listener); } catch(Exception ignore) {}
            }
        }
        finally { rwLock.readLock().unlock(); }
    }

    @Override
    public void removeAllEventListeners() {
        rwLock.writeLock().lock();
        try { listeners.clear(); } finally { rwLock.writeLock().unlock(); }
    }

    @Override
    public void removeEventListener(L listener) {
        rwLock.writeLock().lock();
        try { listeners.remove(listener); } finally { rwLock.writeLock().unlock(); }
    }

    private static final class EXECUTOR_HOLDER {

        private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    }

}
