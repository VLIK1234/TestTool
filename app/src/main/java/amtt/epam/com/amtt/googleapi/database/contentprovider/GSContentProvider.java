package amtt.epam.com.amtt.googleapi.database.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import amtt.epam.com.amtt.googleapi.database.DataBaseManager;

/**
 * @author Iryna Monchanka
 * @version on 10.07.2015
 */

public class GSContentProvider extends ContentProvider {

    public static final String AUTHORITY = "amtt.epam.com.amtt.googleapi.database.contentprovider";

    private static DataBaseManager mDataBaseManager;

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        String tableName = uri.getLastPathSegment();
        cursor = getDataBaseManager().query(tableName, projection, selection, selectionArgs, sortOrder);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return GSUri.matchType(uri);
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (values == null) {
            return null;
        }
        String tableName = uri.getLastPathSegment();
        long id = getDataBaseManager().insert(tableName, values);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        String tableName = uri.getLastPathSegment();
        return getDataBaseManager().bulkInsert(tableName, values);
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        String tableName = uri.getPathSegments().size() > 1 ? uri.getLastPathSegment() : uri.getPathSegments().get(0);
        int deletedRows = getDataBaseManager().delete(tableName, selection, selectionArgs);
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName = uri.getPathSegments().size() > 1 ? uri.getLastPathSegment() : uri.getPathSegments().get(0);
        int updatedRows = getDataBaseManager().update(tableName, values, selection, selectionArgs);
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updatedRows;
    }

    private DataBaseManager getDataBaseManager() {
        if (mDataBaseManager == null) {
            mDataBaseManager = new DataBaseManager(getContext());
        }
        return mDataBaseManager;
    }

}
