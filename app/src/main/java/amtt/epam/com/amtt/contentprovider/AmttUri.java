package amtt.epam.com.amtt.contentprovider;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amtt.epam.com.amtt.database.table.ActivityInfoTable;
import amtt.epam.com.amtt.database.table.IssuetypeTable;
import amtt.epam.com.amtt.database.table.PriorityTable;
import amtt.epam.com.amtt.database.table.ProjectTable;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.database.table.StepsWithMetaTable;
import amtt.epam.com.amtt.database.table.UsersTable;

/**
 @author Artsiom_Kaliaha
 @version on 23.03.2015
 */

public enum AmttUri {

    ACTIVITY_META(ActivityInfoTable.TABLE_NAME, 1, 2),
    STEP(StepsTable.TABLE_NAME, 3, 4),
    STEP_WITH_META(StepsWithMetaTable.TABLE_NAME, 5, 6),
    USER(UsersTable.TABLE_NAME, 7, 8),
    PRIORITY(PriorityTable.TABLE_NAME, 9, 10),
    PROJECT(ProjectTable.TABLE_NAME, 11, 12),
    ISSUETYPE(IssuetypeTable.TABLE_NAME, 13, 14);


    private static final UriMatcher sUriMatcher;
    private final static Map<Integer, String> sContentType;
    private final static Map<AmttUri, String[]> sProjections;
    private final static List<AmttUri> sValues;

    private final Uri mUri;
    private final String mContentType;
    private final String mContentItemType;
    private final int mOrdinal;
    private final int mItemOrdinal;
    private final String mTableName;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, ACTIVITY_META.getTableName(), AmttUri.ACTIVITY_META.getOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, ACTIVITY_META.getTableName() + "/#", AmttUri.ACTIVITY_META.getItemOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, STEP.getTableName(), AmttUri.STEP.getOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, STEP.getTableName() + "/#", AmttUri.STEP.getItemOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, STEP_WITH_META.getTableName(), AmttUri.STEP_WITH_META.getOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, STEP_WITH_META.getTableName() + "/#", AmttUri.STEP_WITH_META.getItemOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, USER.getTableName(), AmttUri.USER.getOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, USER.getTableName() + "/#", AmttUri.USER.getItemOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, PRIORITY.getTableName(), AmttUri.PRIORITY.getOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, PRIORITY.getTableName() + "/#", AmttUri.PRIORITY.getItemOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, PROJECT.getTableName(), AmttUri.PROJECT.getOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, PROJECT.getTableName() + "/#", AmttUri.PROJECT.getItemOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, ISSUETYPE.getTableName(), AmttUri.ISSUETYPE.getOrdinal());
        sUriMatcher.addURI(AmttContentProvider.AUTHORITY, ISSUETYPE.getTableName() + "/#", AmttUri.ISSUETYPE.getItemOrdinal());

        sContentType = new HashMap<>();
        sContentType.put(AmttUri.ACTIVITY_META.getOrdinal(), AmttUri.ACTIVITY_META.getType());
        sContentType.put(AmttUri.ACTIVITY_META.getItemOrdinal(), AmttUri.ACTIVITY_META.getItemType());
        sContentType.put(AmttUri.STEP.getOrdinal(), AmttUri.STEP.getType());
        sContentType.put(AmttUri.STEP.getItemOrdinal(), AmttUri.STEP.getItemType());
        sContentType.put(AmttUri.STEP_WITH_META.getOrdinal(), AmttUri.STEP_WITH_META.getType());
        sContentType.put(AmttUri.STEP_WITH_META.getItemOrdinal(), AmttUri.STEP_WITH_META.getItemType());
        sContentType.put(AmttUri.USER.getOrdinal(), AmttUri.USER.getType());
        sContentType.put(AmttUri.USER.getItemOrdinal(), AmttUri.USER.getItemType());
        sContentType.put(AmttUri.PRIORITY.getOrdinal(), AmttUri.PRIORITY.getType());
        sContentType.put(AmttUri.PRIORITY.getItemOrdinal(), AmttUri.PRIORITY.getItemType());
        sContentType.put(AmttUri.PROJECT.getOrdinal(), AmttUri.PROJECT.getType());
        sContentType.put(AmttUri.PROJECT.getItemOrdinal(), AmttUri.PROJECT.getItemType());
        sContentType.put(AmttUri.ISSUETYPE.getOrdinal(), AmttUri.ISSUETYPE.getType());
        sContentType.put(AmttUri.ISSUETYPE.getItemOrdinal(), AmttUri.ISSUETYPE.getItemType());

        sProjections = new HashMap<>();
        sProjections.put(AmttUri.ACTIVITY_META, ActivityInfoTable.PROJECTION);
        sProjections.put(AmttUri.STEP, StepsTable.PROJECTION);
        sProjections.put(AmttUri.STEP_WITH_META, StepsWithMetaTable.PROJECTION);
        sProjections.put(AmttUri.USER, UsersTable.PROJECTION);
        sProjections.put(AmttUri.PRIORITY, PriorityTable.PROJECTION);
        sProjections.put(AmttUri.PROJECT, ProjectTable.PROJECTION);
        sProjections.put(AmttUri.ISSUETYPE, IssuetypeTable.PROJECTION);

        sValues = Arrays.asList(AmttUri.values());
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

    public String getType() {
        return mContentType;
    }

    public String getItemType() {
        return mContentItemType;
    }

    public int getOrdinal() {
        return mOrdinal;
    }

    public int getItemOrdinal() {
        return mItemOrdinal;
    }

    public String getTableName() {
        return mTableName;
    }

    public static AmttUri match(Uri uri) {
        int matchedUri = sUriMatcher.match(uri);
        for (AmttUri amttUri : sValues) {
            if (amttUri.getOrdinal() == matchedUri || amttUri.getItemOrdinal() == matchedUri) {
                return amttUri;
            }
        }
        return null;
    }

    public static String matchType(Uri uri) {
        int uriType = sUriMatcher.match(uri);
        return sContentType.get(uriType);
    }
}
