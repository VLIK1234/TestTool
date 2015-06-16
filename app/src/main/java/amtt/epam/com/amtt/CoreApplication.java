package amtt.epam.com.amtt;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.datasource.IDataSource;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.ContextHolder;
/**
 * Created by Ivan_Bakach on 19.03.2015.
 */
public class CoreApplication extends Application {

    public interface Callback<Result> {

        void onLoadStart();

        void onLoadExecuted(Result result);

        void onLoadError(Exception e);

    }

    public static class DataLoadingBuilder<Result, Source> {

        private IDataSource<Source> mDataSource;
        private Processor<Result, Source> mProcessor;
        private Callback<Result> mCallback;

        public void setDataSource(IDataSource<Source> dataSource) {
            mDataSource = dataSource;
        }

        public void setProcessor(Processor<Result, Source> processor) {
            mProcessor = processor;
        }

        public void setCallback(Callback<Result> callback) {
            mCallback = callback;
        }
        
        public void load() {
            CoreApplication.load(mDataSource, mProcessor, mCallback);
        }
        
    }

    private static final Map<String, IDataSource> sDataSources;

    static {
        sDataSources = new HashMap<>();
        //registerDataSource(, );
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
    
    private static <SourceResult, ProcessingResult> void load(final IDataSource<SourceResult> source,
                                                              final Processor<ProcessingResult, SourceResult> processor,
                                                              final Callback<ProcessingResult> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                callback.onLoadStart();
                ProcessingResult processingResult;
                try {
                    SourceResult sourceResult = source.getData();
                    processingResult = processor.process(sourceResult);
                } catch (Exception e) {
                    callback.onLoadError(e);
                    return;
                }
                callback.onLoadExecuted(processingResult);
            }
        }).start();
    }
    
}
