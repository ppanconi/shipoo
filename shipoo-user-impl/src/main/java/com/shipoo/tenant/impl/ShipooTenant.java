package com.shipoo.tenant.impl;

import lombok.Value;

import java.util.UUID;

@Value
public class ShipooTenant {

    /**
     * The tenant id.
     */
    private final UUID id;

    /**
     * The user that created the item.
     */
    private final UUID creator;

    /**
     * The tenant name.
     */
    private final String name;
}
