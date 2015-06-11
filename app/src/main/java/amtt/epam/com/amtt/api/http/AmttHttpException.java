package amtt.epam.com.amtt.api.http;

import org.apache.http.HttpException;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 */
public abstract class AmttHttpException extends HttpException {

    private Exception mCausedException;
    private int mResultCode;
    private HttpRequestBase mHttpRequest;
    private String mEntityString;

    public AmttHttpException(Exception suppressedException, int resultCode, HttpRequestBase mHttpRequest, String entityString) {

    }

    public Exception getCausedException() {
        return mCausedException;
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
