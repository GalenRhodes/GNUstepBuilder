package com.galenrhodes.gnustep.builder.common.events;

import java.util.EventListener;
import java.util.EventObject;

public interface EventPublisher<E extends EventObject, L extends EventListener> {

    void addEventListener(L listener);

    void asyncPublishEvent(E event);

    void publishEvent(E event);

    void removeAllEventListeners();

    void removeEventListener(L listener);

}
