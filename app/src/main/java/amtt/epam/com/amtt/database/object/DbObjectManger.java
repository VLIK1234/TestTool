package amtt.epam.com.amtt.database.object;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
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

    public static final String SIGN_SELECTION = "=?";
    public static final String OR = " OR ";

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
    public Integer update(DatabaseEntity objectPrototype) {
        return null;
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
        return query(object, null, null, null);
    }

    @Override
    public DatabaseEntity getByKey(DatabaseEntity objectPrototype) {
        return query(objectPrototype, null, BaseColumns._ID, new String[]{String.valueOf(objectPrototype.getId())}).get(0);
    }

    public List<DatabaseEntity> query(final DatabaseEntity entity, String[] projection, String mSelection, String[] mSelectionArgs) {
        String selectionString="";

        if (mSelectionArgs.length==1) {
            selectionString = mSelection + SIGN_SELECTION;
        }else {
            for (int i = 0; i < mSelectionArgs.length; i++) {
                if (i!=mSelectionArgs.length-1) {
                    selectionString +=  mSelection + i + SIGN_SELECTION + OR;
                }else{
                    selectionString +=  mSelection + i + SIGN_SELECTION;
                }
            }
        }
        Cursor cursor = ContextHolder.getContext().getContentResolver().query(entity.getUri(), null, selectionString, mSelectionArgs, null);
        List<DatabaseEntity> listObject = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                listObject.add(new DatabaseEntity(cursor) {
                    @Override
                    public ContentValues getContentValues() {
                        return entity.getContentValues();
                    }

                    @Override
                    public Uri getUri() {
                        return entity.getUri();
                    }

                    @Override
                    public int getId() {
                        return entity.getId();
                    }
                });
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listObject;
    }
}
