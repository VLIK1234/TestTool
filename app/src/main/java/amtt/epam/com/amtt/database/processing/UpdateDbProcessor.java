package amtt.epam.com.amtt.database.processing;

import amtt.epam.com.amtt.processing.Processor;

/**
 * @author Iryna Monchenko
 * @version on 21.08.2015
 */

public class UpdateDbProcessor implements Processor<Integer, Integer> {

    @Override
    public Integer process(Integer source) throws Exception {
        return source;
    }

}
