package amtt.epam.com.amtt.common;

/**
 * Created by Artsiom_Kaliaha on 18.06.2015.
 */
public class DataRequestBuilder <Result, Param> {

    private String mDataSourceName;
    private Param mLoadingParam;
    private String mProcessorName;
    private Callback<Result> mCallback;

    public DataRequestBuilder setDataSource(String dataSourceName) {
        mDataSourceName = dataSourceName;
        return this;
    }

    public DataRequestBuilder setDataSourceParam(Param loadingParam) {
        mLoadingParam = loadingParam;
        return this;
    }

    public DataRequestBuilder setProcessor(String processorName) {
        mProcessorName = processorName;
        return this;
    }

    public DataRequestBuilder setCallback(Callback<Result> callback) {
        mCallback = callback;
        return this;
    }

    public void load() {
        CoreApplication.load(mDataSourceName, mLoadingParam, mProcessorName, mCallback);
    }

}
