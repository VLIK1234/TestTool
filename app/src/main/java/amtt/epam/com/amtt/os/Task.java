package amtt.epam.com.amtt.os;

import android.os.AsyncTask;

import amtt.epam.com.amtt.datasource.IDataSource;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.CoreApplication.Callback;

/**
 * Created by Artsiom_Kaliaha on 15.06.2015.
 * Generalized AsyncTask
 * param
 * param ProcessorSource Type that will be passed to a processor after AsyncTask execution
 * param TaskResult      Type that will be returned from a processor and consequently from AsyncTask
 */
public class Task<TaskResult, DataSourceParam, ProcessorSource> extends AsyncTask<Void, Void, TaskResult> {

    public static class Builder<TaskResult, DataSourceParam, ProcessorSource> {

        private Callback<TaskResult> mCallback;
        private IDataSource<ProcessorSource, DataSourceParam> mDataSource;
        private DataSourceParam mDataSourceDataSourceParam;
        private Processor<TaskResult, ProcessorSource> mProcessor;

        public Builder setCallback(Callback<TaskResult> asyncTaskCallback) {
            mCallback = asyncTaskCallback;
            return this;
        }

        public Builder setDataSource(IDataSource<ProcessorSource, DataSourceParam> dataSource) {
            mDataSource = dataSource;
            return this;
        }

        public Builder setDataSourceParam(DataSourceParam dataSourceDataSourceParam) {
            mDataSourceDataSourceParam = dataSourceDataSourceParam;
            return this;
        }

        public Builder setProcessor(Processor<TaskResult, ProcessorSource> processor) {
            mProcessor = processor;
            return this;
        }

        public void createAndExecute() {
            Task<TaskResult, DataSourceParam, ProcessorSource> task = new Task<>();
            task.mDataSource = this.mDataSource;
            task.mDataSourceParam = this.mDataSourceDataSourceParam;
            task.mProcessor = this.mProcessor;
            task.mCallback = this.mCallback;
            task.execute();
        }

    }

    private Callback<TaskResult> mCallback;
    private IDataSource<ProcessorSource, DataSourceParam> mDataSource;
    private DataSourceParam mDataSourceParam;
    private Processor<TaskResult, ProcessorSource> mProcessor;
    private Exception mException;

    @Override
    protected void onPreExecute() {
        if (mCallback != null) {
            mCallback.onLoadStart();
        }
    }

    @Override
    protected TaskResult doInBackground(Void... params) {
        TaskResult processingTaskResult = null;
        try {
            ProcessorSource source = mDataSource.getData(mDataSourceParam);
            if (mProcessor != null) {
                processingTaskResult = mProcessor.process(source);
            }
        } catch (Exception e) {
            mException = e;
        }
        return processingTaskResult;
    }

    @Override
    protected void onPostExecute(TaskResult processingTaskResult) {
        if (mCallback == null) {
            return;
        }
        if (mException != null) {
            mCallback.onLoadError(mException);
            return;
        }
        mCallback.onLoadExecuted(processingTaskResult);
    }

}
