package amtt.epam.com.amtt.database;

import android.database.Cursor;

import java.util.List;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.common.DataRequest;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbRequestParams;
import amtt.epam.com.amtt.database.processing.BulkInsertProcessor;
import amtt.epam.com.amtt.database.processing.DeleteProcessor;
import amtt.epam.com.amtt.database.processing.InsertProcessor;
import amtt.epam.com.amtt.database.processing.QueryProcessor;
import amtt.epam.com.amtt.database.processing.UpdateProcessor;

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
        AmttApplication.registerPlugin(new DataBaseSource<Entity, Integer>());
        AmttApplication.registerPlugin(new InsertProcessor());
        requestParams = new DbRequestParams<Entity>(object, DbRequestType.INSERT);
        execute(requestParams, InsertProcessor.NAME, callback);
    }

    public void bulkInsert(List<Entity> objects, Callback<Integer> callback) {
        AmttApplication.registerPlugin(new DataBaseSource<Entity, Integer>());
        AmttApplication.registerPlugin(new BulkInsertProcessor());
        requestParams = new DbRequestParams<Entity>(objects, DbRequestType.BULK_INSERT);
        execute(requestParams, BulkInsertProcessor.NAME, callback);
    }

    public void query(Entity entity, String[] projection, String mSelection, String[] mSelectionArgs,
                      String sortOrder, Callback<List<Entity>> callback) {
        AmttApplication.registerPlugin(new DataBaseSource<Entity, Cursor>());
        AmttApplication.registerPlugin(new QueryProcessor<Entity>());
        requestParams = new DbRequestParams<Entity>(entity, projection, mSelection, mSelectionArgs,
                                                    sortOrder, DbRequestType.QUERY);
        execute(requestParams, QueryProcessor.NAME, callback);
    }

    public void update(Entity object, String selection, String[] selectionArgs, Callback<Integer> callback) {
        AmttApplication.registerPlugin(new DataBaseSource<Entity, Integer>());
        AmttApplication.registerPlugin(new UpdateProcessor());
        requestParams = new DbRequestParams<Entity>(object, selection, selectionArgs, DbRequestType.UPDATE);
        execute(requestParams, UpdateProcessor.NAME, callback);
    }

    public void delete(Entity object, Callback<Integer> callback) {
        AmttApplication.registerPlugin(new DataBaseSource<Entity, Integer>());
        AmttApplication.registerPlugin(new DeleteProcessor());
        requestParams = new DbRequestParams<Entity>(object, DbRequestType.DELETE);
        execute(requestParams, DeleteProcessor.NAME, callback);
    }

    private void execute(DbRequestParams params, String processorName, Callback callback) {
        AmttApplication.executeRequest(new DataRequest<>(DataBaseSource.NAME, params, processorName, callback));
    }
}
