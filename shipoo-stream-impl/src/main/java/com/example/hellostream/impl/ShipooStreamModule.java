/*
 * 
 */
package com.example.hellostream.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.example.hello.api.ShipooService;
import com.example.hellostream.api.ShipooStreamService;

/**
 * The module that binds the ShipooStreamService so that it can be served.
 */
public class ShipooStreamModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    // Bind the ShipooStreamService service
    bindService(ShipooStreamService.class, ShipooStreamServiceImpl.class);
    // Bind the ShipooService client
    bindClient(ShipooService.class);
    // Bind the subscriber eagerly to ensure it starts up
    bind(ShipooStreamSubscriber.class).asEagerSingleton();
  }
}
