package amtt.epam.com.amtt.api;

/**
 * Created by Artsiom_Kaliaha on 27.04.2015.
 */
public class JiraException extends Exception {

    private final Exception mSuppressedException;
    private final int mStatusCode;
    private final String mResponseErrorMessage;

    public  JiraException(Exception suppressedException, int resultCode, String responseErrorMessage) {
        mSuppressedException = suppressedException;
        mStatusCode = resultCode;
        mResponseErrorMessage = responseErrorMessage;
    }

    public Exception getSuppressedException() {
        return mSuppressedException;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    public String getResponseErrorMessage() {
        return mResponseErrorMessage;
    }

}
