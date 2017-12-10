package com.shipoo.user.impl;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Optional;

public interface PShipooUserCommand extends Jsonable {

    @Value
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
    final class CreatePShipooUser implements PShipooUserCommand, PersistentEntity.ReplyType<Optional<PShipooUser>> {
        private final String email;
        private final Optional<String> firstName;
        private final Optional<String> familyName;
        private final Optional<String> username;
    }


    enum GetPShipooUser implements PShipooUserCommand, PersistentEntity.ReplyType<Optional<PShipooUser>> {
        INSTANCE
    }
}
