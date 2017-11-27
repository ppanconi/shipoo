package views;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static play.mvc.Controller.ctx;

public class Utils {

    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static String jsonProfile() throws JsonProcessingException {
        CommonProfile profile = profile();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(profile);
        logger.info(json);
        return json;
    }

    public static CommonProfile profile() {
        return ((CommonProfile) ctx().args.get("profile"));
    }

    public static boolean isAnonymous() {
        return profile().getId().equals("anonymous");
    }

}
