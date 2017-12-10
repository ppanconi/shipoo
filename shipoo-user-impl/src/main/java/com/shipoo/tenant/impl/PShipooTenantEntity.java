package com.shipoo.tenant.impl;

import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;

public class PShipooTenantEntity
        extends PersistentEntity<PShipooTenantCommand, PShipooTenantEvent, PShipooTenantState> {


    @Override
    public Behavior initialBehavior(Optional<PShipooTenantState> snapshotState) {
        return null;
    }


}
