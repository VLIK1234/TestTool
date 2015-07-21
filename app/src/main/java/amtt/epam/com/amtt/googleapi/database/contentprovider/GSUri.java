package amtt.epam.com.amtt.googleapi.database.contentprovider;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amtt.epam.com.amtt.googleapi.database.table.SpreadsheetTable;
import amtt.epam.com.amtt.googleapi.database.table.TestcaseTable;
import amtt.epam.com.amtt.googleapi.database.table.WorksheetTable;

/**
 * @author Iryna Monchanka
 * @version on 10.07.2015
 */

public enum GSUri {

    SPREADSHEET(SpreadsheetTable.TABLE_NAME, 1, 2),
    WORKSHEET(WorksheetTable.TABLE_NAME, 3, 4),
    TESTCASE(TestcaseTable.TABLE_NAME, 5, 6);

    private static final UriMatcher sUriMatcher;
    private static final Map<Integer, String> sContentType;
    private static final Map<GSUri, String[]> sProjections;
    private static final List<GSUri> sValues;

    private final Uri mUri;
    private final String mContentType;
    private final String mContentItemType;
    private final int mOrdinal;
    private final int mItemOrdinal;
    private final String mTableName;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(GSContentProvider.AUTHORITY, SPREADSHEET.getTableName(), GSUri.SPREADSHEET.getOrdinal());
        sUriMatcher.addURI(GSContentProvider.AUTHORITY, SPREADSHEET.getTableName() + "/#", GSUri.SPREADSHEET.getItemOrdinal());
        sUriMatcher.addURI(GSContentProvider.AUTHORITY, WORKSHEET.getTableName(), GSUri.WORKSHEET.getOrdinal());
        sUriMatcher.addURI(GSContentProvider.AUTHORITY, WORKSHEET.getTableName() + "/#", GSUri.WORKSHEET.getItemOrdinal());
        sUriMatcher.addURI(GSContentProvider.AUTHORITY, TESTCASE.getTableName(), GSUri.TESTCASE.getOrdinal());
        sUriMatcher.addURI(GSContentProvider.AUTHORITY, TESTCASE.getTableName() + "/#", GSUri.TESTCASE.getItemOrdinal());

        sContentType = new HashMap<>();
        sContentType.put(GSUri.SPREADSHEET.getOrdinal(), GSUri.SPREADSHEET.getType());
        sContentType.put(GSUri.SPREADSHEET.getItemOrdinal(), GSUri.SPREADSHEET.getItemType());
        sContentType.put(GSUri.WORKSHEET.getOrdinal(), GSUri.WORKSHEET.getType());
        sContentType.put(GSUri.WORKSHEET.getItemOrdinal(), GSUri.WORKSHEET.getItemType());
        sContentType.put(GSUri.TESTCASE.getOrdinal(), GSUri.TESTCASE.getType());
        sContentType.put(GSUri.TESTCASE.getItemOrdinal(), GSUri.TESTCASE.getItemType());

        sProjections = new HashMap<>();
        sProjections.put(GSUri.SPREADSHEET, SpreadsheetTable.PROJECTION);
        sProjections.put(GSUri.WORKSHEET, WorksheetTable.PROJECTION);
        sProjections.put(GSUri.TESTCASE, TestcaseTable.PROJECTION);

        sValues = Arrays.asList(GSUri.values());
    }

    GSUri(String tableName, int ordinal, int itemOrdinal) {
        mTableName = tableName;
        mUri = Uri.parse("content://" + GSContentProvider.AUTHORITY + "/" + tableName);
        mContentType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + GSContentProvider.AUTHORITY + "." + tableName;
        mContentItemType = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + GSContentProvider.AUTHORITY + "." + tableName;
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

    public static GSUri match(Uri uri) {
        int matchedUri = sUriMatcher.match(uri);
        for (GSUri amttUri : sValues) {
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

    public static String[] matchProjection(GSUri amttUri) {
        return sProjections.get(amttUri);
    }

}
