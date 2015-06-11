package amtt.epam.com.amtt.api.http;

import amtt.epam.com.amtt.api.result.JiraOperationResult;

/**
 * Class providing access to REST method responses
 * Created by Artsiom_Kaliaha on 15.04.2015.
 */
public class HttpResult<ResultType> {

    private JiraOperationResult mResult;
    private ResultType mObject;

    public HttpResult() {
    }

    public void setOperationResult(JiraOperationResult result) {
        if (mResult == null) {
            mResult = result;
        }
    }

    public void setResultObject(ResultType object) {
        mObject = object;
    }

    public JiraOperationResult getOpeartionResult() {
        return mResult;
    }

    public ResultType getResultObject() {
        return mObject;
    }

}
