package amtt.epam.com.amtt.database.dao;

import android.content.Context;
import android.net.Uri;

import java.util.List;

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

    public interface IResult<T> {
        void onResult(T result);
    }

    //TODO what bulk insert?
    /*
    * Use this method for updates. Exception won't be thrown, all the conflicts will be replaced.
    * */
    @Override
    public Integer addOrUpdate(DatabaseEntity object) {
        Uri insertedItemUri = sContext.getContentResolver().insert(object.getUri(), object.getContentValues());
        return Integer.valueOf(insertedItemUri.getLastPathSegment());
    }


    public void addOrUpdateAsync(final DatabaseEntity object, final IResult<Integer> result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                result.onResult(addOrUpdate(object));
            }
        }).start();
    }

    public int addOrUpdate(List<DatabaseEntity> object) {
        //TODO fix with error checks and use transactions
        Uri insertedItemUri = sContext.getContentResolver().insert(object.get(0).getUri(), object.get(0).getContentValues());
        return Integer.valueOf(insertedItemUri.getLastPathSegment());
    }

    public void addOrUpdateAsync(final List<DatabaseEntity> object, final IResult<Integer> result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                result.onResult(addOrUpdate(object));
            }
        }).start();
    }

    @Override
    public void remove(DatabaseEntity object) {
        sContext.getContentResolver().delete(object.getUri(), BaseColumns._ID + "?", new String[]{String.valueOf(object.getId())});
    }

    @Override
    public void removeAll(DatabaseEntity objectPrototype) {
        sContext.getContentResolver().delete(objectPrototype.getUri(), null, null);
    }

    @Override
    public List<DatabaseEntity> getAll() {
        return null;
    }

    @Override
    public DatabaseEntity getByKey(DatabaseEntity objectPrototype) {
        return null;
    }


}
