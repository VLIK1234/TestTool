package amtt.epam.com.amtt.excel.bo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.excel.database.contentprovider.GSUri;
import amtt.epam.com.amtt.excel.database.table.WorksheetTable;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */

public class GoogleWorksheet extends GoogleSheet<GoogleWorksheet> {

    private int mId;
    private String mLabels;
    private List<GoogleEntryWorksheet> mEntry;

    public GoogleWorksheet() {
    }

    public GoogleWorksheet(Cursor cursor) {
        if (cursor.getPosition() == -1) {
            cursor.moveToNext();
        }
        mId = cursor.getInt(cursor.getColumnIndex(WorksheetTable._ID));
        mIdLink = cursor.getString(cursor.getColumnIndex(WorksheetTable._WORKSHEET_ID_LINK));
        mUpdated = cursor.getString(cursor.getColumnIndex(WorksheetTable._UPDATED));
        mTitle = cursor.getString(cursor.getColumnIndex(WorksheetTable._TITLE));
        mOpenSearchTotalResults = Integer.parseInt(cursor.getString(cursor.getColumnIndex(WorksheetTable._TOTAL_RESULTS)));
        mOpenSearchStartIndex = Integer.parseInt(cursor.getString(cursor.getColumnIndex(WorksheetTable._START_INDEX)));
        mLabels = cursor.getString(cursor.getColumnIndex(WorksheetTable._LABELS));
    }

    @Override
    public GoogleWorksheet parse(Cursor cursor) {
        return null;
    }

    public GoogleWorksheet(List<GoogleEntryWorksheet> entry) {
        this.mEntry = entry;
    }

    public GoogleWorksheet(String idLink, String updated, String title, GoogleLink selfLink,
                           GoogleLink alternateLink, GoogleLink feedLink, GoogleLink postLink,
                           GoogleAuthor author, int openSearchTotalResults, int openSearchStartIndex,
                           List<GoogleEntryWorksheet> entry) {
        super(idLink, updated, title, selfLink, alternateLink, feedLink, postLink, author,
                openSearchTotalResults, openSearchStartIndex);
        this.mEntry = entry;
    }

    public List<GoogleEntryWorksheet> getEntry() {
        return mEntry;
    }

    public void setEntry(List<GoogleEntryWorksheet> entry) {
        this.mEntry = entry;
    }

    public void setEntryItem(GoogleEntryWorksheet googleEntryWorksheet) {
        if (mEntry == null) {
            mEntry = new ArrayList<>();
        }
        mEntry.add(googleEntryWorksheet);
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public Uri getUri() {
        return GSUri.WORKSHEET.get();
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(WorksheetTable._WORKSHEET_ID_LINK, mIdLink);
        values.put(WorksheetTable._UPDATED, mUpdated);
        values.put(WorksheetTable._TITLE, mTitle);
        values.put(WorksheetTable._TOTAL_RESULTS, mOpenSearchTotalResults);
        values.put(WorksheetTable._START_INDEX, mOpenSearchStartIndex);
        values.put(WorksheetTable._LABELS, mLabels);
        return values;
    }
}
