package amtt.epam.com.amtt.contentprovider;

import amtt.epam.com.amtt.database.ActivityInfoTable;
import amtt.epam.com.amtt.database.DataBaseManager;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.util.*;

/**
 * Created by Artsiom_Kaliaha on 23.03.2015.
 */
public class AmttContentProvider extends ContentProvider {

    private static final String AUTHORITY = "amtt.epam.com.amtt.contentprovider";

    public static final Uri ACTIVITY_META_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ActivityInfoTable.TABLE_NAME);

    public static final String ACTIVITY_META_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY + "." + ActivityInfoTable.TABLE_NAME;
    public static final String ACTIVITY_META_CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY + "." + ActivityInfoTable.TABLE_NAME;

    private static final UriMatcher sUriMatcher;
    private static Map<Integer, String> sContentType;
    private static Map<Integer, String[]> sProjections;
    private static DataBaseManager mDataBaseManager;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, ACTIVITY_META_CONTENT_TYPE, AmttUri.ACTIVITY_META.ordinal());
        sUriMatcher.addURI(AUTHORITY, ACTIVITY_META_CONTENT_ITEM_TYPE + "/#", AmttUri.ACTIVITY_META_BY_NAME.ordinal());

        sContentType = new HashMap<>();
        sContentType.put(AmttUri.ACTIVITY_META.ordinal(), ACTIVITY_META_CONTENT_TYPE);
        sContentType.put(AmttUri.ACTIVITY_META_BY_NAME.ordinal(), ACTIVITY_META_CONTENT_ITEM_TYPE);

        sProjections = new HashMap<>();
        sProjections.put(AmttUri.ACTIVITY_META.ordinal(), ActivityInfoTable.PROJECTION);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (!isProjectionCorrect(sUriMatcher.match(uri), projection)) {
            throw new IllegalArgumentException("Incorrect projection column(s)");
        }

        if (TextUtils.isEmpty(selection)) {
            //one row for querying is defined in SELECTION
            if (uri.getPathSegments().size() > 1) {
                //one row for querying is defined in URI
                //getContentResolver.delete(AmttContentProvider.ACTIVITY_META_CONTENT_URI
                // .buildUpon()
                // .appendPath(String.valueOf(ActivityInfoTable._ACTIVITY_NAME))
                // .build(), null, null);
                String activityName = uri.getLastPathSegment();
                selection = ActivityInfoTable._ACTIVITY_NAME + "=" + activityName;
            }
        }

        String tableName = uri.getPath();
        String activityName = uri.getLastPathSegment();
        return getDataBaseManager().query(tableName, activityName, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        int uriType = sUriMatcher.match(uri);
        return sContentType.get(uriType);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = uri.getLastPathSegment();
        long id = getDataBaseManager().insert(tableName, values);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (TextUtils.isEmpty(selection)) {
            //one row for deleting is defined in SELECTION
            if (uri.getPathSegments().size() > 1) {
                //one row for deleting is defined in URI
                //getContentResolver.delete(AmttContentProvider.ACTIVITY_META_CONTENT_URI
                // .buildUpon()
                // .appendPath(String.valueOf(ActivityInfoTable._ACTIVITY_NAME))
                // .build(), null, null);
                String activityName = uri.getLastPathSegment();
                selection = ActivityInfoTable._ACTIVITY_NAME + "=" + activityName;
            }
        }

        String tableName = uri.getPathSegments().size() > 1 ? uri.getLastPathSegment() : uri.getPathSegments().get(0);
        int deletedRows = getDataBaseManager().delete(tableName, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (TextUtils.isEmpty(selection)) {
            //one row for updating is defined in SELECTION
            if (uri.getPathSegments().size() > 1) {
                //one row for updating is defined in URI
                //getContentResolver.delete(AmttContentProvider.ACTIVITY_META_CONTENT_URI
                // .buildUpon()
                // .appendPath(String.valueOf(ActivityInfoTable._ACTIVITY_NAME))
                // .build(), null, null);
                String activityName = uri.getLastPathSegment();
                selection = ActivityInfoTable._ACTIVITY_NAME + "=" + activityName;
            }
        }

        String tableName = uri.getPathSegments().size() > 1 ? uri.getLastPathSegment() : uri.getPathSegments().get(0);
        int updatedRows = getDataBaseManager().update(tableName, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRows;
    }

    private static boolean isProjectionCorrect(int uriType, String[] projection) {
        String[] existingColumns = sProjections.get(uriType);
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
