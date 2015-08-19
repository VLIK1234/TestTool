package amtt.epam.com.amtt.os;

import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Iryna Monchenko
 * @version on 19.08.2015
 */

public class ThreadExecutor {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService sExecutor;

    static {
        sExecutor = new ThreadPoolExecutor(CPU_COUNT, CPU_COUNT, 0L, TimeUnit.MILLISECONDS, new LIFOLinkedBlockingDeque<Runnable>());
    }
}
