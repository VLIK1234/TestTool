package amtt.epam.com.amtt.api.exception;

/**
 * Created by Artsiom_Kaliaha on 28.04.2015.
 */
public enum ExceptionType {

    AUTH("Check your credentials", null, null),
    AUTH_FORBIDDEN("You are not allowed to login now", null, null),
    NO_INTERNET("You are offline", "Try again", "Open settings"),
    UNKNOWN("Unknown error", null, null),
    BAD_GATEWAY("Gateway error", null, null),
    NOT_FOUND("Wrong web-address", null, null);

    private String mMessage;
    private String mPositiveText;
    private String mNeutralText;

    private ExceptionType(String message, String positiveText, String neutralText) {
        mMessage = message;
        mPositiveText = positiveText;
        mNeutralText = neutralText;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getPositiveText() {
        return mPositiveText;
    }

    public String getNeutralText() {
        return mNeutralText;
    }

}
