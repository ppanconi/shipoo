package com.shipoo.ui.controllers;

import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.store.PlaySessionStore;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static play.mvc.Controller.ctx;

public class GetProfileAction extends Action.Simple  {

    private PlaySessionStore playSessionStore;

    @Inject
    public GetProfileAction(PlaySessionStore playSessionStore) {
        super();
        this.playSessionStore = playSessionStore;
    }

    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        ctx.args.put("profile", profile());
        return delegate.call(ctx);
    }

    public CommonProfile profile() {
        final PlayWebContext context = new PlayWebContext(ctx(), playSessionStore);
        final ProfileManager<CommonProfile> profileManager = new ProfileManager(context);
        return profileManager.getAll(true).get(0);
    }
}
