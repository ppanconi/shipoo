package com.shipoo.ui.controllers;

import org.pac4j.play.CallbackController;
import org.pac4j.play.PlayWebContext;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.pac4j.core.util.CommonHelper.assertNotNull;

public class CookieCallbackController extends CallbackController {

    private CookieCallbackLogic callbackLogic = new CookieCallbackLogic();

    @Override
    public CompletionStage<Result> callback() {
        assertNotNull("callbackLogic", callbackLogic);
        assertNotNull("config", config);

        final PlayWebContext playWebContext = new PlayWebContext(ctx(), playSessionStore);
        return CompletableFuture.supplyAsync(() ->
                callbackLogic.perform(playWebContext, config, config.getHttpActionAdapter(),
                        "/", true, false), ec.current());
    }
}
