package amtt.epam.com.amtt;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.datasource.IDataSource;
import amtt.epam.com.amtt.http.HttpClient;
import amtt.epam.com.amtt.os.Task;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Ivan_Bakach on 19.03.2015.
 */
@SuppressWarnings("unchecked")
public class CoreApplication extends Application {

    public interface Callback<Result> {

        void onLoadStart();

        void onLoadExecuted(Result result);

        void onLoadError(Exception e);

    }

    public static class DataLoadingBuilder<Result, Param, Source> {

        private String mDataSourceName;
        private Param mLoadingParam;
        private Processor<Result, Source> mProcessor;
        private Callback<Result> mCallback;

        public DataLoadingBuilder setDataSource(String dataSourceName) {
            mDataSourceName = dataSourceName;
            return this;
        }

        public DataLoadingBuilder setDataSourceParam(Param loadingParam) {
            mLoadingParam = loadingParam;
            return this;
        }

        public DataLoadingBuilder setProcessor(Processor<Result, Source> processor) {
            mProcessor = processor;
            return this;
        }

        public DataLoadingBuilder setCallback(Callback<Result> callback) {
            mCallback = callback;
            return this;
        }

        public void load() {
            CoreApplication.load(mDataSourceName, mLoadingParam, mProcessor, mCallback);
        }

    }

    private static final Map<String, IDataSource> sDataSources;

    static {
        sDataSources = new HashMap<>();
        registerDataSource(HttpClient.SOURCE_NAME, HttpClient.getClient());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.setContext(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    private static void registerDataSource(String sourceName, IDataSource sourceClass) {
        sDataSources.put(sourceName, sourceClass);
    }

    private static <TaskResult, Param, ProcessorSource> void load(final String dataSourceName,
                                                                  final Param dataSourceParam,
                                                                  final Processor<TaskResult, ProcessorSource> processor,
                                                                  final Callback<TaskResult> callback) {
        if (sDataSources.get(dataSourceName) == null) {
            throw new IllegalArgumentException("Unknown data source " + dataSourceName);
        }

        new Task.Builder<TaskResult, Param, ProcessorSource>()
                .setDataSource(sDataSources.get(dataSourceName))
                .setDataSourceParam(dataSourceParam)
                .setProcessor(processor)
                .setCallback(callback)
                .createAndExecute();
    }

}
