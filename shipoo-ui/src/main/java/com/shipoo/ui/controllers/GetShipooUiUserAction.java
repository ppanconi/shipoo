package com.shipoo.ui.controllers;

import com.shipoo.ui.model.AbstractShipooUiUser;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.store.PlaySessionStore;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static play.mvc.Controller.ctx;

public class GetShipooUiUserAction extends Action.Simple  {

    private PlaySessionStore playSessionStore;

    @Inject
    public GetShipooUiUserAction(PlaySessionStore playSessionStore) {
        super();
        this.playSessionStore = playSessionStore;
    }

    @Override
    public CompletionStage<Result> call(Http.Context ctx) {

        AbstractShipooUiUser user = getUser();
        ctx.args.put("user", user);
        return delegate.call(ctx);
    }

    public AbstractShipooUiUser getUser() {
        CommonProfile profile = profile();
        if (profile != null) {
            AbstractShipooUiUser user = AbstractShipooUiUser.fromCommonProfile(profile);
            return user;
        } else return null;
    }

    public CommonProfile profile() {
        final PlayWebContext context = new PlayWebContext(ctx(), playSessionStore);
        final ProfileManager<CommonProfile> profileManager = new ProfileManager(context);
        List<CommonProfile> profiles = profileManager.getAll(false);
        if (profiles.size() == 0){
            return null;
        } else {
            return profiles.get(0);
        }
    }
}
