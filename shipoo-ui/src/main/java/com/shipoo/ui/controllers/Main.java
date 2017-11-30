package com.shipoo.ui.controllers;

import com.example.hello.api.ShipooService;
import com.shipoo.ui.model.AbstractShipooUiUser;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

//@With(GetShipooUiUserAction.class)
public class Main extends Controller {

    private final ShipooService shipooService;

    private PlaySessionStore playSessionStore;

    private HttpExecutionContext ec;

    @Inject
    public Main(ShipooService shipooService, PlaySessionStore playSessionStore, HttpExecutionContext ec) {
        this.shipooService = shipooService;
        this.playSessionStore = playSessionStore;
        this.ec = ec;
    }

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    @Secure(clients = "CookieClient")
    public CompletionStage<Result> index(String path) {
        AbstractShipooUiUser user = getUser();
        return shipooService.hello(user.firstName()).invoke().thenApplyAsync( m ->
                    ok(views.html.index.render(m, user)
                ), ec.current());
    }

    @Secure(clients = "AnonymousClient")
    public CompletionStage<Result> anonymous() {
        AbstractShipooUiUser user = getUser();
        return CompletableFuture.completedFuture(ok(views.html.anonymous.render(user)));
    }

    @Secure(clients = "OidcClient,CookieClient,")
    public CompletionStage<Result> oidcLogin() {
        return CompletableFuture.completedFuture(redirect(routes.Main.index("")));
    }

    protected AbstractShipooUiUser getUser() {
        CommonProfile profile = profile();
        AbstractShipooUiUser user = AbstractShipooUiUser.fromCommonProfile(profile);
        return user;
    }

    protected CommonProfile profile() {
        final PlayWebContext context = new PlayWebContext(ctx(), playSessionStore);
        final ProfileManager<CommonProfile> profileManager = new ProfileManager(context);
        List<CommonProfile> profiles = profileManager.getAll(false);
        return profiles.get(0);
    }

}
