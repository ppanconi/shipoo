package com.shipoo.tenant.impl;

import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

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
    class TenantCreated implements PShipooTenantEvent {
        PShipooTenantState tenant;
    }

    /**
     * Event of tenant update
     */
    @Value
    class TenantUpdated implements PShipooTenantEvent {
        UUID commander;
        ShipooTenantUserData updateData;
    }

    /**
     * Event of member added to tenant
     */
    @Value
    class TenantMemberPutted implements PShipooTenantEvent {
        UUID commander;
        ShipooTenantMember memberPutted;
    }

    /**
     * Event of member added to tenant
     */
    @Value
    class TenantMemberRemoved implements PShipooTenantEvent {
        UUID commander;
        ShipooTenantMember memberRemoved;
    }
}
