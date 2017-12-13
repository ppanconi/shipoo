package com.shipoo.tenant.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.shipoo.user.impl.PShipooUser;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import java.util.Optional;
import java.util.UUID;

public class PShipooTenantEntity
        extends PersistentEntity<PShipooTenantCommand, PShipooTenantEvent, Optional<PShipooTenantState>> {

    public static class EntityNotFound extends Exception {
        public EntityNotFound(String id) {
            super("Entity non exist " + id);
        }
    }

    @Override
    public Behavior initialBehavior(Optional<Optional<PShipooTenantState>> snapshotState) {
        return null;
    }

    private Behavior notCreated() {
        BehaviorBuilder b = newBehaviorBuilder(Optional.empty());

        b.setReadOnlyCommandHandler(PShipooTenantCommand.GetTenant.class, (get, ctx) ->
                ctx.reply(state())
        );

        b.setCommandHandler(PShipooTenantCommand.CreateTenant.class, (createCommand, ctx) -> {

            PShipooTenantState newState = new PShipooTenantState(UUID.fromString(entityId()),
                    createCommand.getCreator(), createCommand.getTenantData(), TreePVector.empty());

            return ctx.thenPersist(new PShipooTenantEvent.TenantCreated(newState), (e) -> ctx.reply(Done.getInstance()));
        });

        b.setCommandHandler(PShipooTenantCommand.AddTenantMember.class, (cmd, ctx) ->
                ctx.commandFailed(new EntityNotFound(entityId()))
        );


        b.setEventHandlerChangingBehavior(PShipooTenantEvent.TenantCreated.class, event -> created(event.getTenant()));

        return b.build();
    }

    private Behavior created(PShipooTenantState tenant) {
    }
}