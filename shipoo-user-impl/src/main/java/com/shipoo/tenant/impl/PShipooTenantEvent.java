package com.shipoo.tenant.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

public interface PShipooTenantEvent extends Jsonable, AggregateEvent<PShipooTenantEvent> {

    int NUM_SHARDS = 4;
    AggregateEventShards<PShipooTenantEvent> TAG = AggregateEventTag.sharded(PShipooTenantEvent.class, NUM_SHARDS);

    @Override
    default AggregateEventTagger<PShipooTenantEvent> aggregateTag() {
        return TAG;
    }


    /**
     * Event of creation
     */
    @Value
    @EqualsAndHashCode(callSuper=true)
    class TenantCreated extends BaseEvent implements PShipooTenantEvent {
        PShipooTenantState tenant;

        @JsonCreator
        @Builder
        public TenantCreated(UUID id, Instant timestamp, PShipooTenantState tenant) {
            super(id, timestamp);
            this.tenant = tenant;
        }

        @Override
        public String toString() {
            return super.toString() + "TenantCreated{tenant=" + tenant.toString() + "}";
        }
    }

    /**
     * Event of tenant update
     */
    @Value
    @EqualsAndHashCode(callSuper=true)
    class TenantUpdated extends BaseEvent implements PShipooTenantEvent {
        UUID commander;
        ShipooTenantUserData updateData;

        @JsonCreator
        @Builder
        public TenantUpdated(UUID id, Instant timestamp, UUID commander, ShipooTenantUserData updateData) {
            super(id, timestamp);
            this.commander = commander;
            this.updateData = updateData;
        }
    }

    /**
     * Event of member added to tenant
     */
    @Value
    @EqualsAndHashCode(callSuper=true)
    class TenantMemberPutted extends BaseEvent implements PShipooTenantEvent {
        UUID commander;
        ShipooTenantMember memberPutted;

        @JsonCreator
        @Builder
        public TenantMemberPutted(UUID id, Instant timestamp, UUID commander, ShipooTenantMember memberPutted) {
            super(id, timestamp);
            this.commander = commander;
            this.memberPutted = memberPutted;
        }
    }

    /**
     * Event of member added to tenant
     */
    @Value
    @EqualsAndHashCode(callSuper=true)
    class TenantMemberRemoved extends BaseEvent implements PShipooTenantEvent {
        UUID commander;
        ShipooTenantMember memberRemoved;

        @JsonCreator
        @Builder
        public TenantMemberRemoved(UUID id, Instant timestamp, UUID commander, ShipooTenantMember memberRemoved) {
            super(id, timestamp);
            this.commander = commander;
            this.memberRemoved = memberRemoved;
        }
    }

}
