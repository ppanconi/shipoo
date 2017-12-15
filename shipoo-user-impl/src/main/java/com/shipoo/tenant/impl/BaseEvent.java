package com.shipoo.tenant.impl;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseEvent {

    UUID id;
    Instant timestamp;

    public BaseEvent(UUID id, Instant timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }
}
