package com.shipoo.tenant.impl;

import akka.actor.ActorSystem;
import akka.actor.ExtendedActorSystem;
import akka.testkit.JavaTestKit;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.lightbend.lagom.internal.jackson.JacksonJsonSerializer;
import com.lightbend.lagom.javadsl.api.deser.SerializerFactory;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class SerializzationTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create("HelloWorldTest");
    }

    @AfterClass
    public static void teardown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    private final JacksonJsonSerializer serializer = new JacksonJsonSerializer((ExtendedActorSystem) system);

    @Test
    public void serializeTenantCreatedTest() throws IOException {

        ShipooTenantUserData tenant = ShipooTenantUserData.builder()
                .name("My Startup").build();

        PShipooTenantState state = PShipooTenantState.builder()
                .id(UUID.randomUUID())
                .creator(UUID.randomUUID())
                .build();


        PShipooTenantEvent.TenantCreated event = PShipooTenantEvent.TenantCreated.builder()
                .id(UUID.randomUUID())
                .timestamp(Instant.now())
                .tenant(state)
                .build();

        byte[] blob = serializer.toBinary(event);

        System.out.println(new String(blob));

        PShipooTenantEvent.TenantCreated eventDes =
                (PShipooTenantEvent.TenantCreated) serializer.fromBinary(blob, serializer.manifest(event));
        assertEquals(event, eventDes);
    }
}
