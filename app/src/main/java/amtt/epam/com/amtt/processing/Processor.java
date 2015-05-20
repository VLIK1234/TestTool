package amtt.epam.com.amtt.processing;

/**
 * Created by Irina Monchenko on 26.03.2015.
 */
public interface Processor<ProcessingResult, Source> {

    ProcessingResult process(Source source);
}
