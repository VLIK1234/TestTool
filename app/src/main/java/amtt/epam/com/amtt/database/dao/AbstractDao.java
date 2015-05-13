package amtt.epam.com.amtt.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import java.util.List;

import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Artsiom_Kaliaha on 13.05.2015.
 */
public abstract class AbstractDao<ObjectType extends Identifiable> implements DaoInterface<ObjectType> {

    static final Context sContext;
    Uri[] mUris;

    static {
        sContext = ContextHolder.getContext();
    }

    @Override
    public int add(ObjectType object) throws Exception {
        addExtra(object);
        List<ContentValues> valuesArray = getAddValues(object);
        Uri insertedItemUri;
        for(int i = 0; i < mUris.length; i++) {
            insertedItemUri = sContext.getContentResolver().insert(mUris[i], valuesArray.get(i));
            if (i == mUris.length - 1) {
                //return the last inserted item's Uri
                return Integer.valueOf(insertedItemUri.getLastPathSegment());
            }
        }
        return 0;
    }

    @Override
    public void remove(ObjectType object) throws Exception {

    }

    @Override
    public void removeByKey(int key) throws Exception {

    }

    @Override
    public void removeAll() throws Exception {
        for (Uri uri : mUris) {
            sContext.getContentResolver().delete(uri, null, null);
        }
        removeAllExtra();
    }

    @Override
    public void update(ObjectType object) throws Exception {
        ContentValues values = getUpdateValues(object);
        sContext.getContentResolver().update(AmttUri.USER.get(),
                values,
                BaseColumns._ID + "=?",
                new String[]{ String.valueOf(object.getId()) });
    }

    @Override
    public List<ObjectType> getAll() throws Exception {
        return null;
    }

    @Override
    public ObjectType getByKey(int key) throws Exception {
        return null;
    }


    abstract List<ContentValues> getAddValues(ObjectType object) throws Exception;

    abstract ContentValues getUpdateValues(ObjectType object) throws Exception;

    /*
    * method should be implemented in case additional actions during data saving takes place
    * e.g. saving activity info to data base and screen to storage
    * */
    abstract void addExtra(ObjectType object) throws Exception;

    /*
    * the same approach but for removing all the entries
    * */
    abstract void removeAllExtra() throws Exception;

}
