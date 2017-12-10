package com.shipoo.tenant.impl;

import lombok.Value;

import java.util.UUID;

@Value
public class ShipooTenantMember {

    /**
     * The user that created the item.
     */
    private final UUID user;

    /**
     * the user role in the tenant
     */
    private final ShipooTenantRole role;

    
}
