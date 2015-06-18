package amtt.epam.com.amtt.common;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.datasource.IDataSource;
import amtt.epam.com.amtt.os.Task;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Artsiom_Kaliaha on 18.06.2015.
 */
@SuppressWarnings("unchecked")
public abstract class CoreApplication extends Application {

    public static final String NO_PROCESSOR = "NO_PROCESSOR";
    private static final Map<String, IDataSource> sDataSources;
    private static final Map<String, Processor> sProcessors;

    static {
        sDataSources = new HashMap<>();
        sProcessors = new HashMap<>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.setContext(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        performRegistration();
    }

    public void registerDataSource(String sourceName, IDataSource dataSource) {
        sDataSources.put(sourceName, dataSource);
    }

    public void registerProcessor(String sourceName, Processor processor) {
        sProcessors.put(sourceName, processor);
    }

    public void unregisterDataSource(String sourceName) {
        sDataSources.remove(sourceName);
    }

    public void unregisterProcessor(String sourceName) {
        sProcessors.remove(sourceName);
    }

    public static <TaskResult, Param, ProcessorSource> void load(final String dataSourceName,
                                                                 final Param dataSourceParam,
                                                                 final String processorName,
                                                                 final Callback<TaskResult> callback) {
        if (sDataSources.get(dataSourceName) == null) {
            throw new IllegalArgumentException("Unknown / unregistered data source " + dataSourceName);
        }
        if (sProcessors.get(processorName) == null && !processorName.equals(NO_PROCESSOR)) {
            throw new IllegalArgumentException("Unknown / unregistered processor " + processorName);
        }

        new Task.Builder<TaskResult, Param, ProcessorSource>()
                .setDataSource(sDataSources.get(dataSourceName))
                .setDataSourceParam(dataSourceParam)
                .setProcessor(sProcessors.get(processorName))
                .setCallback(callback)
                .createAndExecute();
    }

    public abstract void performRegistration();

}
