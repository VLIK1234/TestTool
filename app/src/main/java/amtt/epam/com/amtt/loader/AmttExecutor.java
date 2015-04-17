package amtt.epam.com.amtt.loader;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Artsiom_Kaliaha on 03.04.2015.
 */
public class AmttExecutor implements Executor {

    private static final int CPU_COUNT;
    private static final int CORE_SIZE = 0;
    private static final int EXCESS_THREADS_LIFETIME  = 0;
    private final ExecutorService sExecutorService;

    static {
        CPU_COUNT = Runtime.getRuntime().availableProcessors();
    }

    public AmttExecutor(int queueCapacity) {
        sExecutorService = new ThreadPoolExecutor(CORE_SIZE,
                CPU_COUNT,
                EXCESS_THREADS_LIFETIME,
                TimeUnit.NANOSECONDS,
                new BlockingStack<Runnable>(queueCapacity));
    }

    public AmttExecutor() {
        sExecutorService = new ThreadPoolExecutor(CORE_SIZE,
                CPU_COUNT,
                EXCESS_THREADS_LIFETIME,
                TimeUnit.NANOSECONDS,
                new BlockingStack<Runnable>());
    }

    public void execute(@NonNull Runnable runnable) {
        sExecutorService.execute(runnable);
    }

}
