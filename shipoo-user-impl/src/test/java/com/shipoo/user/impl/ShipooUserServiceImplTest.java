package com.shipoo.user.impl;

import com.lightbend.lagom.javadsl.api.transport.NotFound;
import com.shipoo.test.Await;
import com.shipoo.user.ShipooUserService;
import com.shipoo.user.vo.ShipooUser;
import com.shipoo.user.vo.ShipooUserRegistration;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.withServer;
import static org.junit.Assert.*;

public class ShipooUserServiceImplTest {

    static final String email = "admin@gmail.com";
    static final Optional<String> username = Optional.of("admin");
    static final Optional<String> firstName = Optional.of("john");
    static final Optional<String> familyName = Optional.of("smith");

    static final ShipooUserRegistration userRegistration = new ShipooUserRegistration(
            email,
            firstName,
            familyName,
            username
    );

    private void assertUser(ShipooUser user) {
        assertEquals(email, user.getEmail());
        assertEquals(username, user.getUsername());
        assertEquals(firstName, user.getFirstName());
        assertEquals(familyName, user.getFamilyName());
    }

    private static ShipooUserService service;

    @Test
    public void createUser() throws Exception {

        withServer(defaultSetup().withCassandra(true), server -> {
            service = server.client(ShipooUserService.class);
            ShipooUser createdUser = createNewUser(userRegistration);
            assertUser(createdUser);

        });
    }

    @Test
    public void getUser() throws Exception {

        withServer(defaultSetup().withCassandra(true), server -> {
            service = server.client(ShipooUserService.class);
            ShipooUser user = getUser(createNewUser(userRegistration).getId());
            assertUser(user);

        });
    }

    @Test(expected = com.lightbend.lagom.javadsl.api.transport.NotFound.class)
    public void getNoCreatedUser() throws Exception {

        UUID userId = UUID.randomUUID();
        withServer(defaultSetup().withCassandra(true), server -> {
            service = server.client(ShipooUserService.class);
            try {
                ShipooUser user = getUser(userId);
            } catch (RuntimeException ex) {
                NotFound notFound = (NotFound) ex.getCause();
                assertEquals("Item " + userId + " not found", notFound.getMessage());
                throw  notFound;
            }
        });
    }

    private ShipooUser createNewUser(ShipooUserRegistration userRegistration) {
        return Await.result(
                service.createUser()
                        .invoke(userRegistration)
        );
    }

    private ShipooUser getUser(UUID userId) {
        return Await.result(
                service.getUser(userId)
                        .invoke()
        );
    }
}