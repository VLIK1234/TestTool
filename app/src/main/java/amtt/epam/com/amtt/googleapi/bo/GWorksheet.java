package amtt.epam.com.amtt.googleapi.bo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.googleapi.database.contentprovider.GSUri;
import amtt.epam.com.amtt.googleapi.database.table.WorksheetTable;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */

public class GWorksheet extends GSheet<GWorksheet> {

    private int mId;
    private String mSpreadsheetIdLink;
    private List<GEntryWorksheet> mEntry;

    public GWorksheet() {
    }

    public GWorksheet(Cursor cursor) {
        if (cursor.getPosition() == -1) {
            cursor.moveToNext();
        }
        mId = cursor.getInt(cursor.getColumnIndex(WorksheetTable._ID));
        mIdLink = cursor.getString(cursor.getColumnIndex(WorksheetTable._WORKSHEET_ID_LINK));
        mSpreadsheetIdLink = cursor.getString(cursor.getColumnIndex(WorksheetTable._SPREADSHEET_ID_LINK));
        mUpdated = cursor.getString(cursor.getColumnIndex(WorksheetTable._UPDATED));
        mTitle = cursor.getString(cursor.getColumnIndex(WorksheetTable._TITLE));
        mOpenSearchTotalResults = Integer.parseInt(cursor.getString(cursor.getColumnIndex(WorksheetTable._TOTAL_RESULTS)));
        mOpenSearchStartIndex = Integer.parseInt(cursor.getString(cursor.getColumnIndex(WorksheetTable._START_INDEX)));
    }

    @Override
    public GWorksheet parse(Cursor cursor) {
        return null;
    }

    public GWorksheet(List<GEntryWorksheet> entry) {
        this.mEntry = entry;
    }

    public GWorksheet(String idLink, String updated, String title, GLink selfLink,
                      GLink alternateLink, GLink feedLink, GLink postLink,
                      GAuthor author, int openSearchTotalResults, int openSearchStartIndex,
                      List<GEntryWorksheet> entry) {
        super(idLink, updated, title, selfLink, alternateLink, feedLink, postLink, author,
                openSearchTotalResults, openSearchStartIndex);
        this.mEntry = entry;
    }

    public List<GEntryWorksheet> getEntry() {
        return mEntry;
    }

    public void setEntry(List<GEntryWorksheet> entry) {
        this.mEntry = entry;
    }

    public void setEntryItem(GEntryWorksheet googleEntryWorksheet) {
        if (mEntry == null) {
            mEntry = new ArrayList<>();
        }
        mEntry.add(googleEntryWorksheet);
    }

    public String getSpreadsheetIdLink() {
        return mSpreadsheetIdLink;
    }

    public void setSpreadsheetIdLink(String spreadsheetIdLink) {
        mSpreadsheetIdLink = spreadsheetIdLink;
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
        values.put(WorksheetTable._SPREADSHEET_ID_LINK, mSpreadsheetIdLink);
        values.put(WorksheetTable._UPDATED, mUpdated);
        values.put(WorksheetTable._TITLE, mTitle);
        values.put(WorksheetTable._TOTAL_RESULTS, mOpenSearchTotalResults);
        values.put(WorksheetTable._START_INDEX, mOpenSearchStartIndex);
        return values;
    }

    public GEntryWorksheet getEntryById(String id) {
        GEntryWorksheet entryWorksheet = null;
        for (GEntryWorksheet entry : mEntry) {
            if (entry.getIdGSX().equals(id)) {
                entryWorksheet = entry;
            }
        }
        return entryWorksheet;
    }
}
