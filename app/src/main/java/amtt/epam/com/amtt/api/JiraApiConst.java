package amtt.epam.com.amtt.api;

/**
 * Created by Artsiom_Kaliaha on 15.04.2015.
 */
public final class JiraApiConst {

    //Default values
    private static int DEFAULT_API_VERSION = 2;
    private static int DEFAULT_MAX_RESULTS = 10;

    //Base
    private static String API_VERSION_LATEST = "latest";
    private static String REST_PATH = "rest";
    private static String API_PATH = "api";
    private static String SLASH = "/";
    private static String REST_API_PATH = SLASH + REST_PATH + SLASH + API_PATH + SLASH + DEFAULT_API_VERSION + SLASH;
    public static String EPAM_JIRA_SUFFIX = SLASH + "jira";
    private static String QUESTION = "?";
    private static String EQUAL = "=";
    private static String EXPAND = "expand";
    private static String AND = "&";

    //Tags
    private static String ASSIGNABLE_TAG = "assignable";
    private static String AUTH_TAG = "auth";
    private static String CREATEMETA_TAG = "createmeta";
    private static String GROUPS_TAG = "groups";
    private static String ISSUE_TAG = "issue";
    private static String MAX_RESULTS_TAG = "maxResults";
    private static String MULTI_PROJECT_SEARCH_TAG = "multiProjectSearch";
    private static String PRIORITY_TAG = "priority";
    private static String PROJECT_TAG = "project";
    private static String PROJECT_KEYS_TAG = "projetKeys";
    private static String SESSION_TAG = "session";
    private static String USER_TAG = "user";
    private static String USER_NAME_TAG = "username";
    private static String VERSIONS_TAG = "versions";

    //Paths
    public static String LOGIN_PATH = SLASH + REST_PATH + SLASH + AUTH_TAG + SLASH + API_VERSION_LATEST + SLASH + SESSION_TAG;
    public static String ISSUE_PATH = REST_API_PATH + ISSUE_TAG + SLASH;
    public static String USER_PROJECTS_PATH = ISSUE_PATH + CREATEMETA_TAG;
    public static String USER_PATH = REST_API_PATH + USER_TAG;
    public static String USER_INFO_PATH = USER_PATH + QUESTION + USER_NAME_TAG + EQUAL;
    public static String USERS_ASSIGNABLE_PATH = USER_PATH + SLASH + ASSIGNABLE_TAG + SLASH + MULTI_PROJECT_SEARCH_TAG + QUESTION + PROJECT_KEYS_TAG + EQUAL;
    public static String USERS_ASSIGNABLE_PATH_UN = AND + USER_NAME_TAG + EQUAL;
    public static String USERS_ASSIGNABLE_PATH_MR = AND + MAX_RESULTS_TAG + EQUAL + DEFAULT_MAX_RESULTS;
    public static String PROJECT_VERSIONS_PATH = REST_API_PATH + PROJECT_TAG + SLASH;
    public static String PROJECT_VERSIONS_PATH_V = SLASH + VERSIONS_TAG;
    public static String PROJECT_PRIORITY_PATH = REST_API_PATH + PRIORITY_TAG;
    public static String EXPAND_GROUPS = AND + EXPAND + EQUAL + GROUPS_TAG;

    //Header keys
    public static String AUTH = "Authorization";
    public static String CONTENT_TYPE = "content-type";

    //Header values
    public static String APPLICATION_JSON = "application/json";
    public static String BASIC_AUTH = "Basic ";
}
