package com.shipoo.tenant.impl;


import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;
import org.pcollections.PSequence;

import java.util.Optional;
import java.util.UUID;

@Value
public class PShipooTenantState implements Jsonable {


    private final Optional<ShipooTenant> tenant;


    private final PSequence<ShipooTenantMember> members;


}
