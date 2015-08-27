package amtt.epam.com.amtt.common;

/**
 * Created by Artsiom_Kaliaha on 18.06.2015.
 */

public class DataRequest<Param> {

    private final String mDataSourceName;
    private final Param mDataSourceParam;
    private final String mProcessorName;
    private final Callback mCallback;

    public DataRequest(String dataSourceName, Param dataSourceParam, String processorName, Callback callback) {
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
