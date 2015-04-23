package amtt.epam.com.amtt.api;

import java.net.HttpURLConnection;

/**
 * Created by Artsiom_Kaliaha on 15.04.2015.
 */
public interface JiraApiConst {
    //TODO use static final class for constants
    //Paths
    String BASE_PATH = "/rest/auth/latest/";
    String LOGIN_PATH = BASE_PATH + "session";
    String ISSUE_PATH = "/rest/api/2/issue/";
    String USER_PROJECTS_PATH = "/rest/api/2/issue/createmeta";
    String USER_INFO_PATH = "/rest/api/2/user?username=";
    String EXPAND_GROUPS = "&expand=groups";

    //Header keys
    String AUTH = "Authorization";
    String CONTENT_TYPE = "content-type";

    //Header values
    String APPLICATION_JSON = "application/json";
    String BASIC_AUTH = "Basic ";

    //TODO We have HttpStatus and HttpURLConnection with response codes constants
    //Response codes
    int BAD_GATE_WAY = 502;

}
