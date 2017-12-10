package com.shipoo.user.impl;

import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver;
import com.shipoo.user.impl.PShipooUserCommand.GetPShipooUser;
import org.junit.*;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.shipoo.user.impl.PShipooUserEntity.USER_ALREADY_EXISTS;
import static org.junit.Assert.*;

public class PShipooUserEntityTest {

    private static ActorSystem system;
    private PersistentEntityTestDriver<PShipooUserCommand, PShipooUserEvent, Optional<PShipooUser>> driver;

    @BeforeClass
    public static void startActorSystem() {
        system = ActorSystem.create("PShipooUserEntityTest");
    }

    @AfterClass
    public static void shutdownActorSystem() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }


    private final UUID id = UUID.randomUUID();
    private final String username = "admin";
    private final String email = "admin@gmail.com";

    private final PShipooUser user = new PShipooUser(id, email, Optional.empty(),
            Optional.empty(), Optional.of(username));

    @Before
    public void createTestDriver() {
        driver = new PersistentEntityTestDriver<>(system, new PShipooUserEntity(), id.toString());
    }

    @After
    public void noIssues() {
        if (!driver.getAllIssues().isEmpty()) {
            driver.getAllIssues().forEach(System.out::println);
            fail("There were issues " + driver.getAllIssues().get(0));
        }
    }

    @Test
    public void testCreateUser() {
        PersistentEntityTestDriver.Outcome<PShipooUserEvent, Optional<PShipooUser>> outcome = driver.run(
                new PShipooUserCommand.CreatePShipooUser(user.getEmail(),
                        user.getFirstName(), user.getFamilyName(), user.getUsername())
        );

        PShipooUserEvent.PShipooUserCreated pShipooUserEvent = (PShipooUserEvent.PShipooUserCreated)
                outcome.events().get(0);

        assertEquals(username, pShipooUserEvent.getUser().getUsername().get());
        assertEquals(email,  pShipooUserEvent.getUser().getEmail());
        assertEquals(id, pShipooUserEvent.getUser().getId());

        assertEquals(Collections.emptyList(), driver.getAllIssues());
    }

    @Test
    public void testRejectDuplicateCreate() {
        driver.run(new PShipooUserCommand.CreatePShipooUser(user.getEmail(),
                        user.getFirstName(), user.getFamilyName(), user.getUsername()));

        PersistentEntityTestDriver.Outcome<PShipooUserEvent, Optional<PShipooUser>> outcome =
                driver.run(
                        new PShipooUserCommand.CreatePShipooUser(user.getEmail(),
                                user.getFirstName(), user.getFamilyName(), user.getUsername())
                );

        Object replay = outcome.getReplies().get(0);
        assertEquals(PShipooUserEntity.InvalidCommandException.class, replay.getClass());
        assertEquals(USER_ALREADY_EXISTS, ((PersistentEntity.InvalidCommandException) replay).getMessage());

        assertEquals(Collections.emptyList(), outcome.events());
        assertEquals(Collections.emptyList(), driver.getAllIssues());
    }

    @Test
    public void testGetCreated() {
        driver.run(new PShipooUserCommand.CreatePShipooUser(user.getEmail(),
                user.getFirstName(), user.getFamilyName(), user.getUsername()));

        PersistentEntityTestDriver.Outcome<PShipooUserEvent, Optional<PShipooUser>> outcome = driver.run(
                GetPShipooUser.INSTANCE);

        Optional<PShipooUser> maybeUser = (Optional<PShipooUser>) outcome.getReplies().get(0);
        assertTrue(maybeUser.isPresent());

        PShipooUser outputUser = maybeUser.get();
        assertEquals(user, outputUser);
        assertEquals(username, outputUser.getUsername().get());
        assertEquals(email,  outputUser.getEmail());

        assertEquals(Collections.emptyList(), driver.getAllIssues());
    }

    @Test
    public void testGetNotCreated() {

        PersistentEntityTestDriver.Outcome<PShipooUserEvent, Optional<PShipooUser>> outcome = driver.run(
                GetPShipooUser.INSTANCE);

        Optional<PShipooUser> maybeUser = (Optional<PShipooUser>) outcome.getReplies().get(0);
        assertFalse(maybeUser.isPresent());
        assertEquals(Collections.emptyList(), driver.getAllIssues());

    }
}