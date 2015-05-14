package amtt.epam.com.amtt.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import amtt.epam.com.amtt.database.DataBaseManager;
import amtt.epam.com.amtt.database.table.ActivityInfoTable;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.database.table.StepsWithMetaTable;


/**
 * Created by Artsiom_Kaliaha on 23.03.2015.
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
        if (!isProjectionCorrect(matchedUri, projection)) {
            throw new IllegalArgumentException("Incorrect projection column(s)");
        }

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

    private static boolean isProjectionCorrect(AmttUri uriType, String[] projection) {
        String[] existingColumns = AmttUri.matchProjection(uriType);
        Set<String> availableProjection;
        Set<String> receivedProjection = new HashSet<>(Arrays.asList(projection));
        if (existingColumns != null) {
            availableProjection = new HashSet<>(Arrays.asList(existingColumns));
        } else {
            availableProjection = Collections.EMPTY_SET;
        }
        return availableProjection.containsAll(receivedProjection);
    }

    private DataBaseManager getDataBaseManager() {
        if (mDataBaseManager == null) {
            mDataBaseManager = new DataBaseManager(getContext());
        }
        return mDataBaseManager;
    }

}
