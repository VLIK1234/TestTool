package amtt.epam.com.amtt.util;

import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.os.Task;
import amtt.epam.com.amtt.processing.Processor;

/**
 * @author Iryna Monchanka
 * @version on 8/17/2015
 */
public class ThreadManager {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static int MAXIMUM_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    public static ExecutorService sExecutor;

    static {
       setExecutor();
    }

    public static void setMaximumPoolSize(int size){
        MAXIMUM_POOL_SIZE = size;
        setExecutor();
    }

    private static void setExecutor() {
        sExecutor = new ThreadPoolExecutor(CPU_COUNT, MAXIMUM_POOL_SIZE, 0L, TimeUnit.MILLISECONDS, new LIFOLinkedBlockingDeque<Runnable>());
    }

    public static <Params, DataSourceResult, ProcessingResult> void
    execute(Params params,
            DataSource<Params, DataSourceResult>  dataSource,
            Processor<DataSourceResult, ProcessingResult> processor,
            Callback<ProcessingResult> callback) {
        new Task<>(params, dataSource, processor, callback).executeOnThreadExecutor(sExecutor);
    }

    public static <Params, DataSourceResult, ProcessingResult>  void
    execute(Params param,
            DataSource<Params, DataSourceResult>  dataSource,
            Callback<ProcessingResult> callback) {
        new Task<>(param, dataSource, callback).executeOnThreadExecutor(sExecutor);
    }

}
