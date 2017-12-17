package com.shipoo.tenant.impl;

import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver;
import org.junit.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.shipoo.tenant.impl.ShipooTenantException.USER_CANT_PUT_MEMBER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PShipooTenantEntityTest {

    private static ActorSystem system;
    private PersistentEntityTestDriver<PShipooTenantCommand, PShipooTenantEvent, Optional<PShipooTenantState>> driver;

    private final UUID id = UUID.randomUUID();
    private final UUID creator = UUID.randomUUID();
    private final ShipooTenantUserData tenantData = ShipooTenantUserData.builder().
            name("My startup Tenant")
            .build();

    @BeforeClass
    public static void startActorSystem() {
        system = ActorSystem.create("PShipooUserEntityTest");
    }

    @AfterClass
    public static void shutdownActorSystem() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Before
    public void createTestDriver() {
        driver = new PersistentEntityTestDriver<>(system, new PShipooTenantEntity(), id.toString());
    }

    @After
    public void noIssues() {
        if (!driver.getAllIssues().isEmpty()) {
            driver.getAllIssues().forEach(System.out::println);
            fail("There were issues " + driver.getAllIssues().get(0));
        }
    }

    private PShipooTenantEvent.TenantCreated createTenant() {
        PersistentEntityTestDriver.Outcome<PShipooTenantEvent, Optional<PShipooTenantState>> outcome = driver.run(
                PShipooTenantCommand.CreateTenant.builder()
                        .creator(creator)
                        .tenantData(tenantData)
                        .id(id)
                        .build()
        );

        return (PShipooTenantEvent.TenantCreated) outcome.events().get(0);
    }

    @Test
    public void testCreateTenant() {
        PShipooTenantEvent.TenantCreated event = createTenant();

        assertEquals(tenantData, event.getTenant().getTenantData());

        assertEquals(Collections.emptyList(), driver.getAllIssues());
    }

    @Test
    public void testPutMember() {

        PShipooTenantEvent.TenantCreated event = createTenant();

        UUID commander = event.getTenant().getCreator();
        PersistentEntityTestDriver.Outcome<PShipooTenantEvent, Optional<PShipooTenantState>> outcome =
                createDelivererMember(commander);

        assertTrue(outcome.getReplies().size() == 1);

        Optional<CommandReply.Done> replay = (Optional<CommandReply.Done>) outcome.getReplies().get(0);
        assert replay.isPresent();

        assertEquals(0L, replay.get().getCode().longValue());

    }

    @Test
    public void testPutMemberFail() {

        PShipooTenantEvent.TenantCreated event = createTenant();

        UUID commander = event.getTenant().getCreator();
        PersistentEntityTestDriver.Outcome<PShipooTenantEvent, Optional<PShipooTenantState>> outcome =
                createDelivererMember(commander);

        assertTrue(outcome.getReplies().size() == 1);
        Optional<CommandReply.Done> replay = (Optional<CommandReply.Done>) outcome.getReplies().get(0);
        assert replay.isPresent();

        assertEquals(0L, replay.get().getCode().longValue());

        PShipooTenantEvent.TenantMemberPutted putted = (PShipooTenantEvent.TenantMemberPutted)
                outcome.events().get(0);


        UUID deliverer = putted.getMemberPutted().getUser();

        outcome = createDelivererMember(deliverer);
        assertTrue(outcome.getReplies().size() == 1);
        assertEquals(USER_CANT_PUT_MEMBER, outcome.getReplies().get(0));

    }

    private PersistentEntityTestDriver.Outcome<PShipooTenantEvent, Optional<PShipooTenantState>> createDelivererMember(UUID commander) {
        UUID member = UUID.randomUUID();
        PShipooTenantCommand.PutTenantMember cmd = PShipooTenantCommand.PutTenantMember.builder()
                .commander(commander)
                .member(member)
                .role(ShipooTenantRole.DELIVERER)
                .build();

        return driver.run(cmd);
    }

}