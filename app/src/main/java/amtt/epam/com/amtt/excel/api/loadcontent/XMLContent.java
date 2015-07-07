package amtt.epam.com.amtt.excel.api.loadcontent;

import amtt.epam.com.amtt.excel.api.ContentLoadingCallback;
import amtt.epam.com.amtt.excel.api.GoogleApiConst;
import amtt.epam.com.amtt.excel.api.GoogleSpreadsheetContentCallback;
import amtt.epam.com.amtt.excel.bo.GoogleSpreadsheet;
import amtt.epam.com.amtt.excel.bo.GoogleWorksheet;

/**
 * @author Iryna Monchanka
 * @version on 7/6/2015
 */
public class XMLContent {

    private GoogleSpreadsheet mSpreadsheet;
    private GoogleWorksheet mWorksheet;

    private static class XMLContentHolder {
        public static final XMLContent INSTANCE = new XMLContent();
    }

    public static XMLContent getInstance() {
        return XMLContentHolder.INSTANCE;
    }

    public void getSpreadsheet(final GoogleSpreadsheetContentCallback<GoogleSpreadsheet> spreadsheetContentCallback) {
        if (mSpreadsheet != null) {
            spreadsheetContentCallback.resultOfDataLoading(mSpreadsheet);
        } else {
            getSpreadsheetAsynchronously(spreadsheetContentCallback);
        }
    }

    public void setSpreadsheet(GoogleSpreadsheet spreadsheet) {
        this.mSpreadsheet = spreadsheet;
    }

    public void getWorksheet(final GoogleSpreadsheetContentCallback<GoogleWorksheet> spreadsheetContentCallback) {
        if (mWorksheet != null) {
            spreadsheetContentCallback.resultOfDataLoading(mWorksheet);
        } else {
            if (mSpreadsheet != null) {
                mSpreadsheet.getListWorksheets().get(3);
                getWorksheetAsynchronously(mSpreadsheet.getListWorksheets().get(3), spreadsheetContentCallback);
            }
        }
    }

    public void setWorksheet(GoogleWorksheet worksheet) {
        this.mWorksheet = worksheet;
    }

    private void getSpreadsheetAsynchronously(final GoogleSpreadsheetContentCallback<GoogleSpreadsheet> spreadsheetContentCallback) {
        ContentFromBackend.getInstance().getSpreadsheetAsynchronously(new ContentLoadingCallback<GoogleSpreadsheet>() {
            @Override
            public void resultFromBackend(GoogleSpreadsheet result, GoogleApiConst.ContentType tag, GoogleSpreadsheetContentCallback spreadsheetContentCallback) {
                if (tag == GoogleApiConst.ContentType.SPREADSHEET_RESPONSE) {
                    if (spreadsheetContentCallback != null) {
                        if (result != null) {
                            XMLContent.getInstance().setSpreadsheet(result);
                            spreadsheetContentCallback.resultOfDataLoading(mSpreadsheet);
                        } else {
                            spreadsheetContentCallback.resultOfDataLoading(null);
                        }
                    }
                }
            }
        }, spreadsheetContentCallback);
    }

    private void getWorksheetAsynchronously(String worksheetKey, GoogleSpreadsheetContentCallback<GoogleWorksheet> spreadsheetContentCallback) {
        ContentFromBackend.getInstance().getWorksheetAsynchronously(worksheetKey, new ContentLoadingCallback<GoogleWorksheet>() {
            @Override
            public void resultFromBackend(GoogleWorksheet result, GoogleApiConst.ContentType tag, GoogleSpreadsheetContentCallback spreadsheetContentCallback) {
                if (tag == GoogleApiConst.ContentType.WORKSHEET_RESPONSE) {
                    if (spreadsheetContentCallback != null) {
                        if (result != null) {
                            XMLContent.getInstance().setWorksheet(result);
                            spreadsheetContentCallback.resultOfDataLoading(mWorksheet);
                        } else {
                            spreadsheetContentCallback.resultOfDataLoading(null);
                        }
                    }
                }
            }
        }, spreadsheetContentCallback);
    }
}
