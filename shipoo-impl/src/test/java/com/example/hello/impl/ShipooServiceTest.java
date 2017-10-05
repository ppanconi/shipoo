/*
 * 
 */
package com.example.hello.impl;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.withServer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.example.hello.api.GreetingMessage;
import com.example.hello.api.ShipooService;

public class ShipooServiceTest {

  @Test
  public void shouldStorePersonalizedGreeting() throws Exception {
    withServer(defaultSetup().withCassandra(true), server -> {
      ShipooService service = server.client(ShipooService.class);

      String msg1 = service.hello("Alice").invoke().toCompletableFuture().get(5, SECONDS);
      assertEquals("Hello, Alice!", msg1); // default greeting

      service.useGreeting("Alice").invoke(new GreetingMessage("Hi")).toCompletableFuture().get(5, SECONDS);
      String msg2 = service.hello("Alice").invoke().toCompletableFuture().get(5, SECONDS);
      assertEquals("Hi, Alice!", msg2);

      String msg3 = service.hello("Bob").invoke().toCompletableFuture().get(5, SECONDS);
      assertEquals("Hello, Bob!", msg3); // default greeting
    });
  }

}