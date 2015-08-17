package amtt.epam.com.amtt.datasource;

import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.processing.Processor;

/**
 * @author Iryna Monchanka
 * @version on 8/17/2015
 */
public interface ThreadLoader<ProcessingResult, DataSourceResult, Params> {

    void load(final Callback<ProcessingResult> callback, Params params, final DataSource<DataSourceResult, Params> dataSource, final Processor<ProcessingResult, DataSourceResult> processor);

}