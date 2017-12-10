package com.shipoo.user.impl;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import com.shipoo.user.impl.PShipooUserCommand.*;
import com.shipoo.user.impl.PShipooUserEvent.*;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class PShipooUserEntity extends PersistentEntity<PShipooUserCommand, PShipooUserEvent, Optional<PShipooUser> > {

    public static final String USER_ALREADY_EXISTS = "User already exists.";

    @Override
    public Behavior initialBehavior(Optional<Optional<PShipooUser>> snapshotState) {
        Optional<PShipooUser> pShipooUser = snapshotState.flatMap(Function.identity());

        if (pShipooUser.isPresent()) {
            return created(pShipooUser.get());
        } else {
            return notCreated();
        }
    }

    private Behavior created(PShipooUser user) {
        BehaviorBuilder b = newBehaviorBuilder(Optional.of(user));

        b.setReadOnlyCommandHandler(GetPShipooUser.class, (get, ctx) ->
                ctx.reply(state())
        );

        b.setReadOnlyCommandHandler(CreatePShipooUser.class, (create, ctx) ->
                ctx.invalidCommand(USER_ALREADY_EXISTS)
        );

        return b.build();
    }

    private Behavior notCreated() {
        BehaviorBuilder b = newBehaviorBuilder(Optional.empty());

        b.setReadOnlyCommandHandler(GetPShipooUser.class, (get, ctx) ->
                ctx.reply(state())
        );

        b.setCommandHandler(CreatePShipooUser.class, (create, ctx) -> {

            PShipooUser user = new PShipooUser(
                    UUID.fromString(entityId()),
                    create.getEmail(),
                    create.getFirstName(),
                    create.getFamilyName(),
                    create.getUsername());
            return ctx.thenPersist(new PShipooUserCreated(user), (e) -> ctx.reply(Optional.of(user)));
        });

        b.setEventHandlerChangingBehavior(PShipooUserCreated.class, user -> created(user.getUser()));

        return b.build();
    }

}
