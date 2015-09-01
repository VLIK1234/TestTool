package amtt.epam.com.amtt.database;

import android.database.Cursor;

import java.util.List;

import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbRequestParams;
import amtt.epam.com.amtt.database.processing.ReadDbProcessor;
import amtt.epam.com.amtt.database.processing.UpdateDbProcessor;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.ThreadManager;

/**
 * @author Iryna Monchenko
 * @version on 21.08.2015
 */

public class DataBaseApi<Entity extends DatabaseEntity> {

    private static volatile DataBaseApi INSTANCE;
    private DbRequestParams<Entity> requestParams;

    public static DataBaseApi getInstance() {
        DataBaseApi localInstance = INSTANCE;
        if (localInstance == null) {
            synchronized (DataBaseApi.class) {
                localInstance = INSTANCE;
                if (localInstance == null) {
                    INSTANCE = localInstance = new DataBaseApi();
                }
            }
        }
        return localInstance;
    }

    public void insert(Entity object, Callback<Integer> callback) {
        requestParams = new DbRequestParams<>(object, DbRequestType.INSERT);
        execute(requestParams, new DataBaseSource<Entity, Integer>(), new UpdateDbProcessor(), callback);
    }

    public void bulkInsert(List<Entity> objects, Callback<Integer> callback) {
        requestParams = new DbRequestParams<>(objects, DbRequestType.BULK_INSERT);
        execute(requestParams, new DataBaseSource<Entity, Integer>(),
                new UpdateDbProcessor(), callback);
    }

    public void query(Entity entity, String[] projection, String mSelection, String[] mSelectionArgs,
                      String sortOrder, Callback<List<Entity>> callback) {
        requestParams = new DbRequestParams<>(entity, projection, mSelection, mSelectionArgs,
                                                    sortOrder, DbRequestType.QUERY);
        execute(requestParams,new DataBaseSource<Entity, Cursor>(),
                new ReadDbProcessor<>((Class<Entity>) entity.getClass()), callback);
    }

    public void update(Entity object, String selection, String[] selectionArgs, Callback<Integer> callback) {
        requestParams = new DbRequestParams<>(object, selection, selectionArgs, DbRequestType.UPDATE);
        execute(requestParams, new DataBaseSource<Entity, Integer>(), new UpdateDbProcessor(), callback);
    }

    public void delete(Entity object, Callback<Integer> callback) {
        requestParams = new DbRequestParams<>(object, DbRequestType.DELETE);
        execute(requestParams, new DataBaseSource<Entity, Integer>(), new UpdateDbProcessor(), callback);
    }

    private void execute(DbRequestParams params, DataBaseSource datasourse, Processor processor, Callback callback) {
        ThreadManager.execute(params, datasourse, processor, callback);
    }
}
