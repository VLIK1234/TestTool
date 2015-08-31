package amtt.epam.com.amtt.util;

import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    public static final String NO_PROCESSOR = "NO_PROCESSOR";
    private static int MAXIMUM_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService sExecutor;

    static {

        sExecutor = new ThreadPoolExecutor(CPU_COUNT, MAXIMUM_POOL_SIZE, 0L, TimeUnit.MILLISECONDS, new LIFOLinkedBlockingDeque<Runnable>());
    }

    public void setMaximumPoolSize(int size){
        MAXIMUM_POOL_SIZE = size;
    }

    public static <Params, DataSourceResult, ProcessingResult> void
    loadData(final Params params,
             final DataSource<Params, DataSourceResult> dataSource,
             final Processor<DataSourceResult, ProcessingResult> processor,
             final Callback<ProcessingResult> callback) {

        loadData(params, dataSource, processor, callback, new ThreadLoader<Params, DataSourceResult, ProcessingResult>() {
            @Override
            public void load(Params params,
                             DataSource<Params, DataSourceResult> dataSource,
                             Processor<DataSourceResult, ProcessingResult> processor,
                             Callback<ProcessingResult> callback) {
                executeInAsyncTask(params, dataSource, processor, callback);
            }
        });

    }

    public static <Params, DataSourceResult, ProcessingResult> void
    loadData(final Params params,
             final DataSource<Params, DataSourceResult> dataSource,
             final Processor<DataSourceResult, ProcessingResult> processor,
             final Callback<ProcessingResult> callback,
             final ThreadLoader<Params, DataSourceResult,  ProcessingResult> threadLoader) {

            threadLoader.load(params, dataSource, processor, callback);

    }

    private static <ProcessingResult, DataSourceResult, Params> void
    executeInAsyncTask(final Params params,
                       final DataSource<Params, DataSourceResult> dataSource,
                       final Processor<DataSourceResult, ProcessingResult> processor,
                       final Callback<ProcessingResult> callback) {

        new Task<>(params, dataSource, processor, callback).executeOnThreadExecutor(sExecutor);

    }

    public static <Params, DataSourceResult, ProcessingResult> void
    execute(Params param,
            DataSource<Params, DataSourceResult>  dataSource,
            Processor<DataSourceResult, ProcessingResult> processor,
            Callback<ProcessingResult> callback) {
            ThreadManager.loadData(param, dataSource, processor, callback);
    }

    public static <Params, DataSourceResult, ProcessingResult>  void
    execute(Params param,
            DataSource<Params, DataSourceResult>  dataSource,
            Callback<ProcessingResult> callback) {
        new Task<>(param, dataSource, callback).executeOnExecutor(sExecutor);
    }

}
