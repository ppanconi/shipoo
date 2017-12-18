package com.shipoo.tenant.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import com.shipoo.tenant.impl.PShipooTenantCommand.CreateTenant;
import com.shipoo.tenant.impl.PShipooTenantCommand.GetTenant;
import com.shipoo.tenant.impl.PShipooTenantCommand.PutTenantMember;
import com.shipoo.tenant.impl.PShipooTenantCommand.RemoveTenantMember;
import com.shipoo.tenant.impl.PShipooTenantEvent.TenantCreated;
import com.shipoo.tenant.impl.PShipooTenantEvent.TenantMemberPutted;
import lombok.EqualsAndHashCode;
import org.pcollections.HashTreePMap;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static com.shipoo.tenant.impl.ShipooTenantRole.CREATOR;

public class PShipooTenantEntity
        extends PersistentEntity<PShipooTenantCommand, PShipooTenantEvent, Optional<PShipooTenantState>> {

    @EqualsAndHashCode(callSuper = false)
    public static class EntityNotFound extends Exception implements Jsonable {

        @JsonCreator
        public EntityNotFound(String id) {
            super("Tenant non exist " + id);
        }
    }

    @Override
    public Behavior initialBehavior(Optional<Optional<PShipooTenantState>> snapshotState) {
        Optional<PShipooTenantState> state = snapshotState.flatMap(Function.identity());
        if (state.isPresent()) {
            return created(state.get());
        } else {
            return notCreated();
        }
    }

    private Behavior notCreated() {
        BehaviorBuilder b = newBehaviorBuilder(Optional.empty());

        /**
         * CreateTenant command
         */
        b.setCommandHandler(CreateTenant.class, (createCommand, ctx) -> {

            ShipooTenantMember creatorMember = ShipooTenantMember.builder().user(createCommand.getCreator())
                    .role(CREATOR).build();

            PShipooTenantState newState = new PShipooTenantState(UUID.fromString(entityId()),
                    createCommand.getCreator(), createCommand.getTenantData(),
                    HashTreePMap.singleton(creatorMember.getUser(), creatorMember));

            TenantCreated event = TenantCreated.builder()
                    .id(UUID.randomUUID())
                    .timestamp(Instant.now())
                    .tenant(newState)
                    .build();


            return ctx.thenPersist(event, (e) -> ctx.reply(
                    CommandReply.Done.builder()
                            .id(event.getId())
                            .timestamp(event.getTimestamp())
                            .build()));
        });

        b.setEventHandlerChangingBehavior(TenantCreated.class, event -> created(event.getTenant()));

        b.setReadOnlyCommandHandler(GetTenant.class, this::getState);

        b.setCommandHandler(PutTenantMember.class, (cmd, ctx) -> {
            ctx.commandFailed(new EntityNotFound(entityId()));
            return ctx.done();
        });

        b.setCommandHandler(RemoveTenantMember.class, ( cmd, ctx) -> {
            ctx.commandFailed(new EntityNotFound(entityId()));
            return ctx.done();
        });

        return b.build();
    }

    private Behavior created(PShipooTenantState tenant) {
        BehaviorBuilder b = newBehaviorBuilder(Optional.of(tenant));

        b.setCommandHandler(CreateTenant.class, (createCommand, ctx) -> {
            ctx.invalidCommand("Tenant already exists.");
            return ctx.done();
        });

        b.setReadOnlyCommandHandler(GetTenant.class, this::getState);

        /**
         * PutMember
         */
        b.setCommandHandler(PutTenantMember.class, (cmd, ctx) -> {
            assert state().isPresent();
            return state().get().acceptPutTenantMemberCommand(cmd, ctx);
        });

        b.setEventHandler(TenantMemberPutted.class, event -> {
            assert state().isPresent();
            return Optional.of(state().get().applyTenantMemberPuttedEvent(event));
        });


        /**
         * Remove Tenant
         */
        b.setCommandHandler(RemoveTenantMember.class, ( cmd, ctx) -> {
            assert state().isPresent();
            return state().get().acceptRemoveTenantMemberCommand(cmd, ctx);
        });

        b.setEventHandler(PShipooTenantEvent.TenantMemberRemoved.class, event -> {
            assert state().isPresent();
            return Optional.of(state().get().applyTenantMemberRemovedEvent(event));
        });


        return b.build();

    }

    private void getState(GetTenant get, ReadOnlyCommandContext<Optional<PShipooTenantState>> ctx) {
        ctx.reply(state());
    }

}