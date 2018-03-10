package com.shipoo.tenant;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.deser.PathParamSerializers;
import com.lightbend.lagom.javadsl.api.transport.Method;
import com.shipoo.tenant.vo.ShipooTenant;
import com.shipoo.tenant.vo.ShipooTenantMember;

import java.util.UUID;

import static com.lightbend.lagom.javadsl.api.Service.*;

public interface ShipooTenantService extends Service {

    @Override
    default Descriptor descriptor() {
        return named("shipootenat").withCalls(
                pathCall("/api/tenant", this::createTenant),
                pathCall("/api/tenant/:id", this::getTenant),
                restCall(Method.PUT, "/api/tenant/:id", this::updateTenant),
                restCall(Method.PUT, "/api/tenant/member", this::putTenant)
        ).withPathParamSerializer(
                UUID.class, PathParamSerializers.required("UUID", UUID::fromString, UUID::toString)
        );
    }

    ServiceCall<ShipooTenant,ShipooTenant> createTenant();

    ServiceCall<NotUsed,ShipooTenant> getTenant(UUID tenantId);

    ServiceCall<ShipooTenant, Done> updateTenant();

    ServiceCall<ShipooTenantMember,Done> putTenant();

}
