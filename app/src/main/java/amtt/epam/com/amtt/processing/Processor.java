package amtt.epam.com.amtt.processing;

import amtt.epam.com.amtt.datasource.Plugin;

/**
 @author Iryna Monchanka
 @version on 26.03.2015
 */

public interface Processor<DataSourceResult, ProcessingResult> extends Plugin {

    ProcessingResult process(DataSourceResult source) throws Exception;

}
