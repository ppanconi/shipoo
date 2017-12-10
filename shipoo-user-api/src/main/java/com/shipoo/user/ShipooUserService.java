package com.shipoo.user;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.deser.PathParamSerializers;
import com.shipoo.user.vo.ShipooUser;
import com.shipoo.user.vo.ShipooUserRegistration;

import java.util.UUID;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

public interface ShipooUserService extends Service {

    ServiceCall<ShipooUserRegistration, ShipooUser> createUser();

    ServiceCall<NotUsed, ShipooUser> getUser(UUID userId);

    @Override
    default Descriptor descriptor() {
        return named("shipoouser").withCalls(
                pathCall("/api/user", this::createUser),
                pathCall("/api/user/:id", this::getUser)
//              ,  pathCall("/api/user?pageNo&pageSize", this::getUsers)
        ).withPathParamSerializer(
                UUID.class, PathParamSerializers.required("UUID", UUID::fromString, UUID::toString)
        );
    }

}
