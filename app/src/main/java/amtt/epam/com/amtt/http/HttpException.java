package amtt.epam.com.amtt.http;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 * Special Exception type containing the most important information about failed HttpRequest or HttpRequest with inappropriate result
 */
public class HttpException extends org.apache.http.HttpException {

    private final int mResultCode;

    public HttpException(int resultCode) {
        mResultCode = resultCode;
    }

    public int getStatusCode() {
        return mResultCode;
    }

}
