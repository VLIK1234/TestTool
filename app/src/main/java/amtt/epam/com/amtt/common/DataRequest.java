package amtt.epam.com.amtt.common;

/**
 * Created by Artsiom_Kaliaha on 18.06.2015.
 * Request executed by CoreApplication
 */
public class DataRequest<Param, Result> {

    private String mDataSourceName;
    private Param mDataSourceParam;
    private String mProcessorName;
    private Callback<Result> mCallback;

    public DataRequest(String dataSourceName, Param dataSourceParam, String processorName, Callback<Result> callback) {
        mDataSourceName = dataSourceName;
        mDataSourceParam = dataSourceParam;
        mProcessorName = processorName;
        mCallback = callback;
    }

    public String getDataSource() {
        return mDataSourceName;
    }

    public Param getDataSourceParam() {
        return mDataSourceParam;
    }

    public String getProcessor() {
        return mProcessorName;
    }

    public Callback getCallback() {
        return mCallback;
    }

}
