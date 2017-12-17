package com.shipoo.tenant.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;
import java.util.UUID;

public interface PShipooTenantCommand extends Jsonable {

    @Value
    final class CreateTenant
            implements PShipooTenantCommand, PersistentEntity.ReplyType<CommandReply.Done> {
        ShipooTenantUserData tenantData;
        UUID id;
        UUID creator;

        @JsonCreator
        @Builder
        public CreateTenant(ShipooTenantUserData tenantData, UUID id, UUID creator) {
            this.tenantData = tenantData;
            this.id = id;
            this.creator = creator;
        }
    }

    @Value
    final class GetTenant
            implements PShipooTenantCommand, PersistentEntity.ReplyType<Optional<PShipooTenantState>> {
        UUID id;

        @JsonCreator
        @Builder
        public GetTenant(UUID id) {
            this.id = id;
        }
    }

    @Value
    final class UpdateTenant
        implements PShipooTenantCommand, PersistentEntity.ReplyType<Optional<CommandReply.Done>> {
        UUID commander;
        ShipooTenantUserData tenantData;

        @JsonCreator
        @Builder
        public UpdateTenant(UUID commander, ShipooTenantUserData tenantData) {
            this.commander = commander;
            this.tenantData = tenantData;
        }
    }

    @Value
    final class PutTenantMember
        implements PShipooTenantCommand, PersistentEntity.ReplyType<Optional<CommandReply.Done>> {
        UUID commander;
        UUID member;
        ShipooTenantRole role;

        @JsonCreator
        @Builder
        public PutTenantMember(UUID commander, UUID member, ShipooTenantRole role) {
            this.commander = commander;
            this.member = member;
            this.role = role;
        }
    }

    @Value
    final class RemoveTenantMember
            implements PShipooTenantCommand, PersistentEntity.ReplyType<Optional<CommandReply.Done>> {
        UUID commander;
        UUID member;

        @JsonCreator
        @Builder
        public RemoveTenantMember(UUID commander, UUID member) {
            this.commander = commander;
            this.member = member;
        }
    }

}
