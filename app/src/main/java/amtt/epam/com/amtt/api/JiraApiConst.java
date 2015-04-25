package amtt.epam.com.amtt.api;

/**
 * Created by Artsiom_Kaliaha on 15.04.2015.
 */
public final class JiraApiConst {

    //Base
    private static int API_VERSION = 2;
    private static String API_VERSION_LATEST = "latest";
    private static String REST_PATH = "rest";
    private static String API_PATH = "api";
    private static String SLASH = "/";
    private static String REST_API_PATH = SLASH + REST_PATH + SLASH + API_PATH + SLASH + API_VERSION + SLASH;
    public static String EPAM_JIRA_SUFFIX = SLASH + "jira";

    //Paths
    public static String LOGIN_PATH = SLASH + REST_PATH + SLASH + "auth" + SLASH + API_VERSION_LATEST + SLASH + "session";
    public static String ISSUE_PATH = REST_API_PATH + "issue" + SLASH;
    public static String USER_PROJECTS_PATH = ISSUE_PATH + "createmeta";
    public static String USER_INFO_PATH = REST_API_PATH + "user?username=";
    public static String EXPAND_GROUPS = "&expand=groups";

    //Header keys
    public static String AUTH = "Authorization";
    public static String CONTENT_TYPE = "content-type";

    //Header values
    public static String APPLICATION_JSON = "application/json";
    public static String BASIC_AUTH = "Basic ";

}
