package amtt.epam.com.amtt.os;

import android.os.AsyncTask;

import amtt.epam.com.amtt.processing.Processor;

/**
 * Created by Artsiom_Kaliaha on 15.06.2015.
 * Generalized AsyncTask
 * param ProcessorSource Type that will be passed to a processor after AsyncTask execution
 * param TaskResult      Type that will be returned from a processor and consequently from AsyncTask
 */
public class Task<TaskResult, ProcessorSource> extends AsyncTask<Void, Void, TaskResult> {

    /**
     * Defines method in class that can be executed by Task
     */
    public interface IExecutable<ExecutionResult> {

        ExecutionResult execute() throws Exception;

    }

    public static interface AsyncTaskCallback<ProcessingResult> {

        void onTaskStart();

        void onTaskExecuted(ProcessingResult result);

        void onTaskError(Exception e);

    }

    public static class Builder<TaskResult, ProcessorSource> {

        private AsyncTaskCallback<TaskResult> mAsyncTaskCallback;
        private IExecutable<ProcessorSource> mExecutable;
        private Processor<TaskResult, ProcessorSource> mProcessor;

        public Builder setCallback(AsyncTaskCallback<TaskResult> asyncTaskCallback) {
            mAsyncTaskCallback = asyncTaskCallback;
            return this;
        }

        public Builder setExecutable(IExecutable<ProcessorSource> executable) {
            mExecutable = executable;
            return this;
        }

        public Builder setProcessor(Processor<TaskResult, ProcessorSource> processor) {
            mProcessor = processor;
            return this;
        }

        public void createAndExecute() {
            Task<TaskResult, ProcessorSource> task = new Task<>();
            task.mAsyncTaskCallback = this.mAsyncTaskCallback;
            task.mExecutable = this.mExecutable;
            task.mProcessor = this.mProcessor;
            task.execute();
        }

    }

    private AsyncTaskCallback<TaskResult> mAsyncTaskCallback;
    private IExecutable<ProcessorSource> mExecutable;
    private Processor<TaskResult, ProcessorSource> mProcessor;
    private Exception mException;

    @Override
    protected void onPreExecute() {
        if (mAsyncTaskCallback != null) {
            mAsyncTaskCallback.onTaskStart();
        }
    }

    @Override
    protected TaskResult doInBackground(Void... params) {
        TaskResult processingTaskResult = null;
        try {
            ProcessorSource source = mExecutable.execute();
            processingTaskResult = mProcessor.process(source);
        } catch (Exception e) {
            mException = e;
        }
        return processingTaskResult;
    }

    @Override
    protected void onPostExecute(TaskResult processingTaskResult) {
        if (mAsyncTaskCallback != null) {
            if (processingTaskResult != null) {
                mAsyncTaskCallback.onTaskExecuted(processingTaskResult);
            } else {
                mAsyncTaskCallback.onTaskError(mException);
            }
        }
    }

}
