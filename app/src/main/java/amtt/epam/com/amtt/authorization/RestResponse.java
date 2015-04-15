package amtt.epam.com.amtt.authorization;

import org.apache.http.HttpEntity;

/**
 * Created by Artsiom_Kaliaha on 15.04.2015.
 */
public class RestResponse<ResultType> {

    private String mResponseMessage;
    private int mStatusCode;
    private HttpEntity mEntity;
    private ResultType mResult;

    public RestResponse() {
    }

    public void setMessage(String responseMessage) {
        mResponseMessage = responseMessage;
    }

    public void setMessage(Exception e) {
        mResponseMessage = e.getMessage();
    }

    public void setStatusCode(int statusCode) {
        mStatusCode = statusCode;
    }

    public void setEntity(HttpEntity httpEntity) {
        mEntity = httpEntity;
    }

    public void setResult(ResultType result) {
        mResult = result;
    }

    public String getMessage() {
        return mResponseMessage;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    public HttpEntity getEntity() {
        return mEntity;
    }

    public ResultType getResult() {
        return mResult;
    }

}
