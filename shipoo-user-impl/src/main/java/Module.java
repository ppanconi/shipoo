import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.shipoo.user.ShipooUserService;
import com.shipoo.user.impl.ShipooUserServiceImpl;

public class Module extends AbstractModule implements ServiceGuiceSupport {

    @Override
    protected void configure() {
        bindService(ShipooUserService.class, ShipooUserServiceImpl.class);
    }
}
