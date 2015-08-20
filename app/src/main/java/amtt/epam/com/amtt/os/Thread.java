package amtt.epam.com.amtt.os;

import android.os.Handler;

import java.util.concurrent.ExecutorService;

import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.processing.Processor;

/**
 * @author Iryna Monchenko
 * @version on 18.08.2015
 */
public class Thread<Params, DataSourceResult, ProcessingResult> {

    private final Callback<ProcessingResult> mCallback;
    private final Params mParams;
    private final DataSource<Params, DataSourceResult> mDataSource;
    private final Processor<DataSourceResult, ProcessingResult> mProcessor;
    private final Handler mHandler;

    public Thread(Callback<ProcessingResult> callback, Params params, DataSource<Params, DataSourceResult> dataSource, Processor<DataSourceResult, ProcessingResult> processor, Handler handler) {
        this.mCallback = callback;
        this.mParams = params;
        this.mDataSource = dataSource;
        this.mProcessor = processor;
        this.mHandler = handler;
    }

    public void executeOnThreadExecutor() {
        ExecutorService executor = ThreadExecutor.sExecutor;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final DataSourceResult result = mDataSource.getData(mParams);
                    final ProcessingResult processingResult = mProcessor.process(result);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onLoadExecuted(processingResult);
                        }
                    });
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onLoadError(e);
                        }
                    });
                }
            }
        });
    }
}
