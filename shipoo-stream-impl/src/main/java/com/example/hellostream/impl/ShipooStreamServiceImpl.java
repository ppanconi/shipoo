/*
 * 
 */
package com.example.hellostream.impl;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.example.hello.api.ShipooService;
import com.example.hellostream.api.ShipooStreamService;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Implementation of the ShipooStreamService.
 */
public class ShipooStreamServiceImpl implements ShipooStreamService {

  private final ShipooService shipooService;
  private final ShipooStreamRepository repository;

  @Inject
  public ShipooStreamServiceImpl(ShipooService shipooService, ShipooStreamRepository repository) {
    this.shipooService = shipooService;
    this.repository = repository;
  }

  @Override
  public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> directStream() {
    return hellos -> completedFuture(
        hellos.mapAsync(8, name -> shipooService.hello(name).invoke()));
  }

  @Override
  public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> autonomousStream() {
    return hellos -> completedFuture(
        hellos.mapAsync(8, name -> repository.getMessage(name).thenApply( message ->
            String.format("%s, %s!", message.orElse("Hello"), name)
        ))
    );
  }
}
