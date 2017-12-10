package com.shipoo.tenant.impl;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

import java.util.Optional;
import java.util.UUID;

public interface PShipooTenantCommand extends Jsonable {

    @Value
    final class CreateTenant
            implements PShipooTenantCommand, PersistentEntity.ReplyType<Optional<PShipooTenantState>> {
        ShipooTenant tenantData;
    }

    @Value
    final class UpdateTenant
        implements PShipooTenantCommand, PersistentEntity.ReplyType<Optional<PShipooTenantState>> {
        UUID commander;
        ShipooTenant tenantData;
    }

    @Value
    final class AddTenantMember
        implements PShipooTenantCommand, PersistentEntity.ReplyType<Optional<ShipooTenantMember>> {
        UUID commander;
        UUID member;
        ShipooTenantRole role;
    }

}
