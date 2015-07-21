package amtt.epam.com.amtt.googleapi.bo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.googleapi.database.contentprovider.GSUri;
import amtt.epam.com.amtt.googleapi.database.table.SpreadsheetTable;

/**
 * @author Iryna Monchanka
 * @version on 7/1/2015
 */

public class GSpreadsheet extends GSheet<GSpreadsheet> {

    private int mId;
    private List<GEntrySpreadshet> mEntry;

    public GSpreadsheet() {
    }

    public GSpreadsheet(Cursor cursor) {
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
    public GSpreadsheet parse(Cursor cursor) {
        return null;
    }

    public GSpreadsheet(List<GEntrySpreadshet> mEntry) {
        this.mEntry = mEntry;
    }

    public GSpreadsheet(String idLink, String updated, String title, GLink selfLink,
                        GLink alternateLink, GLink feedLink, GLink postLink,
                        GAuthor author, int openSearchTotalResults, int openSearchStartIndex,
                        List<GEntrySpreadshet> entry) {
        super(idLink, updated, title, selfLink, alternateLink, feedLink, postLink, author,
                openSearchTotalResults, openSearchStartIndex);
        this.mEntry = entry;
    }

    public List<GEntrySpreadshet> getEntry() {
        return mEntry;
    }

    public void setEntry(List<GEntrySpreadshet> entry) {
        this.mEntry = entry;
    }

    public void setEntryItem(GEntrySpreadshet googleEntrySpreadshet) {
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
