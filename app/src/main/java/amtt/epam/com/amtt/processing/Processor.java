package amtt.epam.com.amtt.processing;

import amtt.epam.com.amtt.datasource.Plugin;

/**
 * Created by Irina Monchenko on 26.03.2015.
 */
public interface Processor<ProcessingResult, Source> extends Plugin {

    ProcessingResult process(Source source) throws Exception;

}
