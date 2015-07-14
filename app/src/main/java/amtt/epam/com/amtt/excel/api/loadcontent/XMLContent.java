package amtt.epam.com.amtt.excel.api.loadcontent;

import amtt.epam.com.amtt.api.ContentConst;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.api.ContentLoadingCallback;
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

    public void getSpreadsheet(final GetContentCallback<GoogleSpreadsheet> getContentCallback) {
        if (mSpreadsheet != null) {
            getContentCallback.resultOfDataLoading(mSpreadsheet);
        } else {
            getSpreadsheetAsynchronously(getContentCallback);
        }
    }

    public void setSpreadsheet(GoogleSpreadsheet spreadsheet) {
        this.mSpreadsheet = spreadsheet;
    }

    public void getWorksheet(final GetContentCallback<GoogleWorksheet> getContentCallback) {
        if (mWorksheet != null) {
            getContentCallback.resultOfDataLoading(mWorksheet);
        } else {
            if (mSpreadsheet != null) {
                mSpreadsheet.getListWorksheets().get(3);
                getWorksheetAsynchronously(mSpreadsheet.getListWorksheets().get(3), getContentCallback);
            }
        }
    }

    public void setWorksheet(GoogleWorksheet worksheet) {
        this.mWorksheet = worksheet;
    }

    private void getSpreadsheetAsynchronously(final GetContentCallback<GoogleSpreadsheet> getContentCallback) {
        ContentFromBackend.getInstance().getSpreadsheetAsynchronously(new ContentLoadingCallback<GoogleSpreadsheet>() {
            @Override
            public void resultFromBackend(GoogleSpreadsheet result, ContentConst tag, GetContentCallback getContentCallback1) {
                if (tag == ContentConst.SPREADSHEET_RESPONSE) {
                    if (getContentCallback1 != null) {
                        if (result != null) {
                            XMLContent.getInstance().setSpreadsheet(result);
                            getContentCallback1.resultOfDataLoading(mSpreadsheet);
                        } else {
                            getContentCallback1.resultOfDataLoading(null);
                        }
                    }
                }
            }
        }, getContentCallback);
    }

    private void getWorksheetAsynchronously(String worksheetKey, GetContentCallback<GoogleWorksheet> getContentCallback) {
        ContentFromBackend.getInstance().getWorksheetAsynchronously(worksheetKey, new ContentLoadingCallback<GoogleWorksheet>() {
            @Override
            public void resultFromBackend(GoogleWorksheet result, ContentConst tag, GetContentCallback getContentCallback1) {
                if (tag == ContentConst.WORKSHEET_RESPONSE) {
                    if (getContentCallback1 != null) {
                        if (result != null) {
                            XMLContent.getInstance().setWorksheet(result);
                            getContentCallback1.resultOfDataLoading(mWorksheet);
                        } else {
                            getContentCallback1.resultOfDataLoading(null);
                        }
                    }
                }
            }
        }, getContentCallback);
    }
}
