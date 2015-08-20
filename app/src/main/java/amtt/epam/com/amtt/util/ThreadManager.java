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

    public static <Params, DataSourceResult, ProcessingResult > void
    loadData(
            final Params params,
            final DataSource<Params, DataSourceResult> dataSource,
            final Processor<DataSourceResult, ProcessingResult> processor,
            final Callback<ProcessingResult> callback
    ) {
        loadData(params, dataSource, processor, callback, new ThreadLoader<Params, DataSourceResult, ProcessingResult>() {

            @Override
            public void load(Params params, DataSource<Params, DataSourceResult> dataSource,
                             Processor<DataSourceResult, ProcessingResult> processor, Callback<ProcessingResult> callback) {
                    executeInAsyncTask(params, dataSource, processor, callback);
                   // executeInThread(callback, params, dataSource, processor);
            }
        });
    }

    public static <Params, DataSourceResult, ProcessingResult> void
    loadData(
            final Params params,
            final DataSource<Params, DataSourceResult> dataSource,
            final Processor<DataSourceResult, ProcessingResult> processor,
            final Callback<ProcessingResult> callback,
            final ThreadLoader<Params, DataSourceResult,  ProcessingResult> threadLoader) {
        if (callback != null) {
            threadLoader.load(params, dataSource, processor, callback);
        }
    }

    public static <ProcessingResult, Params> void
    loadData(
            final Callback<ProcessingResult> callback,
            final Params params) {
        if (callback != null) {
            executeInAsyncTask(params, null, null, callback);
        }
    }


    private static <ProcessingResult, DataSourceResult, Params> void
    executeInAsyncTask(final Params params,
                       final DataSource<Params, DataSourceResult> dataSource,
                       final Processor<DataSourceResult, ProcessingResult> processor,
                       final Callback<ProcessingResult> callback) {
        new Task<>(params, dataSource, processor, callback).executeCorrectly();
    }

    private static <ProcessingResult, DataSourceResult, Params> void
    executeInThread(final Params params,
                    final DataSource<Params, DataSourceResult> dataSource,
                    final Processor<DataSourceResult, ProcessingResult> processor,
                    final Callback<ProcessingResult> callback) {
        final Handler handler = new Handler();
        callback.onLoadStart();
        new amtt.epam.com.amtt.os.Thread<>(callback, params, dataSource, processor, handler).executeOnThreadExecutor();
    }
}
