package com.shipoo.tenant.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.lightbend.lagom.serialization.Jsonable;

public class ShipooTenantException extends RuntimeException implements Jsonable {

    public static final ShipooTenantException USER_CANT_PUT_MEMBER =
            new ShipooTenantException("The user can't put a memeber on this Tenant");

    @JsonCreator
    public ShipooTenantException(String s) {
        super(s);
    }
}
