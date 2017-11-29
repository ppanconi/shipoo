package views;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shipoo.ui.model.AbstractShipooUiUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static play.mvc.Controller.ctx;

public class Utils {

    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static String jsonUser() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user());
//        logger.info(json);
        return json;
    }

    public static AbstractShipooUiUser user() {
        return (AbstractShipooUiUser) ctx().args.get("user");
    }

    public static boolean isAnonymous() {
        return user().id().equals("anonymous");
    }

}
