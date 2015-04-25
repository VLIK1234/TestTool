package amtt.epam.com.amtt.api.rest;

import amtt.epam.com.amtt.api.result.JiraOperationResult;

/**
 * Class providing access to REST method responses
 * Created by Artsiom_Kaliaha on 15.04.2015.
 */
public class RestResponse<OutputType> {

    private String mResponseMessage;
    private JiraOperationResult mResult;
    private OutputType mObject;

    public RestResponse() {
    }

    public void setMessage(Exception e) {
        mResponseMessage = e.getMessage();
    }

    public void setOperationResult(JiraOperationResult result) {
        if (mResult == null) {
            mResult = result;
        }
    }

    public void setResultObject(OutputType object) {
        mObject = object;
    }

    public String getExceptionMessage() {
        return mResponseMessage;
    }

    public JiraOperationResult getOpeartionResult() {
        return mResult;
    }

    public OutputType getResultObject() {
        return mObject;
    }

}
