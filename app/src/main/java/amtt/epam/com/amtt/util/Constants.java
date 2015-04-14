package amtt.epam.com.amtt.util;

/**
 * Created by Irina Monchenko on 07.04.2015.
 */
public interface Constants {

    public interface SharedPreferenceKeys {
        String USER_NAME = "username";
        String URL = "url";
        String VOID = "";
        String PROJECTS_NAMES = "projectsNames";
        String PROJECTS_KEYS = "projectsKeys";
        String ACCESS = "access";
        String COLON = ":";
        String CREDENTIALS = "credentials";

    }

    public interface UrlKeys {//todo move to jiraapi

        String BASIC_AUTH = "Basic ";
        String BASE_PATH = "/rest/auth/latest/";
        String LOGIN_METHOD = BASE_PATH + "session";
        String AUTH_HEADER = "Authorization";
        String ISSUE_PATH = "/rest/api/2/issue/";
        String USER_PROJECTS_PATH = "/rest/api/2/issue/createmeta";
        String USER_INFO_PATH = "/rest/api/2/user?username=";
        String EXPAND_GROUPS = "&expand=groups";
        String CONTENT_TYPE = "content-type";
        String APPLICATION_JSON = "application/json";
    }

    public interface DialogKeys {
        String INPUT_USERNAME = "Input username";
        String INPUT_PASSWORD = "Input password";
        String INPUT_URL = "Input URL";
        String NEW_LINE = "\n";
    }


}
