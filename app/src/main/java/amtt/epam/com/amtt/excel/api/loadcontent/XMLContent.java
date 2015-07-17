package amtt.epam.com.amtt.excel.api.loadcontent;

import java.util.List;

import amtt.epam.com.amtt.api.ContentConst;
import amtt.epam.com.amtt.api.ContentLoadingCallback;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.excel.bo.GoogleEntryWorksheet;
import amtt.epam.com.amtt.excel.bo.GoogleSpreadsheet;
import amtt.epam.com.amtt.excel.bo.GoogleWorksheet;
import amtt.epam.com.amtt.util.ActiveUser;

/**
 * @author Iryna Monchanka
 * @version on 7/6/2015
 */
public class XMLContent {

    private GoogleSpreadsheet mSpreadsheet;
    private GoogleWorksheet mWorksheet;
    private GoogleEntryWorksheet mLastTestCase;
    private String mLastTestcaseId;

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
            getSpreadsheetSynchronously(getContentCallback);
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
                getWorksheetAsynchronously(mSpreadsheet.getListWorksheets().get(8), getContentCallback);
            } else {
                getSpreadsheet(new GetContentCallback<GoogleSpreadsheet>() {
                    @Override
                    public void resultOfDataLoading(GoogleSpreadsheet result) {
                        if (result != null) {
                            getWorksheetAsynchronously(result.getListWorksheets().get(8), getContentCallback);
                        }else{
                            getContentCallback.resultOfDataLoading(mWorksheet);
                        }
                    }
                });
            }
        }
    }

    public void setWorksheet(GoogleWorksheet worksheet) {
        this.mWorksheet = worksheet;
    }

    private void getSpreadsheetSynchronously(final GetContentCallback<GoogleSpreadsheet> getContentCallback) {
        if (ActiveUser.getInstance().getSpreadsheetLink() != null) {
            ContentFromDatabase.getSpreadsheet(ActiveUser.getInstance().getSpreadsheetLink(), new IResult<List<GoogleSpreadsheet>>() {
                @Override
                public void onResult(List<GoogleSpreadsheet> result) {
                    if (result != null && !result.isEmpty()) {
                        GoogleSpreadsheet spreadsheet = result.get(0);
                        setSpreadsheet(spreadsheet);
                        getContentCallback.resultOfDataLoading(mSpreadsheet);
                    } else {
                        getSpreadsheetAsynchronously(getContentCallback);
                    }
                }

                @Override
                public void onError(Exception e) {
                    getSpreadsheetAsynchronously(getContentCallback);
                }
            });
        } else {
            getSpreadsheetAsynchronously(getContentCallback);
        }
    }

    private void getSpreadsheetAsynchronously(final GetContentCallback<GoogleSpreadsheet> getContentCallback) {
        ContentFromBackend.getInstance().getSpreadsheetAsynchronously(new ContentLoadingCallback<GoogleSpreadsheet>() {
            @Override
            public void resultFromBackend(GoogleSpreadsheet result, ContentConst tag, GetContentCallback contentCallback) {
                if (tag == ContentConst.SPREADSHEET_RESPONSE) {
                    if (contentCallback != null) {
                        if (result != null) {
                            XMLContent.getInstance().setSpreadsheet(result);
                            contentCallback.resultOfDataLoading(result);
                        } else {
                            contentCallback.resultOfDataLoading(null);
                        }
                    }
                }
            }
        }, getContentCallback);
    }

    private void getWorksheetAsynchronously(String worksheetKey, final GetContentCallback<GoogleWorksheet> getContentCallback) {
        ContentFromBackend.getInstance().getWorksheetAsynchronously(worksheetKey, new ContentLoadingCallback<GoogleWorksheet>() {
            @Override
            public void resultFromBackend(GoogleWorksheet result, ContentConst tag, GetContentCallback contentCallback) {
                if (tag == ContentConst.WORKSHEET_RESPONSE) {
                    if (contentCallback != null) {
                        if (result != null) {
                            XMLContent.getInstance().setWorksheet(result);
                            contentCallback.resultOfDataLoading(result);
                        } else {
                            contentCallback.resultOfDataLoading(null);
                        }
                    }
                }
            }
        }, getContentCallback);
    }

    public GoogleEntryWorksheet getTestcaseByIdGSX(String idGSX) {
        GoogleEntryWorksheet testcase = null;
        if (mWorksheet != null) {
            testcase = mWorksheet.getEntryById(idGSX);
        }
        return testcase;
    }

    public GoogleEntryWorksheet getLastTestcase(){
        if(mLastTestcaseId != null){
            setLastTestCase(getTestcaseByIdGSX(mLastTestcaseId));
        }
        return mLastTestCase;
    }

    public void setLastTestCase(GoogleEntryWorksheet testCase){
        this.mLastTestCase = testCase;
    }

    public String getLastTestcaseId() {
        return mLastTestcaseId;
    }

    public void setLastTestcaseId(String lastTestcaseId) {
        this.mLastTestcaseId = lastTestcaseId;
    }
}
