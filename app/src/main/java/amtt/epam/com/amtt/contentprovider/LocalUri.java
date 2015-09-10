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

public enum LocalUri {

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
        sUriMatcher.addURI(LocalContentProvider.AUTHORITY, STEP.getTableName(), LocalUri.STEP.getOrdinal());
        sUriMatcher.addURI(LocalContentProvider.AUTHORITY, STEP.getTableName() + "/#", LocalUri.STEP.getItemOrdinal());
        sUriMatcher.addURI(LocalContentProvider.AUTHORITY, USER.getTableName(), LocalUri.USER.getOrdinal());
        sUriMatcher.addURI(LocalContentProvider.AUTHORITY, USER.getTableName() + "/#", LocalUri.USER.getItemOrdinal());
        sUriMatcher.addURI(LocalContentProvider.AUTHORITY, PRIORITY.getTableName(), LocalUri.PRIORITY.getOrdinal());
        sUriMatcher.addURI(LocalContentProvider.AUTHORITY, PRIORITY.getTableName() + "/#", LocalUri.PRIORITY.getItemOrdinal());
        sUriMatcher.addURI(LocalContentProvider.AUTHORITY, PROJECT.getTableName(), LocalUri.PROJECT.getOrdinal());
        sUriMatcher.addURI(LocalContentProvider.AUTHORITY, PROJECT.getTableName() + "/#", LocalUri.PROJECT.getItemOrdinal());
        sUriMatcher.addURI(LocalContentProvider.AUTHORITY, ISSUETYPE.getTableName(), LocalUri.ISSUETYPE.getOrdinal());
        sUriMatcher.addURI(LocalContentProvider.AUTHORITY, ISSUETYPE.getTableName() + "/#", LocalUri.ISSUETYPE.getItemOrdinal());

        sContentType = new HashMap<>();
        sContentType.put(LocalUri.STEP.getOrdinal(), LocalUri.STEP.getType());
        sContentType.put(LocalUri.STEP.getItemOrdinal(), LocalUri.STEP.getItemType());
        sContentType.put(LocalUri.USER.getOrdinal(), LocalUri.USER.getType());
        sContentType.put(LocalUri.USER.getItemOrdinal(), LocalUri.USER.getItemType());
        sContentType.put(LocalUri.PRIORITY.getOrdinal(), LocalUri.PRIORITY.getType());
        sContentType.put(LocalUri.PRIORITY.getItemOrdinal(), LocalUri.PRIORITY.getItemType());
        sContentType.put(LocalUri.PROJECT.getOrdinal(), LocalUri.PROJECT.getType());
        sContentType.put(LocalUri.PROJECT.getItemOrdinal(), LocalUri.PROJECT.getItemType());
        sContentType.put(LocalUri.ISSUETYPE.getOrdinal(), LocalUri.ISSUETYPE.getType());
        sContentType.put(LocalUri.ISSUETYPE.getItemOrdinal(), LocalUri.ISSUETYPE.getItemType());
    }

    LocalUri(String tableName, int ordinal, int itemOrdinal) {
        mTableName = tableName;
        mUri = Uri.parse("content://" + LocalContentProvider.AUTHORITY + "/" + tableName);
        mContentType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + LocalContentProvider.AUTHORITY + "." + tableName;
        mContentItemType = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + LocalContentProvider.AUTHORITY + "." + tableName;
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
