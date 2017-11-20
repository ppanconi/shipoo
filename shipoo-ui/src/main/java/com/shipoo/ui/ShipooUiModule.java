package com.shipoo.ui;

import com.example.hello.api.ShipooService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.api.ServiceAcl;
import com.lightbend.lagom.javadsl.api.ServiceInfo;
import com.lightbend.lagom.javadsl.client.ServiceClientGuiceSupport;
import com.typesafe.config.Config;

public class ShipooUiModule extends AbstractModule implements ServiceClientGuiceSupport {

    @Override
    protected void configure() {
        bindServiceInfo(ServiceInfo.of("shipoo-ui", ServiceAcl.path("(?!/api/).*")));
//        // TODO: remove this and the ConfigProvider when upgrading to Lagom 1.4.0
        binder().bind(Config.class).toProvider(ConfigProvider.class);
        bindClient(ShipooService.class);
    }

}
