package com.shipoo.tenant.impl;

import lombok.Value;

import java.util.UUID;

@Value
public class ShipooTenantUserData {


    /**
     * The tenant name.
     */
    private final String name;
}
