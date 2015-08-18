package amtt.epam.com.amtt.os;

import android.os.Handler;

import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.processing.Processor;

/**
 * @author Iryna Monchenko
 * @version on 18.08.2015
 */
public class Thread<Params, DataSourceResult, ProcessingResult> {

    private final Callback<ProcessingResult> callback;
    private final Params params;
    private final DataSource<DataSourceResult, Params> dataSource;
    private final Processor<ProcessingResult, DataSourceResult> processor;
    private final Handler handler;

    public Thread(Callback<ProcessingResult> callback, Params params, DataSource<DataSourceResult, Params> dataSource, Processor<ProcessingResult, DataSourceResult> processor, Handler handler) {
        this.callback = callback;
        this.params = params;
        this.dataSource = dataSource;
        this.processor = processor;
        this.handler = handler;
    }

    public java.lang.Thread invoke() {
        return new java.lang.Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final DataSourceResult result = dataSource.getData(params);
                    final ProcessingResult processingResult = processor.process(result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onLoadExecuted(processingResult);
                        }
                    });
                } catch (final Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onLoadError(e);
                        }
                    });
                }
            }
        });
    }
}
