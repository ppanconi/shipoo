package com.shipoo.ui.controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class TenantController extends Controller {

    public CompletionStage<Result> create() {
        JsonNode json = request().body().asJson();
        System.out.println(json);

        return CompletableFuture.completedFuture(ok(Json.parse("{\"status\": \"OK\"}")));
    }
}
