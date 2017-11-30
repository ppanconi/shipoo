package com.shipoo.ui.controllers;

import com.shipoo.ui.SecurityModule;
import org.pac4j.core.context.Cookie;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.play.PlayWebContext;

import static com.shipoo.ui.SecurityModule.COOKIE_NAME;

public class Utils {
    static public void saveProfileTokenCookie(PlayWebContext context, String token) {

        final Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(context.getServerName());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
//        cookie.isSecure() TODO use config to set true for produciotn
        cookie.setMaxAge(3600 * 12);

        context.addResponseCookie(cookie);
    }

    static public void saveProfileToCookie(PlayWebContext context, CommonProfile profile) {

        if (profile.getAttribute("sub") != null) {
            profile.removeAttribute("sub");
        }
        final JwtGenerator generator = new JwtGenerator(new SecretSignatureConfiguration(SecurityModule.JWT_SALT));
        String token = generator.generate(profile);

        saveProfileTokenCookie(context, token);
    }

}
