package com.shipoo.tenant.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

import java.util.Optional;
import java.util.UUID;

public interface PShipooTenantCommand extends Jsonable {

    @Value
    final class CreateTenant
            implements PShipooTenantCommand, PersistentEntity.ReplyType<Done> {
        ShipooTenantUserData tenantData;
        UUID id;
        UUID creator;
    }

    @Value
    final class GetTenant
            implements PShipooTenantCommand, PersistentEntity.ReplyType<Optional<PShipooTenantState>> {
        UUID id;
    }

    @Value
    final class UpdateTenant
        implements PShipooTenantCommand, PersistentEntity.ReplyType<Optional<PShipooTenantState>> {
        UUID commander;
        ShipooTenantUserData tenantData;
    }

    @Value
    final class PutTenantMember
        implements PShipooTenantCommand, PersistentEntity.ReplyType<Optional<ShipooTenantMember>> {
        UUID commander;
        UUID member;
        ShipooTenantRole role;
    }

    @Value
    final class RemoveTenantMember
            implements PShipooTenantCommand, PersistentEntity.ReplyType<Optional<ShipooTenantMember>> {
        UUID commander;
        UUID member;
    }

}
