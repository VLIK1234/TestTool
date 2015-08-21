package amtt.epam.com.amtt.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbRequestParams;
import amtt.epam.com.amtt.datasource.DataSource;

/**
 * @author Iryna Monchenko
 * @version on 20.08.2015
 */

public class DataBaseSource<T> implements DataSource<DbRequestParams, T> {

    public static final String NAME = DataBaseSource.class.getName();
    private ContentResolver contentResolver = AmttApplication.getContext().getContentResolver();


    @Override
    public T getData(DbRequestParams params) throws Exception {
        switch (params.getRequestType()) {
            case INSERT:
                return (T) insert(params.getObject());
            case BULK_INSERT:
                return (T) bulkInsert(params.getObjectList());
            case QUERY:
                return (T) query(params.getObject(), params.getProjection(), params.getSelection(), params.getSelectionArgs());
            case UPDATE:
                return (T) update(params.getObject(), params.getSelection(), params.getSelectionArgs());
            case DELETE:
                return (T) delete(params.getObject());
            default:
                return null;
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    public <Entity extends DatabaseEntity> Integer insert(Entity object) {
        Uri insertedItemUri = contentResolver.insert(object.getUri(), object.getContentValues());
        if (insertedItemUri != null) {
            return Integer.valueOf(insertedItemUri.getLastPathSegment());
        } else {
            return -1;
        }
    }

    public <Entity extends DatabaseEntity> Integer bulkInsert(final List<Entity> objects) {
        ContentValues[] contentValues = new ContentValues[objects.size()];
        for (int i = 0; i < objects.size(); i++) {
            contentValues[i] = objects.get(i).getContentValues();
        }
        return contentResolver.bulkInsert(objects.get(0).getUri(), contentValues);
    }

    public <Entity extends DatabaseEntity> Integer update(Entity object, String selection, String[] selectionArgs) {
        return contentResolver.update(object.getUri(), object.getContentValues(), selection, selectionArgs);
    }

    public <Entity extends DatabaseEntity> Integer delete(Entity object) {
        return contentResolver.delete(object.getUri(), BaseColumns._ID + "=?", new String[]{String.valueOf(object.getId())});
    }

    public <Entity extends DatabaseEntity> Cursor query(final Entity entity, final String[] projection,
                                                        final String mSelection, final String[] mSelectionArgs) {
        return contentResolver.query(entity.getUri(), projection, mSelection, mSelectionArgs, null);
    }
}
