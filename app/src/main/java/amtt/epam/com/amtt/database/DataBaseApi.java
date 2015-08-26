package amtt.epam.com.amtt.database;

import android.database.Cursor;

import java.util.List;

import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.common.DataRequest;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbRequestParams;
import amtt.epam.com.amtt.database.processing.ReadDbProcessor;
import amtt.epam.com.amtt.database.processing.UpdateDbProcessor;
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
        ThreadManager.registerPlugin(new DataBaseSource<Entity, Integer>());
        ThreadManager.registerPlugin(new UpdateDbProcessor());
        requestParams = new DbRequestParams<Entity>(object, DbRequestType.INSERT);
        execute(requestParams, UpdateDbProcessor.NAME, callback);
    }

    public void bulkInsert(List<Entity> objects, Callback<Integer> callback) {
        ThreadManager.registerPlugin(new DataBaseSource<Entity, Integer>());
        ThreadManager.registerPlugin(new UpdateDbProcessor());
        requestParams = new DbRequestParams<Entity>(objects, DbRequestType.BULK_INSERT);
        execute(requestParams, UpdateDbProcessor.NAME, callback);
    }

    public void query(Entity entity, String[] projection, String mSelection, String[] mSelectionArgs,
                      String sortOrder, Callback<List<Entity>> callback) {
        ThreadManager.registerPlugin(new DataBaseSource<Entity, Cursor>());
        ThreadManager.registerPlugin(new ReadDbProcessor<Entity>((Class<Entity>) entity.getClass()));
        requestParams = new DbRequestParams<Entity>(entity, projection, mSelection, mSelectionArgs,
                                                    sortOrder, DbRequestType.QUERY);
        execute(requestParams, ReadDbProcessor.NAME, callback);
    }

    public void update(Entity object, String selection, String[] selectionArgs, Callback<Integer> callback) {
        ThreadManager.registerPlugin(new DataBaseSource<Entity, Integer>());
        ThreadManager.registerPlugin(new UpdateDbProcessor());
        requestParams = new DbRequestParams<Entity>(object, selection, selectionArgs, DbRequestType.UPDATE);
        execute(requestParams, UpdateDbProcessor.NAME, callback);
    }

    public void delete(Entity object, Callback<Integer> callback) {
        ThreadManager.registerPlugin(new DataBaseSource<Entity, Integer>());
        ThreadManager.registerPlugin(new UpdateDbProcessor());
        requestParams = new DbRequestParams<Entity>(object, DbRequestType.DELETE);
        execute(requestParams, UpdateDbProcessor.NAME, callback);
    }

    private void execute(DbRequestParams params, String processorName, Callback callback) {
        ThreadManager.executeRequest(new DataRequest<>(DataBaseSource.NAME, params, processorName, callback));
    }
}
