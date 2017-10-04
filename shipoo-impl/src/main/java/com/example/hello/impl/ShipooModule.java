/*
 * 
 */
package com.example.hello.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.example.hello.api.ShipooService;

/**
 * The module that binds the ShipooService so that it can be served.
 */
public class ShipooModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindService(ShipooService.class, ShipooServiceImpl.class);
  }
}
