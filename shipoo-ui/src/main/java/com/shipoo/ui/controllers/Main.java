package com.shipoo.ui.controllers;

import com.example.hello.api.ShipooService;
import com.shipoo.ui.SecurityModule;
import com.shipoo.ui.model.AbstractShipooUiUser;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.java.Secure;
import org.pac4j.play.store.PlaySessionStore;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@With(GetShipooUiUserAction.class)
public class Main extends Controller {

    private final ShipooService shipooService;

    private PlaySessionStore playSessionStore;

    private GetShipooUiUserAction userAction;

    private HttpExecutionContext ec;

    @Inject
    public Main(ShipooService shipooService, PlaySessionStore playSessionStore,
                GetShipooUiUserAction userAction, HttpExecutionContext ec) {
        this.shipooService = shipooService;
        this.playSessionStore = playSessionStore;
        this.userAction = userAction;
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
        AbstractShipooUiUser user = userAction.getUser();
        return shipooService.hello(user.firstName()).invoke().thenApplyAsync( m ->
                    ok(views.html.index.render(m)
                ), ec.current());
    }

    @Secure(clients = "AnonymousClient")
    public CompletionStage<Result> anonymous() {
        return CompletableFuture.completedFuture(ok(views.html.anonymous.render()));
    }

    @Secure(clients = "OidcClient")
    public CompletionStage<Result> oidcLogin() {

        final PlayWebContext context = new PlayWebContext(ctx(), playSessionStore);
        CommonProfile profile = userAction.profile();

        profile.removeAttribute("sub");

        final JwtGenerator generator = new JwtGenerator(new SecretSignatureConfiguration(SecurityModule.JWT_SALT));
        String token = generator.generate(profile);

        Utils.saveProfileTokenCookie(context, token);
        return CompletableFuture.completedFuture(redirect(routes.Main.index("")));
    }

}
