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

public class ReadDbProcessor<Entity extends DatabaseEntity> implements Processor<Cursor, List<Entity>> {

    public static final String NAME = ReadDbProcessor.class.getName();

    private Class<Entity> mEntityClass;

    public ReadDbProcessor(Class<Entity> entityClass){
        mEntityClass = entityClass;
    }


    @Override
    public List<Entity> process(Cursor source) throws Exception {
        final List<Entity> listObject = new ArrayList<Entity>();
        if (source != null) {
            if (source.moveToFirst()) {
                do {
                    try {
                        listObject.add((Entity) mEntityClass.newInstance().parse(source));
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
    public String getPluginName() {
        return NAME;
    }
}
