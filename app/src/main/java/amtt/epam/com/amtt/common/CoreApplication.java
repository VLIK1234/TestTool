package amtt.epam.com.amtt.common;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.datasource.IDataSource;
import amtt.epam.com.amtt.os.Task;
import amtt.epam.com.amtt.processing.Processor;

/**
 * Created by Artsiom_Kaliaha on 18.06.2015.
 */
@SuppressWarnings("unchecked")
public abstract class CoreApplication extends Application {

    public static final String NO_PROCESSOR = "NO_PROCESSOR";
    private static final Map<String, IDataSource> sDataSources;
    private static final Map<String, Processor> sProcessors;

    private static Context sContext;

    static {
        sDataSources = new HashMap<>();
        sProcessors = new HashMap<>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
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

    public static <TaskResult, Param, ProcessorSource> void executeRequest(DataRequest<TaskResult, Param> request) {
        String dataSourceName = request.getDataSource();
        String processorName = request.getProcessor();
        if (sDataSources.get(dataSourceName) == null) {
            throw new IllegalArgumentException("Unknown / unregistered data source " + dataSourceName);
        }
        if (sProcessors.get(processorName) == null && !processorName.equals(NO_PROCESSOR)) {
            throw new IllegalArgumentException("Unknown / unregistered processorName " + processorName);
        }

        new Task.Builder<TaskResult, Param, ProcessorSource>()
                .setDataSource(sDataSources.get(dataSourceName))
                .setDataSourceParam(request.getDataSourceParam())
                .setProcessor(sProcessors.get(processorName))
                .setCallback(request.getCallback())
                .createAndExecute();
    }

    public static Context getContext() {
        return sContext;
    }

    public abstract void performRegistration();

}
