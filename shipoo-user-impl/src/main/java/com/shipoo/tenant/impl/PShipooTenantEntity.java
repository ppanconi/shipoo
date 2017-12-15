package com.shipoo.tenant.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;
import org.pcollections.TreePVector;

import java.util.Optional;
import java.util.UUID;

import com.shipoo.tenant.impl.PShipooTenantCommand.*;
import com.shipoo.tenant.impl.PShipooTenantEvent.*;
import scala.collection.immutable.TreeMap;

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

        b.setCommandHandler(CreateTenant.class, (createCommand, ctx) -> {
            PShipooTenantState newState = new PShipooTenantState(UUID.fromString(entityId()),
                    createCommand.getCreator(), createCommand.getTenantData(), HashTreePMap.empty());
            return ctx.thenPersist(new TenantCreated(newState), (e) -> ctx.reply(Done.getInstance()));
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
            return state().get().acceptPutTenantMenberEvent(cmd, ctx);
        });

        b.setEventHandlerChangingBehavior(TenantMemberPutted.class, event -> {
            //TOTO

        });


        b.setCommandHandler(RemoveTenantMember.class, ( cmd, ctx) -> {
            ctx.commandFailed(new EntityNotFound(entityId()));
            return ctx.done();
        });


        return b.build();

    }

    private void getState(GetTenant get, ReadOnlyCommandContext ctx) {
        ctx.reply(state());
    }


    public Persist acceptPutTenantMenberEvent(PutTenantMember cmd,
                                              CommandContext<ShipooTenantMember> ctx) {

        return null;
    }
}