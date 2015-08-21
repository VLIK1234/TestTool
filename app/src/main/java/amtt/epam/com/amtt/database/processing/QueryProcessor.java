package amtt.epam.com.amtt.database.processing;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.IOUtils;

/**
 * @author Iryna Monchenko
 * @version on 21.08.2015
 */

public class QueryProcessor<Entity extends DatabaseEntity> implements Processor<Cursor, List<Entity>> {

    public static final String NAME = QueryProcessor.class.getName();

    @Override
    public List<Entity> process(Cursor source) throws Exception {
        final List<Entity> listObject = new ArrayList<>();
        Entity entity = null;
        if (source != null) {
            if (source.moveToFirst()) {
                do {
                    try {
                        listObject.add((Entity) entity.parse(source));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (source.moveToNext());
            }
        }
        IOUtils.close(source);
        return listObject;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
