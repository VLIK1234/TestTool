package amtt.epam.com.amtt.database.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.dao.DaoFactory;
import amtt.epam.com.amtt.database.dao.StepDao;
import amtt.epam.com.amtt.database.table.UsersTable;

/**
 * Created by Artsiom_Kaliaha on 26.03.2015.
 */
@SuppressWarnings("unchecked")
public class DataBaseTask<ResultType> extends AsyncTask<Void, Void, DataBaseTask.DataBaseResponse<ResultType>> {

    public static class DataBaseResponse<ResultType> {

        public DataBaseTaskResult mTaskResult;
        public Exception mException;
        public ResultType mResult;

        public DataBaseResponse(ResultType result, Exception e, DataBaseTaskResult taskResult) {
            mResult = result;
            mException = e;
            mTaskResult = taskResult;
        }

        public DataBaseTaskResult getTaskResult() {
            return mTaskResult;
        }

        public ResultType getValueResult() {
            return mResult;
        }

        public Exception getException() {
            return mException;
        }

    }

    public enum DataBaseOperationType {

        CLEAR,
        SAVE_STEP,
        CHECK_USERS_AVAILABILITY

    }

    public enum DataBaseTaskResult {

        DONE,
        ERROR,
        CLEARED

    }

    public static class Builder<ResultType> {

        private DataBaseOperationType mOperationType;
        private Context mContext;
        private DataBaseCallback<ResultType> mCallback;
        private Step mStep;
        private String mUserName;

        public Builder() {
        }

        public Builder setOperationType(DataBaseOperationType operationType) {
            mOperationType = operationType;
            return this;
        }

        public Builder setContext(@NonNull Context context) {
            mContext = context;
            return this;
        }

        public Builder setCallback(@NonNull DataBaseCallback callback) {
            mCallback = callback;
            return this;
        }

        public Builder setStep(Step step) {
            mStep = step;
            return this;
        }

        public Builder setUserName(String userName) {
            mUserName = userName;
            return this;
        }

        public void createAndExecute() {
            DataBaseTask<ResultType> dataBaseTask = new DataBaseTask<>();
            dataBaseTask.mOperationType = this.mOperationType;
            dataBaseTask.mContext = this.mContext;
            dataBaseTask.mCallback = this.mCallback;
            dataBaseTask.mStep = this.mStep;
            dataBaseTask.mUserName = this.mUserName;
            dataBaseTask.execute();
        }

    }

    private Step mStep;
    private String mUserName;
    private DataBaseOperationType mOperationType;
    private Context mContext;
    private DataBaseCallback<ResultType> mCallback;
    private Exception mException;

    @Override
    protected DataBaseResponse<ResultType> doInBackground(Void... params) {
        ResultType operationResult = null;
        try {
            switch (mOperationType) {
                case SAVE_STEP:
                    performStepSaving();
                    break;
                case CLEAR:
                    performCleaning();
                    break;
                default:
                    operationResult = (ResultType)checkUser();
                    break;
            }
        } catch (Exception e) {
            mException = e;
        }

        DataBaseTaskResult taskResult;
        if (mOperationType == DataBaseOperationType.CLEAR) {
            taskResult = DataBaseTaskResult.CLEARED;
        } else {
            taskResult = DataBaseTaskResult.DONE;
        }
//        return dataBaseResponse;
        return new DataBaseResponse<>(operationResult, mException, taskResult);
    }

    @Override
    protected void onPostExecute(DataBaseResponse<ResultType> result) {
        if (mCallback != null) {
            mCallback.onDataBaseActionDone(result);
        }
    }


    private void performStepSaving() throws Exception {
        DaoFactory.getDao(StepDao.TAG).add(mStep);
    }

    private DataBaseTaskResult performCleaning() throws Exception {
        DaoFactory.getDao(StepDao.TAG).removeAll();
        return DataBaseTaskResult.CLEARED;
    }

    private Boolean checkUser() {
        Cursor cursor = mContext.getContentResolver().query(AmttUri.USER.get(),
                UsersTable.PROJECTION,
                UsersTable._USER_NAME + "=?",
                new String[]{mUserName},
                null);
        boolean isAnyUserInDB = cursor.getCount() != 0;
        cursor.close();
        return isAnyUserInDB;
    }

}
