package com.shipoo.ui;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.http.DefaultHttpActionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Result;

import static play.mvc.Results.redirect;

public class ShipooHttpActionAdapter extends DefaultHttpActionAdapter {

    private static Logger logger = LoggerFactory.getLogger(ShipooHttpActionAdapter.class);

    @Override
    public Result adapt(int code, PlayWebContext context) {
        if (code == HttpConstants.UNAUTHORIZED) {
//            return unauthorized(views.html.error401.render().toString()).as((HttpConstants.HTML_CONTENT_TYPE));
            logger.info("UNAUTHORIZED redirect to anonymous.html");
            return redirect("/anonymous.html");
        } else if (code == HttpConstants.FORBIDDEN) {
//            return forbidden(views.html.error403.render().toString()).as((HttpConstants.HTML_CONTENT_TYPE));
            logger.info("FORBIDDEN   default block");
            return super.adapt(code, context);
        } else {
            return super.adapt(code, context);
        }
    }
}
