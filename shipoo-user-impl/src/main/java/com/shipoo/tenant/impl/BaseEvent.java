package com.shipoo.tenant.impl;

import com.lightbend.lagom.serialization.Jsonable;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseEvent implements Jsonable {

    private UUID id;
    private Instant timestamp;

    public BaseEvent(UUID id, Instant timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "BaseEvent{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEvent baseEvent = (BaseEvent) o;

        if (id != null ? !id.equals(baseEvent.id) : baseEvent.id != null) return false;
        return timestamp != null ? timestamp.equals(baseEvent.timestamp) : baseEvent.timestamp == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
