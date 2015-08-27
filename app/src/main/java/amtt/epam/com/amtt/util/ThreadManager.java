package amtt.epam.com.amtt.util;

import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.common.DataRequest;
import amtt.epam.com.amtt.database.DataBaseSource;
import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.datasource.Plugin;
import amtt.epam.com.amtt.datasource.ThreadLoader;
import amtt.epam.com.amtt.googleapi.processing.SpreadsheetProcessor;
import amtt.epam.com.amtt.googleapi.processing.WorksheetProcessor;
import amtt.epam.com.amtt.http.HttpClient;
import amtt.epam.com.amtt.os.Task;
import amtt.epam.com.amtt.processing.ComponentsProcessor;
import amtt.epam.com.amtt.processing.PostCreateIssueProcessor;
import amtt.epam.com.amtt.processing.PriorityProcessor;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.processing.ProjectsProcessor;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.processing.UsersAssignableProcessor;
import amtt.epam.com.amtt.processing.VersionsProcessor;

/**
 * @author Iryna Monchanka
 * @version on 8/17/2015
 */
public class ThreadManager {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    public static final String NO_PROCESSOR = "NO_PROCESSOR";
    private static final Map<String, DataSource> sHttpDataSources;
    private static final Map<String, Processor> sHttpProcessors;
    private static int MAXIMUM_POOL_SIZE = CPU_COUNT;
    public static final ExecutorService sExecutor;

    static {
        sHttpDataSources = new HashMap<>();
        sHttpProcessors = new HashMap<>();
        sExecutor = new ThreadPoolExecutor(CPU_COUNT, MAXIMUM_POOL_SIZE, 0L, TimeUnit.MILLISECONDS, new LIFOLinkedBlockingDeque<Runnable>());
    }

    public void setMaximumPoolSize(int size){
        MAXIMUM_POOL_SIZE = size;
    }

    public static <Params, DataSourceResult, ProcessingResult> void
    loadData(final Params params, final DataSource<Params, DataSourceResult> dataSource,
            final Processor<DataSourceResult, ProcessingResult> processor, final Callback<ProcessingResult> callback) {
        loadData(params, dataSource, processor, callback, new ThreadLoader<Params, DataSourceResult, ProcessingResult>() {

            @Override
            public void load(Params params, DataSource<Params, DataSourceResult> dataSource,
                             Processor<DataSourceResult, ProcessingResult> processor, Callback<ProcessingResult> callback) {
                executeInAsyncTask(params, dataSource, processor, callback);
            }
        });
    }

    public static <Params, DataSourceResult, ProcessingResult> void
    loadData(final Params params, final DataSource<Params, DataSourceResult> dataSource,
            final Processor<DataSourceResult, ProcessingResult> processor, final Callback<ProcessingResult> callback,
            final ThreadLoader<Params, DataSourceResult,  ProcessingResult> threadLoader) {
            threadLoader.load(params, dataSource, processor, callback);
    }

    private static <ProcessingResult, DataSourceResult, Params> void
    executeInAsyncTask(final Params params, final DataSource<Params, DataSourceResult> dataSource,
                       final Processor<DataSourceResult, ProcessingResult> processor,
                       final Callback<ProcessingResult> callback) {
        new Task<>(params, dataSource, processor, callback).executeOnThreadExecutor(sExecutor);
    }

    public static <Param> void executeDbRequest(DataRequest<Param> request, DataSource dataSource, Processor processor) {
            if (dataSource == null) {
                throw new IllegalArgumentException("Unknown data source");
            }
            if (processor == null) {
                throw new IllegalArgumentException("Unknown processor");
            }
            ThreadManager.loadData(request.getDataSourceParam(), dataSource, processor, request.getCallback());
    }

    public static <Param> void executeHttpRequest(DataRequest<Param> request) {
        String dataSourceName = request.getDataSource();
        String processorName = request.getProcessor();
            if (sHttpDataSources.get(dataSourceName) == null) {
                throw new IllegalArgumentException("Unknown / unregistered data source " + dataSourceName);
            }
            if (sHttpProcessors.get(processorName) == null && !processorName.equals(NO_PROCESSOR)) {
                throw new IllegalArgumentException("Unknown / unregistered processorName " + processorName);
            }
            ThreadManager.loadData(request.getDataSourceParam(), sHttpDataSources.get(dataSourceName), sHttpProcessors.get(processorName), request.getCallback());
    }

    public static void registerPlugin(Plugin plugin) {
        if (plugin.getName() == null) {
            throw new IllegalArgumentException("Method getName() for " + plugin.getClass().getName() + " isn't overridden");
        }
        if (plugin instanceof DataSource) {
            sHttpDataSources.put(plugin.getName(), (DataSource) plugin);
        } else {
            sHttpProcessors.put(plugin.getName(), (Processor) plugin);
        }
    }

    public static void performRegistration() {
        registerPlugin(new HttpClient());
        registerPlugin(new ComponentsProcessor());
        registerPlugin(new UserInfoProcessor());
        registerPlugin(new VersionsProcessor());
        registerPlugin(new UsersAssignableProcessor());
        registerPlugin(new ProjectsProcessor());
        registerPlugin(new PriorityProcessor());
        registerPlugin(new PostCreateIssueProcessor());
        registerPlugin(new SpreadsheetProcessor());
        registerPlugin(new WorksheetProcessor());
    }

}
