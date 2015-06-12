package amtt.epam.com.amtt.api.http;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 * Special Exception type containing the most important information about failed HttpRequest or HttpRequest with inappropriate result
 */
public class HttpException extends org.apache.http.HttpException {

    private final Exception mSuppressedException;
    private final int mResultCode;
    private final HttpRequestBase mHttpRequest;
    private final String mEntityString;

    public HttpException(Exception suppressedException, int resultCode, HttpRequestBase httpRequest, String entityString) {
        mSuppressedException = suppressedException;
        mResultCode = resultCode;
        mHttpRequest = httpRequest;
        mEntityString = entityString;
    }

    public Exception getSuppressedException() {
        return mSuppressedException;
    }

    public int getResultCode() {
        return mResultCode;
    }

    public HttpRequestBase getHttpRequest() {
        return mHttpRequest;
    }

    public String getEntityString() {
        return mEntityString;
    }

}
