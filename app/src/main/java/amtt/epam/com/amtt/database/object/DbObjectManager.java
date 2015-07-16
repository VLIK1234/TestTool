package amtt.epam.com.amtt.database.object;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.IOUtils;

/**
 * @author Artsiom_Kaliaha
 * @version on 15.05.2015
 */

public enum DbObjectManager implements IDbObjectManger<DatabaseEntity> {

    INSTANCE;

    /*
    * Use this method for updates. Exception won't be thrown, all the conflicts will be replaced.
    * */

    public static final String SIGN_SELECTION = "=?";
    public static final String SIGN_AND = " AND ";


    @Override
    public Integer add(DatabaseEntity object) {
        Uri insertedItemUri = AmttApplication.getContext().getContentResolver().insert(object.getUri(), object.getContentValues());
        return Integer.valueOf(insertedItemUri.getLastPathSegment());
    }

    public int add(List<DatabaseEntity> objects) {
        ContentValues[] contentValues = new ContentValues[objects.size()];
        for (int i = 0; i < objects.size(); i++) {
            contentValues[i] = objects.get(i).getContentValues();
        }
        return AmttApplication.getContext().getContentResolver().bulkInsert(objects.get(0).getUri(),
                contentValues);
    }

    public synchronized void add(final DatabaseEntity object, final IResult<Integer> result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int outcome = add(object);
                if (result != null) {
                    result.onResult(outcome);
                }
            }
        }).start();
    }

    public synchronized void add(final List<DatabaseEntity> object, final IResult<Integer> result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int outcome = add(object);
                if (result != null) {
                    result.onResult(outcome);
                }
            }
        }).start();
    }

    @Override
    public Integer update(DatabaseEntity object, String selection, String[] selectionArgs) {
        return AmttApplication.getContext().getContentResolver().update(object.getUri(), object.getContentValues(), selection, selectionArgs);
    }

    public synchronized void update(final DatabaseEntity object, final String selection, final String[] selectionArgs, final IResult<Integer> result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int outcome = update(object, selection, selectionArgs);
                if (result != null) {
                    result.onResult(outcome);
                }
            }
        }).start();
    }

    @Override
    public void remove(final DatabaseEntity object) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AmttApplication.getContext().getContentResolver().delete(object.getUri(), BaseColumns._ID + "=?", new String[]{String.valueOf(object.getId())});
            }
        }).start();
    }

    @Override
    public void removeAll(final DatabaseEntity objectPrototype) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AmttApplication.getContext().getContentResolver().delete(objectPrototype.getUri(), null, null);
            }
        }).start();
    }

    @Override
    public void getAll(DatabaseEntity object, IResult<List<DatabaseEntity>> result) {
        query(object, null, null, null, result);
    }

    public <T extends DatabaseEntity> void query(final T entity, final String[] projection,
                                                 final String[] mSelection, final String[] mSelectionArgs, final IResult<List<T>> result) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String selectionString = Constants.Symbols.EMPTY;
                if (mSelectionArgs != null && mSelection != null) {
                    if (mSelection.length != mSelectionArgs.length) {
                        throw new IllegalStateException("Count Selection and SelectionArgs must be equals!");
                    }
                    if (mSelectionArgs.length == 1) {
                        selectionString = mSelection[0] + SIGN_SELECTION;
                    } else {
                        for (int i = 0; i < mSelectionArgs.length; i++) {
                            if (i != mSelectionArgs.length - 1) {
                                selectionString += mSelection[i] + SIGN_SELECTION + SIGN_AND;
                            } else {
                                selectionString += mSelection[i] + SIGN_SELECTION;
                            }
                        }
                    }
                }

                Cursor cursor = AmttApplication.getContext().getContentResolver().query(entity.getUri(), projection, selectionString, mSelectionArgs, null);
                final List<T> listObject = new ArrayList<>();
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            try {
                                listObject.add((T) entity.parse(cursor));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } while (cursor.moveToNext());
                    }
                }
                IOUtils.close(cursor);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        result.onResult(listObject);
                    }
                });
            }
        }).start();
    }
}
