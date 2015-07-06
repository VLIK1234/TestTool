package amtt.epam.com.amtt.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import amtt.epam.com.amtt.database.DataBaseManager;
import amtt.epam.com.amtt.database.table.ActivityInfoTable;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.database.table.StepsWithMetaTable;

/**
 @author Artsiom_Kaliaha
 @version on 23.03.2015
 */

public class AmttContentProvider extends ContentProvider {

    public static final String AUTHORITY = "amtt.epam.com.amtt.contentprovider";

    private static DataBaseManager mDataBaseManager;

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        AmttUri matchedUri = AmttUri.match(uri);
        Cursor cursor;
        //if step should be retrieved, join query is executed
        if (matchedUri == AmttUri.STEP_WITH_META) {
            String[] tablesName = {StepsTable.TABLE_NAME, ActivityInfoTable.TABLE_NAME};
            cursor = getDataBaseManager().joinQuery(tablesName,
                    StepsWithMetaTable.PROJECTION,
                    new String[]{StepsTable._ASSOCIATED_ACTIVITY, ActivityInfoTable._ACTIVITY_NAME});
        } else {
            String tableName = uri.getLastPathSegment();
            cursor = getDataBaseManager().query(tableName, projection, selection, selectionArgs, sortOrder);
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return AmttUri.matchType(uri);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (values == null) {
            return null;
        }
        String tableName = uri.getLastPathSegment();
        long id = getDataBaseManager().insert(tableName, values);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int bulkInsert(Uri uri, @NonNull ContentValues[] values) {
        String tableName = uri.getLastPathSegment();
        return getDataBaseManager().bulkInsert(tableName, values);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = uri.getPathSegments().size() > 1 ? uri.getLastPathSegment() : uri.getPathSegments().get(0);
        int deletedRows = getDataBaseManager().delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName = uri.getPathSegments().size() > 1 ? uri.getLastPathSegment() : uri.getPathSegments().get(0);
        int updatedRows = getDataBaseManager().update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRows;
    }

    private DataBaseManager getDataBaseManager() {
        if (mDataBaseManager == null) {
            mDataBaseManager = new DataBaseManager(getContext());
        }
        return mDataBaseManager;
    }

}
