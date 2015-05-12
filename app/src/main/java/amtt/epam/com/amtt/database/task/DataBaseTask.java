package amtt.epam.com.amtt.database.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.dao.StepDao;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Artsiom_Kaliaha on 26.03.2015.
 */
public class DataBaseTask<ResultType> extends AsyncTask<Void, Void, DataBaseResponse<ResultType>> {

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

    @Override
    @SuppressWarnings("unchecked")
    protected DataBaseResponse<ResultType> doInBackground(Void... params) {
        DataBaseResponse<ResultType> dataBaseResponse = new DataBaseResponse<>();
        try {
            switch (mOperationType) {
                case SAVE_STEP:
                    performStepSaving();
                case CLEAR:
                    performCleaning();
                default:
                    dataBaseResponse.setValueResult((ResultType) checkUser());
            }
        } catch (Exception e) {
            dataBaseResponse = new DataBaseResponse<>();
            dataBaseResponse.setTaskResult(DataBaseTaskResult.ERROR);
        }
        if (mOperationType == DataBaseOperationType.CLEAR) {
            dataBaseResponse.setTaskResult(DataBaseTaskResult.CLEARED);
        } else {
            dataBaseResponse.setTaskResult(DataBaseTaskResult.DONE);
        }
        return dataBaseResponse;
    }

    @Override
    protected void onPostExecute(DataBaseResponse<ResultType> result) {
        mCallback.onDataBaseActionDone(result);
    }


    private void performStepSaving() throws Exception {
        new StepDao().add(mStep);
    }

    private DataBaseTaskResult performCleaning() throws Exception{
        new StepDao().removeAll();
        return DataBaseTaskResult.CLEARED;
    }

    private Boolean checkUser() {
        Cursor cursor = mContext.getContentResolver().query(AmttContentProvider.USER_CONTENT_URI,
                UsersTable.PROJECTION,
                UsersTable._USER_NAME + "=?",
                new String[]{ mUserName },
                null);
        boolean isAnyUserInDB = cursor.getCount() != 0;
        cursor.close();
        return isAnyUserInDB;
    }

}
