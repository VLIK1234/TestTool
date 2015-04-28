package amtt.epam.com.amtt.api.rest;

import org.apache.http.HttpEntity;

/**
 * Class providing access to REST method responses
 * Created by Artsiom_Kaliaha on 15.04.2015.
 */
public class RestResponse<ResultType,ResultObjectType> {

    private String mResponseMessage;
    private int mStatusCode;
    private HttpEntity mEntity;
    private ResultType mResult;
    private ResultObjectType mObject;

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

    public void setResultObject(ResultObjectType object) {
        mObject = object;
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

    public ResultObjectType getResultObject() {
        return mObject;
    }

}
