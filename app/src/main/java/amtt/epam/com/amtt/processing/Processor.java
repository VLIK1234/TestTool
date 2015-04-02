package amtt.epam.com.amtt.processing;

/**
 * Created by shiza on 26.03.2015.
 */
public interface Processor<ProcessingResult, Source> {

    ProcessingResult process(Source source) throws Exception;
}
