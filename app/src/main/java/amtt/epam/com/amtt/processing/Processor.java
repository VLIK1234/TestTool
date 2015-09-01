package amtt.epam.com.amtt.processing;

/**
 @author Iryna Monchanka
 @version on 26.03.2015
 */

public interface Processor<DataSourceResult, ProcessingResult>{

    ProcessingResult process(DataSourceResult source) throws Exception;

}
