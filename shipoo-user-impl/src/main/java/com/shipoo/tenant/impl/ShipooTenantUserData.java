package com.shipoo.tenant.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
public class ShipooTenantUserData implements Jsonable {

    /**
     * The tenant name.
     */
    private final String name;

    @JsonCreator
    @Builder
    public ShipooTenantUserData(String name) {
        this.name = name;
    }

}
