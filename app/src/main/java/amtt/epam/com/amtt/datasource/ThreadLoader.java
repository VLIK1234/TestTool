package amtt.epam.com.amtt.datasource;

import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.processing.Processor;

/**
 * @author Iryna Monchanka
 * @version on 8/17/2015
 */
public interface ThreadLoader<Params, DataSourceResult, ProcessingResult> {

    void load(Params params, final DataSource<Params, DataSourceResult> dataSource,
              final Processor<DataSourceResult, ProcessingResult> processor, final Callback<ProcessingResult> callback);

}