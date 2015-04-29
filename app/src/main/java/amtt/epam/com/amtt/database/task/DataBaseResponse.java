package amtt.epam.com.amtt.database.task;

/**
 * Created by Artsiom_Kaliaha on 29.04.2015.
 */
public class DataBaseResponse<ResultType> {

    public DataBaseTaskResult mTaskResult;
    public ResultType mValueResult;

    public void setTaskResult(DataBaseTaskResult taskResult) {
        mTaskResult = taskResult;
    }

    public void setValueResult(ResultType valueResult) {
        mValueResult = valueResult;
    }

    public DataBaseTaskResult getTaskResult() {
        return mTaskResult;
    }

    public ResultType getValueResult() {
        return mValueResult;
    }

}
