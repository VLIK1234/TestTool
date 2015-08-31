package amtt.epam.com.amtt.common;

import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.processing.Processor;

/**
 @author Artsiom_Kaliaha
 @version on 18.06.2015
 */

public class DataRequest<Param, DataSourceResult, ProcessingResult> {

    private final DataSource<Param, DataSourceResult> mDataSource;
    private final Param mDataSourceParam;
    private final Processor<DataSourceResult, ProcessingResult> mProcessor;
    private final Callback<ProcessingResult> mCallback;

    public DataRequest(DataSource<Param, DataSourceResult> dataSource, Param dataSourceParam, Processor<DataSourceResult, ProcessingResult> processor, Callback<ProcessingResult> callback) {
        mDataSource = dataSource;
        mDataSourceParam = dataSourceParam;
        mProcessor = processor;
        mCallback = callback;
    }

    public DataSource<Param, DataSourceResult> getDataSource() {
        return mDataSource;
    }

    public Param getDataSourceParam() {
        return mDataSourceParam;
    }

    public Processor<DataSourceResult, ProcessingResult> getProcessor() {
        return mProcessor;
    }

    public Callback<ProcessingResult> getCallback() {
        return mCallback;
    }

}
