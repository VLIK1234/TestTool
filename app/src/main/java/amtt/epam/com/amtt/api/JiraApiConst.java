package amtt.epam.com.amtt.api;

/**
 @author Artsiom_Kaliaha
 @version on 15.04.2015
 */

public final class JiraApiConst {

    //Default values
    private static final int DEFAULT_API_VERSION = 2;
    private static final int DEFAULT_MAX_RESULTS = 10;

    //Base
    private static final String API_VERSION_LATEST = "latest";
    private static final String REST_PATH = "rest";
    private static final String API_PATH = "api";
    private static final String SLASH = "/";
    public static final String REST_API_PATH = SLASH + REST_PATH + SLASH + API_PATH + SLASH + DEFAULT_API_VERSION + SLASH;
    private static final String QUESTION = "?";
    private static final String EQUAL = "=";
    private static final String AND = "&";

    //Tags
    private static final String ASSIGNABLE_TAG = "assignable";
    private static final String ATTACHMENTS_TAG = "attachments";
    private static final String AUTH_TAG = "auth";
    private static final String CREATEMETA_TAG = "createmeta";
    private static final String COMPONENTS_TAG = "components";
    private static final String ISSUE_TAG = "issue";
    private static final String MAX_RESULTS_TAG = "maxResults";
    private static final String MULTI_PROJECT_SEARCH_TAG = "multiProjectSearch";
    private static final String PRIORITY_TAG = "priority";
    private static final String PROJECT_TAG = "project";
    private static final String PROJECT_KEYS_TAG = "projectKeys";
    private static final String SESSION_TAG = "session";
    private static final String USER_TAG = "user";
    private static final String USER_NAME_TAG = "username";
    private static final String VERSIONS_TAG = "versions";

    //Paths
    public static final String LOGIN_PATH = SLASH + REST_PATH + SLASH + AUTH_TAG + SLASH + API_VERSION_LATEST + SLASH + SESSION_TAG;
    public static final String ISSUE_PATH = REST_API_PATH + ISSUE_TAG + SLASH;
    public static final String USER_PROJECTS_PATH = ISSUE_PATH + CREATEMETA_TAG;
    public static final String USER_PATH = REST_API_PATH + USER_TAG;
    public static final String USER_INFO_PATH = USER_PATH + QUESTION + USER_NAME_TAG + EQUAL;
    public static final String USERS_ASSIGNABLE_PATH = USER_PATH + SLASH + ASSIGNABLE_TAG + SLASH + MULTI_PROJECT_SEARCH_TAG + QUESTION + PROJECT_KEYS_TAG + EQUAL;
    public static final String USERS_ASSIGNABLE_PATH_UN = AND + USER_NAME_TAG + EQUAL;
    public static final String USERS_ASSIGNABLE_PATH_MR = AND + MAX_RESULTS_TAG + EQUAL + DEFAULT_MAX_RESULTS;
    public static final String PROJECT_VERSIONS_PATH = REST_API_PATH + PROJECT_TAG + SLASH;
    public static final String PROJECT_VERSIONS_PATH_V = SLASH + VERSIONS_TAG;
    public static final String PROJECT_PRIORITY_PATH = REST_API_PATH + PRIORITY_TAG;
    public static final String ATTACHMENTS_PATH = SLASH + ATTACHMENTS_TAG;
    public static final String PROJECT_COMPONENTS_PATH = REST_API_PATH + PROJECT_TAG + SLASH;
    public static final String PROJECT_COMPONENTS_PATH_C = SLASH + COMPONENTS_TAG;

    //Header keys
    public static final String AUTH = "Authorization";
    public static final String CONTENT_TYPE = "content-type";
    public static final String ATLASSIAN_TOKEN = "X-Atlassian-Token";

    //Header values
    public static final String APPLICATION_JSON = "application/json";
    public static final String BASIC_AUTH = "Basic ";
    public static final String NO_CHECK = "nocheck";
}
