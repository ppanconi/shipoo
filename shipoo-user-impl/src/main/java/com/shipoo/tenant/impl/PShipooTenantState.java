package com.shipoo.tenant.impl;


import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;
import org.pcollections.PSequence;

import java.util.UUID;

@Value
public class PShipooTenantState implements Jsonable {

    /**
     * The tenant id.
     */
    private final UUID id;

    /**
     * The user that created the item.
     */
    private final UUID creator;

    /**
     * The tenant date that the user can provides at creation time
     * or during update
     */
    private final ShipooTenantUserData tenant;

    /**
     * The collection of members of this tenant
     */
    private final PSequence<ShipooTenantMember> members;

}
