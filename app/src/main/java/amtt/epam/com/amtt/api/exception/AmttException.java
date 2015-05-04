package amtt.epam.com.amtt.api.exception;

import org.apache.http.HttpEntity;

import amtt.epam.com.amtt.api.rest.RestMethod;

/**
 * Class encapsulate thrown exceptions and bad results code if such ones have been received, rest method for repeated request
 * Created by Artsiom_Kaliaha on 27.04.2015.
 */
public class AmttException extends Exception {

    private final Exception mSuppressedException;
    private final int mStatusCode;
    private final RestMethod mRestMethod;
    //TODO It's not a good idea to put Entity or response here. Let it be just a converted String
    private final HttpEntity mEntity; //field isn't used at the time, but resides here for future exception handling logic complication

    public AmttException(Exception suppressedException, int resultCode, RestMethod restMethod, HttpEntity httpEntity) {
        mSuppressedException = suppressedException;
        mStatusCode = resultCode;
        mRestMethod = restMethod;
        mEntity = httpEntity;
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
