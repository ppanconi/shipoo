package com.shipoo.tenant.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public abstract class CommandReply<D> implements Jsonable {

    private UUID id;
    private Instant timestamp;
    private Integer code;
    private String message;
    private Optional<D> data;

    public CommandReply(UUID id, Instant timestamp, Integer code, String message, Optional<D> data) {
        this.id = id;
        this.timestamp = timestamp;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public UUID getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Optional<D> getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandReply<?> that = (CommandReply<?>) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return data != null ? data.equals(that.data) : that.data == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CommandReply{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * Concrete classes
     */

    @Value
    @EqualsAndHashCode(callSuper=true)
    public static class Void extends CommandReply<Void> {

        @JsonCreator
        @Builder
        public Void(UUID id, Instant timestamp, Integer code, String message) {
            super(id, timestamp, code, message, Optional.empty());
        }
    }

    @Value
    @EqualsAndHashCode(callSuper=true)
    public static class Done extends CommandReply<Void> {

        @JsonCreator
        @Builder
        public Done(UUID id, Instant timestamp) {
            super(id, timestamp, 0, "Command accepted", Optional.empty());
        }
    }

}
