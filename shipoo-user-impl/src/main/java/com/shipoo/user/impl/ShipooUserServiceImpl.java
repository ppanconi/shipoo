package com.shipoo.user.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.shipoo.user.ShipooUserService;
import com.shipoo.user.vo.ShipooUser;
import com.shipoo.user.vo.ShipooUserRegistration;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

import static com.shipoo.user.impl.ApiVoMappers.toApi;

public class ShipooUserServiceImpl implements ShipooUserService {

    private final PersistentEntityRegistry registry;

    @Inject
    public ShipooUserServiceImpl(PersistentEntityRegistry registry) {
        this.registry = registry;

        registry.register(PShipooUserEntity.class);
    }

    @Override
    public ServiceCall<ShipooUserRegistration, ShipooUser> createUser() {
        return shipooUserRegistration -> {
            UUID userId = UUID.randomUUID();
            PersistentEntityRef<PShipooUserCommand> userEntity = entityRef(userId);
            return userEntity.ask(new PShipooUserCommand.CreatePShipooUser(
                    shipooUserRegistration.getEmail(),
                    shipooUserRegistration.getFirstName(),
                    shipooUserRegistration.getFamilyName(),
                    shipooUserRegistration.getUsername()
                    ))
                    .thenApply(mayBeUser -> toApi( ((Optional<PShipooUser>)mayBeUser).get() ));
        };
    }

    @Override
    public ServiceCall<NotUsed, ShipooUser> getUser(UUID userId) {
        return r -> entityRef(userId).ask(PShipooUserCommand.GetPShipooUser.INSTANCE)
                .thenApply(mayBeUser -> {
                    if (mayBeUser.isPresent()) {
                        return toApi(mayBeUser.get());
                    } else {
                        throw new NotFound("Item " + userId + " not found");
                    }
                });
    }

    private PersistentEntityRef<PShipooUserCommand> entityRef(UUID userId) {
        return registry.refFor(PShipooUserEntity.class, userId.toString());
    }


}
