package com.example.hello.impl;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver.Outcome;

import akka.Done;
import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.example.hello.impl.ShipooCommand.Hello;
import com.example.hello.impl.ShipooCommand.UseGreetingMessage;
import com.example.hello.impl.ShipooEvent.GreetingMessageChanged;

public class ShipooEntityTest {

  static ActorSystem system;

  @BeforeClass
  public static void setup() {
    system = ActorSystem.create("ShipooEntityTest");
  }

  @AfterClass
  public static void teardown() {
    JavaTestKit.shutdownActorSystem(system);
    system = null;
  }

  @Test
  public void testShipooEntity() {
    PersistentEntityTestDriver<ShipooCommand, ShipooEvent, ShipooState> driver = new PersistentEntityTestDriver<>(system,
        new ShipooEntity(), "world-1");

    Outcome<ShipooEvent, ShipooState> outcome1 = driver.run(new Hello("Alice"));
    assertEquals("Hello, Alice!", outcome1.getReplies().get(0));
    assertEquals(Collections.emptyList(), outcome1.issues());

    Outcome<ShipooEvent, ShipooState> outcome2 = driver.run(new UseGreetingMessage("Hi"),
        new Hello("Bob"));
    assertEquals(1, outcome2.events().size());
    assertEquals(new GreetingMessageChanged("world-1", "Hi"), outcome2.events().get(0));
    assertEquals("Hi", outcome2.state().message);
    assertEquals(Done.getInstance(), outcome2.getReplies().get(0));
    assertEquals("Hi, Bob!", outcome2.getReplies().get(1));
    assertEquals(2, outcome2.getReplies().size());
    assertEquals(Collections.emptyList(), outcome2.issues());
  }

}
