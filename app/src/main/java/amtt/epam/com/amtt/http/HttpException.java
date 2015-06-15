package amtt.epam.com.amtt.http;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 * Special Exception type containing the most important information about failed HttpRequest or HttpRequest with inappropriate result
 */
public class HttpException extends org.apache.http.HttpException {

    private final Exception mSuppressedException;
    private final int mResultCode;
    private final Request mRequest;
    private final String mEntityString;

    public HttpException(Exception suppressedException, int resultCode, Request request, String entityString) {
        mSuppressedException = suppressedException;
        mResultCode = resultCode;
        mRequest = request;
        mEntityString = entityString;
    }

    public Exception getSuppressedException() {
        return mSuppressedException;
    }

    public int getStatusCode() {
        return mResultCode;
    }

    public Request getRequest() {
        return mRequest;
    }

    public String getEntityString() {
        return mEntityString;
    }

}
