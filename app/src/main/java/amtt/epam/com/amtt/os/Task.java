package amtt.epam.com.amtt.os;

import android.os.AsyncTask;
import android.os.Handler;

import java.util.concurrent.ExecutorService;

import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.common.Callback;

/**
 * Created by Artsiom_Kaliaha on 15.06.2015.
 * Generalized AsyncTask
 * param Params    Type that will be passed to a source as parameter
 * param DataSourceResult Type that will be passed to a processor, in other words data returned by DataSource
 * param ProcessingResult   Type that will be returned from AsyncTask
 */
public class Task<Params, DataSourceResult, ProcessingResult> extends AsyncTask<Params, Void, ProcessingResult> {

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

    @Override
    protected void onPreExecute() {
        if (mCallback != null) {
            mCallback.onLoadStart();
        }
    }

    @Override
    protected ProcessingResult doInBackground(Params... params) {
        ProcessingResult processingResult = null;
        try {
            DataSourceResult dataSourceResult = mDataSource.getData(mParams);
            if (mProcessor != null) {
                processingResult = mProcessor.process(dataSourceResult);
            }
        } catch (Exception e) {
            mException = e;
        }
        return processingResult;
    }

    @Override
    protected void onPostExecute(ProcessingResult processingResult) {
        if (mCallback == null) {
            return;
        }
        if (mException != null) {
            mCallback.onLoadError(mException);
            return;
        }
        mCallback.onLoadExecuted(processingResult);
    }

    public Task<Params, Void, ProcessingResult> executeCorrectly(final Params... params) {
        return executeOnThreadExecutor(ThreadExecutor.sExecutor, params);
    }

    private Task executeOnThreadExecutor(ExecutorService executor, final Params... params) {
        final Handler handler = new Handler();
        onPreExecute();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final ProcessingResult processingResult = doInBackground(params);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onPostExecute(processingResult);
                        }
                    });
                } catch (final Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mException = e;
                            onPostExecute(null);
                        }
                    });
                }
            }
        });
        return this;
    }

}
