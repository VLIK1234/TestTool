package amtt.epam.com.amtt.database.task;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

/**
 * Created by Artsiom_Kaliaha on 26.03.2015.
 */
@SuppressWarnings("unchecked")
public class DataBaseTask<ResultType> extends AsyncTask<Void, Void, DataBaseTask.DataBaseResponse<ResultType>> {

    public static class DataBaseResponse<ResultType> {

        public ResultType mResult;

        public DataBaseResponse(ResultType result) {
            mResult = result;
        }

        public ResultType getResult() {
            return mResult;
        }

    }

    public static class Builder<ResultType> {

        private DataBaseCallback<ResultType> mCallback;
        private DataBaseMethod<ResultType> mDataBaseMethod;

        public Builder setCallback(@NonNull DataBaseCallback callback) {
            mCallback = callback;
            return this;
        }

        public Builder setMethod(DataBaseMethod dataBaseMethod) {
            mDataBaseMethod = dataBaseMethod;
            return this;
        }

        public void createAndExecute() {
            DataBaseTask<ResultType> dataBaseTask = new DataBaseTask<>();
            dataBaseTask.mDataBaseMethod = this.mDataBaseMethod;
            dataBaseTask.mCallback = this.mCallback;
            dataBaseTask.execute();
        }

    }

    private DataBaseMethod<ResultType> mDataBaseMethod;
    private DataBaseCallback<ResultType> mCallback;
    private Exception mException;

    @Override
    protected DataBaseResponse<ResultType> doInBackground(Void... params) {
        DataBaseResponse<ResultType> dataBaseResponse = null;
        try {
            dataBaseResponse = mDataBaseMethod.execute();
        } catch (Exception e) {
            mException = e;
        }
        return dataBaseResponse;
    }

    @Override
    protected void onPostExecute(DataBaseResponse<ResultType> result) {
        if (mCallback != null) {
            if (mException == null) {
                mCallback.onDataBaseRequestPerformed(result);
            } else {
                mCallback.onDataBaseRequestError(mException);
            }
        }
    }

}
