package amtt.epam.com.amtt.common;

/**
 * Created by Artsiom_Kaliaha on 18.06.2015.
 */
public class DataRequest<Result, Param> {

    public static class Builder<Result, Param> {

        private String mDataSourceName;
        private Param mDataSourceParam;
        private String mProcessorName;
        private Callback<Result> mCallback;

        public Builder setDataSource(String dataSourceName) {
            mDataSourceName = dataSourceName;
            return this;
        }

        public Builder setDataSourceParam(Param loadingParam) {
            mDataSourceParam = loadingParam;
            return this;
        }

        public Builder setProcessor(String processorName) {
            mProcessorName = processorName;
            return this;
        }

        public Builder setCallback(Callback<Result> callback) {
            mCallback = callback;
            return this;
        }

        public DataRequest create() {
            DataRequest<Result, Param> request = new DataRequest<>();
            request.mDataSourceName = this.mDataSourceName;
            request.mDataSourceParam = this.mDataSourceParam;
            request.mProcessorName = this.mProcessorName;
            request.mCallback = this.mCallback;
            return request;
        }

    }

    private String mDataSourceName;
    private Param mDataSourceParam;
    private String mProcessorName;
    private Callback<Result> mCallback;

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
