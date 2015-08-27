package amtt.epam.com.amtt.os;

import android.os.AsyncTask;

import java.util.concurrent.ExecutorService;

import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.common.Callback;

/**
 * Created by Artsiom_Kaliaha on 15.06.2015.
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
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final ProcessingResult processingResult = doInBackground(params);
                    onPostExecute(processingResult);
                } catch (final Exception e) {
                    mException = e;
                    onPostExecute(null);
                }
            }
        });
        return this;
    }

}
