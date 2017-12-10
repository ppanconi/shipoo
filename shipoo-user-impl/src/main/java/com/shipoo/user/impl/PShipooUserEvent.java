package com.shipoo.user.impl;

import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

public interface PShipooUserEvent extends Jsonable, AggregateEvent<PShipooUserEvent> {

    int NUM_SHARDS = 4;
    AggregateEventShards<PShipooUserEvent> TAG = AggregateEventTag.sharded(PShipooUserEvent.class, NUM_SHARDS);

    @Value
    final class PShipooUserCreated implements PShipooUserEvent {
        private final PShipooUser user;
    }

    @Override
    default AggregateEventTagger<PShipooUserEvent> aggregateTag() {
        return TAG;
    }

}
