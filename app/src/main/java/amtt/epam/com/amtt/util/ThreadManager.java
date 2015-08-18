package amtt.epam.com.amtt.util;

import android.os.Handler;

import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.datasource.ThreadLoader;
import amtt.epam.com.amtt.os.Task;
import amtt.epam.com.amtt.processing.Processor;

/**
 * @author Iryna Monchanka
 * @version on 8/17/2015
 */
public class ThreadManager {

    public static final boolean IS_ASYNC_TASK = true;

    public static <ProcessingResult, DataSourceResult, Params> void
    loadData(
            final Callback<ProcessingResult> callback,
            final Params params,
            final DataSource<DataSourceResult, Params> dataSource,
            final Processor<ProcessingResult, DataSourceResult> processor
    ) {
        loadData(callback, params, dataSource, processor, new ThreadLoader<ProcessingResult, DataSourceResult, Params>() {
            @Override
            public void load(Callback<ProcessingResult> callback, Params params, DataSource<DataSourceResult, Params> dataSource, Processor<ProcessingResult, DataSourceResult> processor) {
                if (IS_ASYNC_TASK) {
                    executeInAsyncTask(callback, params, dataSource, processor);
                } else {
                    executeInThread(callback, params, dataSource, processor);
                }
            }
        });
    }

    public static <ProcessingResult, DataSourceResult, Params> void
    loadData(
            final Callback<ProcessingResult> callback,
            final Params params,
            final DataSource<DataSourceResult, Params> dataSource,
            final Processor<ProcessingResult, DataSourceResult> processor,
            final ThreadLoader<ProcessingResult, DataSourceResult, Params> threadLoader) {
        if (callback == null) {
            throw new IllegalArgumentException("callback can't be null");
        }
        threadLoader.load(callback, params, dataSource, processor);
    }

    private static <ProcessingResult, DataSourceResult, Params> void executeInAsyncTask(final Callback<ProcessingResult> callback, final Params params, final DataSource<DataSourceResult, Params> dataSource, final Processor<ProcessingResult, DataSourceResult> processor) {
        new Task<>(dataSource, params, processor, callback).execute();
    }

    private static <ProcessingResult, DataSourceResult, Params> void executeInThread(final Callback<ProcessingResult> callback, final Params params, final DataSource<DataSourceResult, Params> dataSource, final Processor<ProcessingResult, DataSourceResult> processor) {
        final Handler handler = new Handler();
        callback.onLoadStart();
        new amtt.epam.com.amtt.os.Thread<>(callback, params, dataSource, processor, handler).invoke().start();
    }
}
