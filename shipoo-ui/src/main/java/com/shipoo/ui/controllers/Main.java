package com.shipoo.ui.controllers;

import com.example.hello.api.ShipooService;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@With(GetProfileAction.class)
public class Main extends Controller {

    private final ShipooService shipooService;

    private PlaySessionStore playSessionStore;

    private GetProfileAction getProfile;

    private HttpExecutionContext ec;

    @Inject
    public Main(ShipooService shipooService, PlaySessionStore playSessionStore,
                GetProfileAction getProfile, HttpExecutionContext ec) {
        this.shipooService = shipooService;
        this.playSessionStore = playSessionStore;
        this.getProfile = getProfile;
        this.ec = ec;
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    @Secure()
    public CompletionStage<Result> index(String path) {
        CommonProfile user = getProfile.profile();
        return shipooService.hello(user.getFirstName()).invoke().thenApplyAsync( m ->
                    ok(views.html.index.render(m)
                ), ec.current());
    }

    @Secure(clients = "AnonymousClient")
    public CompletionStage<Result> anonymous() {
        return CompletableFuture.completedFuture(ok(views.html.anonymous.render()));
    }

    @Secure(clients = "OidcClient")
    public CompletionStage<Result> oidcLogin() {


        return CompletableFuture.completedFuture(redirect(routes.Main.index("")));
    }

}
