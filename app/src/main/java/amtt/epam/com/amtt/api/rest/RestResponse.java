package amtt.epam.com.amtt.api.rest;

/**
 * Class providing access to REST method responses
 * Created by Artsiom_Kaliaha on 15.04.2015.
 */
public class RestResponse<ResultType, ResultObjectType> {

    private String mResponseMessage;
    private ResultType mResult;
    private ResultObjectType mObject;
    private Exception mException;

    public RestResponse() {
    }

    public void setMessage(String responseMessage) {
        mResponseMessage = responseMessage;
    }

    public void setMessage(Exception e) {
        mResponseMessage = e.getMessage();
    }

    public void setResult(ResultType result) {
        mResult = result;
    }

    public void setResultObject(ResultObjectType object) {
        mObject = object;
    }

    public void setException(Exception e) {
        mException = e;
    }


    public String getMessage() {
        return mResponseMessage;
    }

    public ResultType getResult() {
        return mResult;
    }

    public ResultObjectType getResultObject() {
        return mObject;
    }


    public Exception getException() {
        return mException;
    }

}
