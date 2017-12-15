package com.shipoo.tenant.impl;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.shipoo.tenant.impl.PShipooTenantCommand.CreateTenant;
import com.shipoo.tenant.impl.PShipooTenantCommand.GetTenant;
import com.shipoo.tenant.impl.PShipooTenantCommand.PutTenantMember;
import com.shipoo.tenant.impl.PShipooTenantCommand.RemoveTenantMember;
import com.shipoo.tenant.impl.PShipooTenantEvent.TenantCreated;
import com.shipoo.tenant.impl.PShipooTenantEvent.TenantMemberPutted;
import org.pcollections.HashTreePMap;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class PShipooTenantEntity
        extends PersistentEntity<PShipooTenantCommand, PShipooTenantEvent, Optional<PShipooTenantState>> {

    public static class EntityNotFound extends Exception {

        public EntityNotFound(String id) {
            super("Tenant non exist " + id);
        }
    }

    @Override
    public Behavior initialBehavior(Optional<Optional<PShipooTenantState>> snapshotState) {
        return null;
    }

    private Behavior notCreated() {
        BehaviorBuilder b = newBehaviorBuilder(Optional.empty());

        /**
         * CreateTenat command
         */
        b.setCommandHandler(CreateTenant.class, (createCommand, ctx) -> {

            PShipooTenantState newState = new PShipooTenantState(UUID.fromString(entityId()),
                    createCommand.getCreator(), createCommand.getTenantData(), HashTreePMap.empty());

            TenantCreated event = TenantCreated.builder()
                    .id(UUID.randomUUID())
                    .timestamp(Instant.now())
                    .tenant(newState)
                    .build();


            return ctx.thenPersist(event, (e) -> ctx.reply(
                    CommandReply.Done.builder()
                            .id(event.id)
                            .timestamp(event.timestamp)
                            .build()));
        });

        b.setReadOnlyCommandHandler(GetTenant.class, this::getState);

        b.setCommandHandler(PutTenantMember.class, (cmd, ctx) -> {
                ctx.commandFailed(new EntityNotFound(entityId()));
                return ctx.done();
        });

        b.setCommandHandler(RemoveTenantMember.class, ( cmd, ctx) -> {
                ctx.commandFailed(new EntityNotFound(entityId()));
                return ctx.done();
        });

        b.setEventHandlerChangingBehavior(TenantCreated.class, event -> created(event.getTenant()));

        return b.build();
    }

    private Behavior created(PShipooTenantState tenant) {
        BehaviorBuilder b = newBehaviorBuilder(Optional.of(tenant));

        b.setCommandHandler(CreateTenant.class, (createCommand, ctx) -> {
            ctx.invalidCommand("Tenant already exists.");
            return ctx.done();
        });

        b.setReadOnlyCommandHandler(GetTenant.class, this::getState);

        b.setCommandHandler(PutTenantMember.class, (cmd, ctx) -> {
            assert state().isPresent();
            return state().get().acceptPutTenantMemberCommand(cmd, ctx);
        });

        b.setEventHandler(TenantMemberPutted.class, event -> {
            assert state().isPresent();
            return Optional.of(state().get().applyTenantMemberPuttedEvent(event));
        });


        b.setCommandHandler(RemoveTenantMember.class, ( cmd, ctx) -> {
            ctx.commandFailed(new EntityNotFound(entityId()));
            return ctx.done();
        });


        return b.build();

    }

    private void getState(GetTenant get, ReadOnlyCommandContext<Optional<PShipooTenantState>> ctx) {
        ctx.reply(state());
    }

}