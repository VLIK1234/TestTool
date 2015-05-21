package amtt.epam.com.amtt.api.exception;

import amtt.epam.com.amtt.api.rest.RestMethod;

/**
 * Class encapsulate thrown exceptions and bad results code if such ones have been received, rest method for repeated request
 * Created by Artsiom_Kaliaha on 27.04.2015.
 */
public class AmttException extends Exception {

    private Exception mSuppressedException;
    private final int mStatusCode;
    private final RestMethod mRestMethod;
    private String mEntity; //field isn't used at the time, but resides here for future exception handling logic complication

    public AmttException(Exception suppressedException, int resultCode, RestMethod restMethod, String entityString) {
        mSuppressedException = suppressedException;
        mStatusCode = resultCode;
        mRestMethod = restMethod;
        mEntity = entityString;
    }

    public AmttException(Exception suppressedException, int resultCode, RestMethod restMethod) {
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


    public void setEntity(String entityString) {
        mEntity = entityString;
    }

    public void replaceSuppressedOne(Exception e) {
        mSuppressedException = e;
    }

}
