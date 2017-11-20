package com.shipoo.ui.controllers;

import com.example.hello.api.ShipooService;
import org.pac4j.play.java.Secure;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class Main extends Controller {

    private final ShipooService shipooService;

    @Inject
    public Main(ShipooService shipooService) {
        this.shipooService = shipooService;
    }



    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public CompletionStage<Result> index() {

        return shipooService.hello("Dear User").invoke().thenApply( m ->
            ok(views.html.index.render(m))
        );
    }

    @Secure(clients = "OidcClient")
    public CompletionStage<Result> app() {

        return shipooService.hello("Dear User").invoke().thenApply( m ->
                ok(views.html.app.render(m))
        );
    }

}
