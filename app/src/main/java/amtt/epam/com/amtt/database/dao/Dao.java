package amtt.epam.com.amtt.database.dao;

import android.content.Context;
import android.net.Uri;

import java.util.List;

import amtt.epam.com.amtt.bo.database.DatabaseEntity;
import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Artsiom_Kaliaha on 15.05.2015.
 */
public class Dao implements DaoInterface<DatabaseEntity> {

    static final Context sContext;

    static {
        sContext = ContextHolder.getContext();
    }

    /*
    * Use this method for updates. Exception won't be thrown, all the conflicts will be replaced.
    * */
    @Override
    public int addOrUpdate(DatabaseEntity object) throws Exception {
        Uri insertedStepUri = sContext.getContentResolver().insert(object.getUri(), object.getContentValues());
        return Integer.valueOf(insertedStepUri.getLastPathSegment());
    }

    @Override
    public void remove(DatabaseEntity object) {
        sContext.getContentResolver().delete(object.getUri(), BaseColumns._ID + "?", new String[]{String.valueOf(object.getId())});
    }

    @Override
    public void removeAll(DatabaseEntity prototypeObject) {
        sContext.getContentResolver().delete(prototypeObject.getUri(), null, null);
    }

    @Override
    public List<DatabaseEntity> getAll() {
        return null;
    }

    /*
    * Create new object in order to obtain Id and Uri
    * */
    @Override
    public DatabaseEntity getByKey(DatabaseEntity prototypeObject) {
        sContext.getContentResolver().delete(prototypeObject.getUri(), BaseColumns._ID + "?", new String[]{String.valueOf(prototypeObject.getId())});
        return null;
    }

}
