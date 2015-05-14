package amtt.epam.com.amtt.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import java.util.List;

import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Artsiom_Kaliaha on 13.05.2015.
 */
public abstract class AbstractDao<ObjectType extends Identifiable> implements DaoInterface<ObjectType> {

    static final Context sContext;

    static {
        sContext = ContextHolder.getContext();
    }

    @Override
    public int add(ObjectType object) throws Exception {
        ContentValues values = getAddContentValues(object);
        Uri insertedItemUri = sContext.getContentResolver().insert(getUri(), values);
        return Integer.valueOf(insertedItemUri.getLastPathSegment());
    }

    @Override
    public void remove(ObjectType object) {

    }

    @Override
    public void removeByKey(int key) {

    }

    @Override
    public void removeAll() {
        sContext.getContentResolver().delete(getUri(), null, null);
    }

    @Override
    public void update(ObjectType object) {
        ContentValues values = getUpdateContentValues(object);
        sContext.getContentResolver().update(AmttUri.USER.get(),
                values,
                BaseColumns._ID + "=?",
                new String[]{String.valueOf(object.getId())});
    }

    @Override
    public List<ObjectType> getAll() {
        return null;
    }

    @Override
    public ObjectType getByKey(int key) {
        return null;
    }


    ContentValues getAddContentValues(ObjectType object) {
        return null;
    }

    ContentValues getUpdateContentValues(ObjectType object) {
        return null;
    }

    Uri getUri() {
        return null;
    }

}
