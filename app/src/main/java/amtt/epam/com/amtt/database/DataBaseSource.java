package amtt.epam.com.amtt.database;

import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.IDbObjectManger;
import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.IOUtils;

/**
 * @author Iryna Monchenko
 * @version on 20.08.2015
 */
public class DataBaseSource implements DataSource<Object, Cursor>, IDbObjectManger<DatabaseEntity> {

    private static final String SIGN_SELECTION = "=?";
    private static final String SIGN_AND = " AND ";

    @Override
    public Cursor getData(Object o) throws Exception {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public <Entity extends DatabaseEntity> Integer add(Entity object) {
        Uri insertedItemUri = AmttApplication.getContext().getContentResolver().insert(object.getUri(), object.getContentValues());
        if (insertedItemUri != null) {
            return Integer.valueOf(insertedItemUri.getLastPathSegment());
        }else{
            return -1;
        }
    }

    @Override
    public <Entity extends DatabaseEntity> Integer update(Entity object, String selection, String[] selectionArgs) {
        return AmttApplication.getContext().getContentResolver().update(object.getUri(), object.getContentValues(), selection, selectionArgs);
    }

    @Override
    public <Entity extends DatabaseEntity> void remove(Entity object, Callback<Integer> result) {
        int outcome = AmttApplication.getContext().getContentResolver().delete(object.getUri(), BaseColumns._ID + "=?",
                new String[]{String.valueOf(object.getId())});
        if (result != null) {
            result.onLoadExecuted(outcome);
        }
    }

    @Override
    public <Entity extends DatabaseEntity> void removeAll(Entity objectPrototype, Callback<Integer> result) {
        int outcome = AmttApplication.getContext().getContentResolver().delete(objectPrototype.getUri(), null, null);
        if (result != null) {
            result.onLoadExecuted(outcome);
        }
    }

    @Override
    public <Entity extends DatabaseEntity> void getAll(Entity object, Callback<List<Entity>> result) {
        query(object, null, null, null, result);
    }


    public <Entity extends DatabaseEntity> void query(final Entity entity, final String[] projection, final String[] mSelection,
                                                      final String[] mSelectionArgs, final Callback<List<Entity>> result) {
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

                Cursor cursor = AmttApplication.getContext().getContentResolver().query(entity.getUri(), projection,
                        selectionString, mSelectionArgs, null);
        /*
                final List<Entity> listObject = new ArrayList<>();
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            try {
                                listObject.add((Entity) entity.parse(cursor));
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
                        result.onLoadExecuted(listObject);
                    }
                });*/

    }

    public synchronized <Entity extends DatabaseEntity> void queryDefault(final Entity entity, final String[] projection,
                                                                          final String mSelection, final String[] mSelectionArgs, final Callback<List<Entity>> result) {
        final Handler handler = new Handler();

                if (mSelectionArgs != null && mSelection != null) {
                    Cursor cursor = AmttApplication.getContext().getContentResolver().query(entity.getUri(), projection, mSelection, mSelectionArgs, null);

                    /*
                    final List<Entity> listObject = new ArrayList<>();
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                try {
                                    listObject.add((Entity) entity.parse(cursor));
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
                            result.onLoadExecuted(listObject);
                        }
                    });*/
                }
    }
}
