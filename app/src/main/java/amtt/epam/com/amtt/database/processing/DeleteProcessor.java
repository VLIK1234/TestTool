package amtt.epam.com.amtt.database.processing;

import amtt.epam.com.amtt.processing.Processor;

/**
 * @author Iryna Monchenko
 * @version on 21.08.2015
 */

public class DeleteProcessor implements Processor<Integer, Integer> {

    public static final String NAME = DeleteProcessor.class.getName();

    @Override
    public Integer process(Integer source) throws Exception {
        return source;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
