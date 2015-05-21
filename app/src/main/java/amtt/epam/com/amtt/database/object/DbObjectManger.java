package amtt.epam.com.amtt.database.object;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.List;

import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Artsiom_Kaliaha on 15.05.2015.
 */
public enum DbObjectManger implements IDbObjectManger<DatabaseEntity> {

    INSTANCE;

    /*
    * Use this method for updates. Exception won't be thrown, all the conflicts will be replaced.
    * */

    @Override
    public Integer addOrUpdate(DatabaseEntity object) {
        Uri insertedItemUri = ContextHolder.getContext().getContentResolver().insert(object.getUri(), object.getContentValues());
        return Integer.valueOf(insertedItemUri.getLastPathSegment());
    }

    public int addOrUpdate(List<DatabaseEntity> objects) {
        ContentValues[] contentValues = new ContentValues[objects.size()];
        for(int i = 0; i < objects.size(); i++) {
            contentValues[i] = objects.get(i).getContentValues();
        }
        return ContextHolder.getContext().getContentResolver().bulkInsert(objects.get(0).getUri(), contentValues);
    }

    public void addOrUpdateAsync(final DatabaseEntity object, final IResult<Integer> result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (result!=null) {
                    result.onResult(addOrUpdate(object));
                }
            }
        }).start();
    }

    public void addOrUpdateAsync(final List<DatabaseEntity> object, final IResult<Integer> result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (result != null) {
                    result.onResult(addOrUpdate(object));
                }
            }
        }).start();
    }

    @Override
    public void remove(DatabaseEntity object) {
        ContextHolder.getContext().getContentResolver().delete(object.getUri(), BaseColumns._ID + "?", new String[]{String.valueOf(object.getId())});
    }

    @Override
    public void removeAll(DatabaseEntity objectPrototype) {
        ContextHolder.getContext().getContentResolver().delete(objectPrototype.getUri(), null, null);
    }

    @Override
    public List<DatabaseEntity> getAll(DatabaseEntity object) {
        Cursor c = ContextHolder.getContext().getContentResolver().query(object.getUri(), null, null, null, null);
        return null;
    }

    @Override
    public DatabaseEntity getByKey(DatabaseEntity objectPrototype) {
        return null;
    }

    public Cursor query(final DatabaseEntity objcet, final String mSelection, final String[] mSelectionArgs, IResult<DatabaseEntity> result){
        final Cursor[] cursor = new Cursor[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                cursor[0] = ContextHolder.getContext().getContentResolver().query(objcet.getUri(), null, mSelection + "=?", mSelectionArgs, null);
            }
        });
        return cursor[0];
    }
}
