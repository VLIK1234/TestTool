package amtt.epam.com.amtt.os;

import android.os.AsyncTask;

import java.util.concurrent.ExecutorService;

import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.common.Callback;

/**
 @author Iryna Monchanka
 @version on 28.08.2015
 */
public class Task<Params, DataSourceResult, ProcessingResult> extends AsyncTask<Void, Void, ProcessingResult> {

    private final Callback<ProcessingResult> mCallback;
    private final DataSource<Params, DataSourceResult> mDataSource;
    private final Params mParams;
    private final Processor<DataSourceResult, ProcessingResult> mProcessor;
    private Exception mException;

    public Task(Params params, DataSource<Params, DataSourceResult> dataSource,
                Processor<DataSourceResult, ProcessingResult> processor, Callback<ProcessingResult> callback) {
        mDataSource = dataSource;
        mParams = params;
        mProcessor = processor;
        mCallback = callback;
    }

    public Task(Params params,DataSource<Params, DataSourceResult> dataSource, Callback<ProcessingResult> callback) {
        mParams = params;
        mDataSource = dataSource;
        mCallback = callback;
        mProcessor = null;
    }

    @Override
    protected void onPreExecute() {
        if (mCallback != null) {
            mCallback.onLoadStart();
        }
    }

    @Override
    protected final ProcessingResult doInBackground(Void... params) {
        ProcessingResult processingResult;
        try {
            DataSourceResult dataSourceResult = mDataSource.getData(mParams);
            if (mProcessor != null) {
                processingResult = mProcessor.process(dataSourceResult);
                return processingResult;
            } else {
                return (ProcessingResult) dataSourceResult;
            }
        } catch (Exception e) {
            mException = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(ProcessingResult result) {
        if (mCallback == null) {
            return;
        }
        if (mException != null) {
            mCallback.onLoadError(mException);
            return;
        }
        mCallback.onLoadExecuted(result);
    }

    public final Task executeOnThreadExecutor(ExecutorService executor) {
        Task.this.executeOnExecutor(executor);
        return this;
    }
}
