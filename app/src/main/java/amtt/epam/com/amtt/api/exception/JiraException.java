package amtt.epam.com.amtt.api.exception;

import amtt.epam.com.amtt.api.rest.RestMethod;

/**
 * Created by Artsiom_Kaliaha on 27.04.2015.
 */
public class JiraException extends Exception {

    private final Exception mSuppressedException;
    private final int mStatusCode;
    private final RestMethod mRestMethod;

    public JiraException(Exception suppressedException, int resultCode, RestMethod restMethod) {
        mSuppressedException = suppressedException;
        mStatusCode = resultCode;
        mRestMethod = restMethod;
    }


    public Exception getSuppressedOne() {
        return mSuppressedException;
    }



    public int getStatusCode() {
        return mStatusCode;
    }

    public RestMethod getRestMethod() {
        return mRestMethod;
    }

}
