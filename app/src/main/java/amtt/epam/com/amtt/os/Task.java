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
public class Task<Params, DataSourceResult, ProcessingResult> extends AsyncTask<Params, Void, ProcessingResult> {

    private Callback<ProcessingResult> mCallback;
    private DataSource<Params, DataSourceResult> mDataSource;
    private Params mParams;
    private Processor<DataSourceResult, ProcessingResult> mProcessor;
    private Exception mException;
    private ProcessingResult mProcessingResult;

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
    protected ProcessingResult doInBackground(Params... params) {
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
    protected void onPostExecute(ProcessingResult processingResult) {
        if (mException != null) {
            mCallback.onLoadError(mException);
            return;
        }
        if (mCallback == null) {
            return;
        }
        mCallback.onLoadExecuted(processingResult);
    }

    public Task executeOnThreadExecutor(ExecutorService executor, final Params... params) {
        onPreExecute();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... param) {
                try {
                    mProcessingResult = Task.this.doInBackground(params);
                } catch (final Exception e) {
                    mException = e;
                    mProcessingResult = null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                Task.this.onPostExecute(mProcessingResult);
            }
        }.executeOnExecutor(executor);

        return this;
    }
}
