package com.shipoo.ui;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.client.ServiceClientGuiceSupport;
import com.typesafe.config.Config;

public class ShipooModule extends AbstractModule implements ServiceClientGuiceSupport {

    @Override
    protected void configure() {

        // TODO: remove this and the ConfigProvider when upgrading to Lagom 1.4.0
        binder().bind(Config.class).toProvider(ConfigProvider.class);

    }

}
