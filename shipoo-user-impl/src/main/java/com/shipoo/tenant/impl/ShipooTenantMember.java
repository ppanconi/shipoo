package com.shipoo.tenant.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
public class ShipooTenantMember implements Jsonable {

    @JsonCreator
    @Builder
    public ShipooTenantMember(UUID user, ShipooTenantRole role) {
        this.user = user;
        this.role = role;
    }

    /**
     * The user that created the item.
     */
    private final UUID user;

    /**
     * the user role in the tenant
     */
    private final ShipooTenantRole role;

    
}
