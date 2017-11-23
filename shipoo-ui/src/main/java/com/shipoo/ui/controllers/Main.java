package com.shipoo.ui.controllers;

import com.example.hello.api.ShipooService;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.mvc.*;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@With(Main.Profile.class)
public class Main extends Controller {

    private final ShipooService shipooService;

    private PlaySessionStore playSessionStore;

    @Inject
    public Main(ShipooService shipooService, PlaySessionStore playSessionStore) {
        this.shipooService = shipooService;
        this.playSessionStore = playSessionStore;
    }

    public class Profile extends Action.Simple {
        @Override
        public CompletionStage<Result> call(Http.Context ctx) {
            ctx.args.put("profile", profile());
            return delegate.call(ctx);
        }

        public CommonProfile profile() {
            final PlayWebContext context = new PlayWebContext(ctx(), playSessionStore);
            final ProfileManager<CommonProfile> profileManager = new ProfileManager(context);
            return profileManager.getAll(false).get(0);
        }
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    @Secure()
    public CompletionStage<Result> index(String path) {

        return shipooService.hello("Dear User").invoke().thenApply( m ->
            ok(views.html.index.render(m))
        );
    }

    @Secure(clients = "AnonymousClient")
    public CompletionStage<Result> anonymous() {
        return CompletableFuture.completedFuture(ok(views.html.anonymous.render()));
    }

//    @Secure(clients = "OidcClient")
//    public CompletionStage<Result> app() {
//
//        return shipooService.hello("Dear User").invoke().thenApply( m ->
//                ok(views.html.app.render(m))
//        );
//    }

}
