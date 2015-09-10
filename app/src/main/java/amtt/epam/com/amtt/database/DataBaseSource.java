package amtt.epam.com.amtt.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;

import amtt.epam.com.amtt.CoreApplication;
import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbRequestParams;
import amtt.epam.com.amtt.datasource.DataSource;

/**
 * @author Iryna Monchenko
 * @version on 20.08.2015
 */

public class DataBaseSource<Entity extends DatabaseEntity, DataSourceResult> implements DataSource<DbRequestParams, DataSourceResult> {

    private ContentResolver contentResolver = CoreApplication.getContext().getContentResolver();

    @Override
    public DataSourceResult getData(DbRequestParams params) throws Exception {
        switch (params.getRequestType()) {
            case INSERT:
                return (DataSourceResult) insert((Entity) params.getObject());
            case BULK_INSERT:
                return  (DataSourceResult) bulkInsert(params.getObjectList());
            case QUERY:
                return  (DataSourceResult) query((Entity) params.getObject(), params.getProjection(), params.getSelection(), params.getSelectionArgs());
            case UPDATE:
                return  (DataSourceResult) update((Entity) params.getObject(), params.getSelection(), params.getSelectionArgs());
            case DELETE:
                return  (DataSourceResult) delete((Entity) params.getObject());
            case DELETE_ALL:
                return  (DataSourceResult) deleteAll((Entity) params.getObject());
            default:
                return null;
        }
    }

    public Integer insert(Entity object) {
        Uri insertedItemUri = contentResolver.insert(object.getUri(), object.getContentValues());
        if (insertedItemUri != null) {
            return (int) ContentUris.parseId(insertedItemUri);
        } else {
            return -1;
        }
    }

    public Integer bulkInsert(final List<Entity> objects) {
        ContentValues[] contentValues = new ContentValues[objects.size()];
        for (int i = 0; i < objects.size(); i++) {
            contentValues[i] = objects.get(i).getContentValues();
        }
        return contentResolver.bulkInsert(objects.get(0).getUri(), contentValues);
    }

    public Integer update(Entity object, String selection, String[] selectionArgs) {
        return contentResolver.update(object.getUri(), object.getContentValues(), selection, selectionArgs);
    }

    public Integer delete(Entity object) {
        return contentResolver.delete(object.getUri(), BaseColumns._ID + "=?", new String[]{String.valueOf(object.getId())});
    }

    public Integer deleteAll(Entity object) {
        return contentResolver.delete(object.getUri(), null, null);
    }

    public Cursor query(final Entity entity, final String[] projection,
                                                        final String mSelection, final String[] mSelectionArgs) {
        return contentResolver.query(entity.getUri(), projection, mSelection, mSelectionArgs, null);
    }
}
