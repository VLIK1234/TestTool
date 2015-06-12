package amtt.epam.com.amtt.api.http;

/**
 * Created by Artsiom_Kaliaha on 15.04.2015.
 * Result of HttpRequest
 */
public class HttpResult<ResultType> {

    private final String mRequestType;
    private final ResultType mObject;

    public HttpResult(String requestType,ResultType object ) {
        mRequestType = requestType;
        mObject = object;
    }

    public String getRequestType() {
        return mRequestType;
    }

    public ResultType getResultObject() {
        return mObject;
    }

}
