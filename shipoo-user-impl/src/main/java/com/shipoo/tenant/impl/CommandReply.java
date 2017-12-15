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

    UUID id;
    Instant timestamp;
    Optional<Integer> code;
    Optional<String> message;
    Optional<D> data;

    public CommandReply(UUID id, Instant timestamp, Optional<Integer> code, Optional<String> message, Optional<D> data) {
        this.id = id;
        this.timestamp = timestamp;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Value
    @EqualsAndHashCode(callSuper=true)
    public static class Void extends CommandReply<Void> {

        @JsonCreator
        @Builder
        public Void(UUID id, Instant timestamp, Optional<Integer> code, Optional<String> message) {
            super(id, timestamp, code, message, Optional.empty());
        }
    }

    @Value
    @EqualsAndHashCode(callSuper=true)
    public static class Done extends CommandReply<Void> {

        @JsonCreator
        @Builder
        public Done(UUID id, Instant timestamp) {
            super(id, timestamp, Optional.of(0), Optional.of("Command accepted"), Optional.empty());
        }
    }

}
