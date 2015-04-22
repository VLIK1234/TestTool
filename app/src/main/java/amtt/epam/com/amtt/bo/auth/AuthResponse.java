package amtt.epam.com.amtt.bo.auth;

/**
 * Created by Artsiom_Kaliaha on 10.04.2015.
 */
public class AuthResponse {

    public static final String USER_NAME = "name";
    public static final String LOGIN_COUNT = "loginCount";
    public static final String PREVIOUS_LOGIN_TIME = "previousLoginTime";
    public static final String LOGIN_INFO_OBJECT = "loginInfo";

    private final String mUserName;
    private final int mLoginCount;
    private final String mPreviousLoginTime;

    public AuthResponse(String userName, int loginCount, String previousLoginTime) {
        mUserName = userName;
        mLoginCount = loginCount;
        mPreviousLoginTime = previousLoginTime;
    }

    public String getPrettyResponse() {
        return "You have authorized as " + mUserName +
                ". Your login count is " + mLoginCount +
                ". Your previously login time is" + mPreviousLoginTime;
    }

}
