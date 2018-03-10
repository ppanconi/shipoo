package com.shipoo.tenant.impl;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.shipoo.tenant.ShipooTenantService;
import com.shipoo.tenant.vo.ShipooTenant;
import com.shipoo.tenant.vo.ShipooTenantMember;

import java.util.UUID;

public class ShipooTenantServiceImpl implements ShipooTenantService{

    @Override
    public ServiceCall<ShipooTenant, ShipooTenant> createTenant() {
        return null;
    }

    @Override
    public ServiceCall<NotUsed, ShipooTenant> getTenant(UUID tenantId) {
        return null;
    }

    @Override
    public ServiceCall<ShipooTenant, Done> updateTenant() {
        return null;
    }

    @Override
    public ServiceCall<ShipooTenantMember, Done> putTenant() {
        return null;
    }
}
