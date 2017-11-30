package com.shipoo.ui.controllers;

import org.pac4j.core.config.Config;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.engine.DefaultCallbackLogic;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.play.PlayWebContext;
import play.api.mvc.Result;

public class CookieCallbackLogic extends DefaultCallbackLogic<Result, PlayWebContext> {

    @Override
    protected void saveUserProfile(PlayWebContext context, Config config,
                                   CommonProfile profile, boolean multiProfile,
                                   boolean renewSession) {
        Utils.saveProfileToCookie(context, profile);
    }
}
