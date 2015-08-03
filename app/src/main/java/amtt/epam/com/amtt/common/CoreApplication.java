package amtt.epam.com.amtt.common;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.datasource.Plugin;
import amtt.epam.com.amtt.os.Task;
import amtt.epam.com.amtt.processing.Processor;

/**
 @author Artsiom_Kaliaha
 @version on 18.06.2015
 */

public abstract class CoreApplication extends Application {

    public static final String NO_PROCESSOR = "NO_PROCESSOR";
    private static final Map<String, DataSource> sDataSources;
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

    public void registerPlugin(Plugin plugin) {
        if (plugin.getName() == null) {
            throw new IllegalArgumentException("Method getName() for " + plugin.getClass().getName() + " isn't overridden");
        }
        if (plugin instanceof DataSource) {
            sDataSources.put(plugin.getName(), (DataSource) plugin);
        } else {
            sProcessors.put(plugin.getName(), (Processor) plugin);
        }
    }

    public static <Param> void executeRequest(DataRequest<Param> request) {
        String dataSourceName = request.getDataSource();
        String processorName = request.getProcessor();
        if (sDataSources.get(dataSourceName) == null) {
            throw new IllegalArgumentException("Unknown / unregistered data source " + dataSourceName);
        }
        if (sProcessors.get(processorName) == null && !processorName.equals(NO_PROCESSOR)) {
            throw new IllegalArgumentException("Unknown / unregistered processorName " + processorName);
        }

        new Task<>(sDataSources.get(dataSourceName), request.getDataSourceParam(), sProcessors.get(processorName), request.getCallback()).execute();
    }

    public static Context getContext() {
        return sContext;
    }

    public abstract void performRegistration();

}
