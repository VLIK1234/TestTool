package amtt.epam.com.amtt.contentprovider;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.database.table.IssuetypeTable;
import amtt.epam.com.amtt.database.table.PriorityTable;
import amtt.epam.com.amtt.database.table.ProjectTable;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.database.table.UsersTable;

/**
 @author Artsiom_Kaliaha
 @version on 23.03.2015
 */

public enum AmttUri {

    STEP(StepsTable.TABLE_NAME, 3, 4),
    USER(UsersTable.TABLE_NAME, 7, 8),
    PRIORITY(PriorityTable.TABLE_NAME, 9, 10),
    PROJECT(ProjectTable.TABLE_NAME, 11, 12),
    ISSUETYPE(IssuetypeTable.TABLE_NAME, 13, 14);


    private static final UriMatcher sUriMatcher;
    private final static Map<Integer, String> sContentType;

    private final Uri mUri;
    private final String mContentType;
    private final String mContentItemType;
    private final int mOrdinal;
    private final int mItemOrdinal;
    private final String mTableName;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, STEP.getTableName(), AmttUri.STEP.getOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, STEP.getTableName() + "/#", AmttUri.STEP.getItemOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, USER.getTableName(), AmttUri.USER.getOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, USER.getTableName() + "/#", AmttUri.USER.getItemOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, PRIORITY.getTableName(), AmttUri.PRIORITY.getOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, PRIORITY.getTableName() + "/#", AmttUri.PRIORITY.getItemOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, PROJECT.getTableName(), AmttUri.PROJECT.getOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, PROJECT.getTableName() + "/#", AmttUri.PROJECT.getItemOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, ISSUETYPE.getTableName(), AmttUri.ISSUETYPE.getOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, ISSUETYPE.getTableName() + "/#", AmttUri.ISSUETYPE.getItemOrdinal());

        sContentType = new HashMap<>();
        sContentType.put(AmttUri.STEP.getOrdinal(), AmttUri.STEP.getType());
        sContentType.put(AmttUri.STEP.getItemOrdinal(), AmttUri.STEP.getItemType());
        sContentType.put(AmttUri.USER.getOrdinal(), AmttUri.USER.getType());
        sContentType.put(AmttUri.USER.getItemOrdinal(), AmttUri.USER.getItemType());
        sContentType.put(AmttUri.PRIORITY.getOrdinal(), AmttUri.PRIORITY.getType());
        sContentType.put(AmttUri.PRIORITY.getItemOrdinal(), AmttUri.PRIORITY.getItemType());
        sContentType.put(AmttUri.PROJECT.getOrdinal(), AmttUri.PROJECT.getType());
        sContentType.put(AmttUri.PROJECT.getItemOrdinal(), AmttUri.PROJECT.getItemType());
        sContentType.put(AmttUri.ISSUETYPE.getOrdinal(), AmttUri.ISSUETYPE.getType());
        sContentType.put(AmttUri.ISSUETYPE.getItemOrdinal(), AmttUri.ISSUETYPE.getItemType());
    }

    AmttUri(String tableName, int ordinal, int itemOrdinal) {
        mTableName = tableName;
        mUri = Uri.parse("content://" + AmttContentProvider.AUTHORITY + "/" + tableName);
        mContentType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AmttContentProvider.AUTHORITY + "." + tableName;
        mContentItemType = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AmttContentProvider.AUTHORITY + "." + tableName;
        mOrdinal = ordinal;
        mItemOrdinal = itemOrdinal;
    }

    public Uri get() {
        return mUri;
    }

    private String getType() {
        return mContentType;
    }

    private String getItemType() {
        return mContentItemType;
    }

    private int getOrdinal() {
        return mOrdinal;
    }

    private int getItemOrdinal() {
        return mItemOrdinal;
    }

    private String getTableName() {
        return mTableName;
    }

    public static String matchType(Uri uri) {
        int uriType = sUriMatcher.match(uri);
        return sContentType.get(uriType);
    }
}
