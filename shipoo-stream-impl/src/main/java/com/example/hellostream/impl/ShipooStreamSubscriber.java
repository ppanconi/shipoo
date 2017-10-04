/*
 * 
 */
package com.example.hellostream.impl;

import akka.Done;
import akka.stream.javadsl.Flow;
import com.example.hello.api.ShipooEvent;
import com.example.hello.api.ShipooService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * This subscribes to the ShipooService event stream.
 */
public class ShipooStreamSubscriber {

  @Inject
  public ShipooStreamSubscriber(ShipooService shipooService, ShipooStreamRepository repository) {
    // Create a subscriber
    shipooService.helloEvents().subscribe()
      // And subscribe to it with at least once processing semantics.
      .atLeastOnce(
        // Create a flow that emits a Done for each message it processes
        Flow.<ShipooEvent>create().mapAsync(1, event -> {

          if (event instanceof ShipooEvent.GreetingMessageChanged) {
            ShipooEvent.GreetingMessageChanged messageChanged = (ShipooEvent.GreetingMessageChanged) event;
            // Update the message
            return repository.updateMessage(messageChanged.getName(), messageChanged.getMessage());

          } else {
            // Ignore all other events
            return CompletableFuture.completedFuture(Done.getInstance());
          }
        })
      );

  }
}
