package amtt.epam.com.amtt.googleapi.bo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.googleapi.database.contentprovider.GSUri;
import amtt.epam.com.amtt.googleapi.database.table.TagsTable;

/**
 * @author Iryna Monchenko
 * @version on 20.07.2015
 */
public class GTag extends DatabaseEntity<GTag>{

    private int mId;
    private String mIdLinkTestCase;
    private String mIdLinkSpreadsheet;
    private String mName;

    public GTag() {
    }

    public GTag(Cursor cursor) {
        if (cursor.getPosition() == -1) {
            cursor.moveToNext();
        }
        mId = cursor.getInt(cursor.getColumnIndex(TagsTable._ID));
        mIdLinkTestCase = cursor.getString(cursor.getColumnIndex(TagsTable._TESTCASE_ID_LINK));
        mName = cursor.getString(cursor.getColumnIndex(TagsTable._NAME));
        mIdLinkSpreadsheet = cursor.getString(cursor.getColumnIndex(TagsTable._SPREADSHEET_ID_LINK));
    }

    @Override
    public GTag parse(Cursor cursor) {
        return new GTag(cursor);
    }

    public GTag(int id, String idLinkTestCase, String name) {
        this.mId = id;
        this.mIdLinkTestCase = idLinkTestCase;
        this.mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getIdLinkTestCase() {
        return mIdLinkTestCase;
    }

    public void setIdLinkTestCase(String idLinkTestCase) {
        this.mIdLinkTestCase = idLinkTestCase;
    }

    public String getIdLinkSpreadsheet() {
        return mIdLinkSpreadsheet;
    }

    public void setIdLinkSpreadsheet(String idLinkSpreadsheet) {
        mIdLinkSpreadsheet = idLinkSpreadsheet;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public Uri getUri() {
        return GSUri.TAGS.get();
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(TagsTable._TESTCASE_ID_LINK, mIdLinkTestCase);
        values.put(TagsTable._NAME, mName);
        values.put(TagsTable._SPREADSHEET_ID_LINK, mIdLinkSpreadsheet);
        return values;
    }
}

