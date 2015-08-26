package amtt.epam.com.amtt.os;

import android.os.AsyncTask;

import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.common.Callback;

/**
 * Created by Artsiom_Kaliaha on 15.06.2015.
 * Generalized AsyncTask
 * param Param    Type that will be passed to a source as parameter
 * param Progress Type that will be passed to a processor, in other words data returned by DataSource
 * param Result   Type that will be returned from AsyncTask
 */
public class Task<Param, Progress, Result> extends AsyncTask<Param, Void, Result> {

    private final Callback<Result> mCallback;
    private final DataSource<Progress, Param> mDataSource;
    private final Param mParam;
    private final Processor<Result, Progress> mProcessor;
    private Exception mException;

    public Task(DataSource<Progress, Param> dataSource, Param param, Processor<Result, Progress> processor, Callback<Result> callback) {
        mDataSource = dataSource;
        mParam = param;
        mProcessor = processor;
        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        if (mCallback != null) {
            mCallback.onLoadStart();
        }
    }

    @SafeVarargs
    @Override
    protected final Result doInBackground(Param... params) {
        Result result = null;
        try {
            Progress progress = mDataSource.getData(mParam);
            if (mProcessor != null) {
                result = mProcessor.process(progress);
            }
        } catch (Exception e) {
            mException = e;
        }
        return result;
    }

    @Override
    protected void onPostExecute(Result result) {
        if (mCallback == null) {
            return;
        }
        if (mException != null) {
            mCallback.onLoadError(mException);
            return;
        }
        mCallback.onLoadExecuted(result);
    }

}
