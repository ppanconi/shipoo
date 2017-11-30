package com.shipoo.ui.controllers;

import org.pac4j.play.CallbackController;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public class CookieCallbackController extends CallbackController {

    private CookieCallbackLogic logic;

    @Override
    public CompletionStage<Result> callback() {
        return super.callback();
    }
}
