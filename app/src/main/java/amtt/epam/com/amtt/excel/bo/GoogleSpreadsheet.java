package amtt.epam.com.amtt.excel.bo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.excel.database.contentprovider.GSUri;
import amtt.epam.com.amtt.excel.database.table.SpreadsheetTable;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */

public class GoogleSpreadsheet extends GoogleSheet<GoogleSpreadsheet> {

    private int mId;
    private List<GoogleEntrySpreadshet> mEntry;

    public GoogleSpreadsheet() {
    }

    public GoogleSpreadsheet(Cursor cursor) {
        if (cursor.getPosition() == -1) {
            cursor.moveToNext();
        }
        mId = cursor.getInt(cursor.getColumnIndex(SpreadsheetTable._ID));
        mIdLink = cursor.getString(cursor.getColumnIndex(SpreadsheetTable._SPREADSHEET_ID_LINK));
        mUpdated = cursor.getString(cursor.getColumnIndex(SpreadsheetTable._UPDATED));
        mTitle = cursor.getString(cursor.getColumnIndex(SpreadsheetTable._TITLE));
        mOpenSearchTotalResults = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SpreadsheetTable._TOTAL_RESULTS)));
        mOpenSearchStartIndex = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SpreadsheetTable._START_INDEX)));
    }

    @Override
    public GoogleSpreadsheet parse(Cursor cursor) {
        return null;
    }

    public GoogleSpreadsheet(List<GoogleEntrySpreadshet> mEntry) {
        this.mEntry = mEntry;
    }

    public GoogleSpreadsheet(String idLink, String updated, String title, GoogleLink selfLink,
                             GoogleLink alternateLink, GoogleLink feedLink, GoogleLink postLink,
                             GoogleAuthor author, int openSearchTotalResults, int openSearchStartIndex,
                             List<GoogleEntrySpreadshet> entry) {
        super(idLink, updated, title, selfLink, alternateLink, feedLink, postLink, author,
                openSearchTotalResults, openSearchStartIndex);
        this.mEntry = entry;
    }

    public List<GoogleEntrySpreadshet> getEntry() {
        return mEntry;
    }

    public void setEntry(List<GoogleEntrySpreadshet> entry) {
        this.mEntry = entry;
    }

    public void setEntryItem(GoogleEntrySpreadshet googleEntrySpreadshet) {
        if (mEntry == null) {
            mEntry = new ArrayList<>();
        }
        mEntry.add(googleEntrySpreadshet);
    }

    public ArrayList<String> getListWorksheets() {
        ArrayList<String> worksheets = null;
        if (mEntry != null && !mEntry.isEmpty()) {
            worksheets = new ArrayList<>();
            for (int i = 0; i < mEntry.size(); i++) {
                worksheets.add(mEntry.get(i).getListFeedLink().getHref());
            }
        }
        return worksheets;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public Uri getUri() {
        return GSUri.SPREADSHEET.get();
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(SpreadsheetTable._SPREADSHEET_ID_LINK, mIdLink);
        values.put(SpreadsheetTable._UPDATED, mUpdated);
        values.put(SpreadsheetTable._TITLE, mTitle);
        values.put(SpreadsheetTable._TOTAL_RESULTS, mOpenSearchTotalResults);
        values.put(SpreadsheetTable._START_INDEX, mOpenSearchStartIndex);
        return values;
    }
}
