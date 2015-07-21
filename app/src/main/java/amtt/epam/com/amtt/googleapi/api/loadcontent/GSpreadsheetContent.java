package amtt.epam.com.amtt.googleapi.api.loadcontent;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.api.ContentConst;
import amtt.epam.com.amtt.api.ContentLoadingCallback;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.googleapi.bo.GSpreadsheet;
import amtt.epam.com.amtt.googleapi.bo.GTag;
import amtt.epam.com.amtt.googleapi.bo.GWorksheet;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 7/6/2015
 */

public class GSpreadsheetContent {

    //region Variables
    private final String TAG = this.getClass().getSimpleName();
    private GSpreadsheet mSpreadsheet;
    private GWorksheet mWorksheet;
    private GEntryWorksheet mLastTestCase;
    private String mLastTestcaseId;
    private List<GTag> mTags;
    private List<GWorksheet> mWorksheetsList;
    //endregion

    private static class XMLContentHolder {
        public static final GSpreadsheetContent INSTANCE = new GSpreadsheetContent();
    }

    public static GSpreadsheetContent getInstance() {
        return XMLContentHolder.INSTANCE;
    }

    //region Spreadsheet
    public void setSpreadsheet(GSpreadsheet spreadsheet) {
        this.mSpreadsheet = spreadsheet;
    }

    public void getSpreadsheet(final GetContentCallback<GSpreadsheet> getContentCallback) {
        if (mSpreadsheet != null) {
            getContentCallback.resultOfDataLoading(mSpreadsheet);
        } else {
            getSpreadsheetSynchronously(getContentCallback);
        }
    }

    private void getSpreadsheetSynchronously(final GetContentCallback<GSpreadsheet> getContentCallback) {
        if (ActiveUser.getInstance().getSpreadsheetLink() != null) {
            ContentFromDatabase.getSpreadsheet(ActiveUser.getInstance().getSpreadsheetLink(), new IResult<List<GSpreadsheet>>() {
                @Override
                public void onResult(List<GSpreadsheet> result) {
                    if (result != null && !result.isEmpty()) {
                        GSpreadsheet spreadsheet = result.get(0);
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

    private void getSpreadsheetAsynchronously(final GetContentCallback<GSpreadsheet> getContentCallback) {
        ContentFromBackend.getInstance().getSpreadsheetAsynchronously(new ContentLoadingCallback<GSpreadsheet>() {
            @Override
            public void resultFromBackend(GSpreadsheet result, ContentConst tag, GetContentCallback contentCallback) {
                if (tag == ContentConst.SPREADSHEET_RESPONSE) {
                    if (contentCallback != null) {
                        if (result != null) {
                            setSpreadsheet(result);
                            mWorksheetsList = new ArrayList<>();
                            GWorksheet worksheet;
                            for (int i = 0; i < result.getEntry().size(); i++) {
                                worksheet = new GWorksheet();
                                worksheet.setSpreadsheetIdLink(result.getIdLink());
                                worksheet.setIdLink(result.getEntry().get(i).getListFeedLink().getHref());
                                worksheet.setTitle(result.getEntry().get(i).getTitle());
                                worksheet.setUpdated(result.getEntry().get(i).getUpdated());
                                mWorksheetsList.add(worksheet);
                            }
                            ContentFromDatabase.setWorksheets(mWorksheetsList, new IResult<Integer>() {
                                @Override
                                public void onResult(Integer result) {
                                    Logger.i(TAG, "Spreadsheet " + mSpreadsheet.getTitle() + ", Worksheets " + String.valueOf(result));
                                }

                                @Override
                                public void onError(Exception e) {
                                    Logger.e(TAG, "Spreadsheet " + mSpreadsheet.getTitle() + ", Worksheets saving error ", e);
                                }
                            });
                            ContentFromDatabase.setSpreadsheet(result, new IResult<Integer>() {
                                @Override
                                public void onResult(Integer result) {
                                    Logger.i(TAG, "Spreadsheet " + mSpreadsheet.getTitle() + " added " + String.valueOf(result));
                                }

                                @Override
                                public void onError(Exception e) {
                                    Logger.e(TAG, "Spreadsheet " + mSpreadsheet.getTitle() + " saving error ", e);
                                }
                            });
                            contentCallback.resultOfDataLoading(result);
                        } else {
                            contentCallback.resultOfDataLoading(null);
                        }
                    }
                }
            }
        }, getContentCallback);
    }
    //endregion

    //region Worksheet
    public void setWorksheet(GWorksheet worksheet) {
        this.mWorksheet = worksheet;
    }

    public void getWorksheet(final GetContentCallback<GWorksheet> getContentCallback) {
        if (mWorksheet != null) {
            getContentCallback.resultOfDataLoading(mWorksheet);
        } else {
            if (mSpreadsheet != null) {
                getWorksheetAsynchronously(mSpreadsheet.getListWorksheets().get(8), getContentCallback);
            } else {
                getSpreadsheet(new GetContentCallback<GSpreadsheet>() {
                    @Override
                    public void resultOfDataLoading(GSpreadsheet result) {
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

    private void getWorksheetAsynchronously(final String worksheetKey, final GetContentCallback<GWorksheet> getContentCallback) {
        ContentFromBackend.getInstance().getWorksheetAsynchronously(worksheetKey, new ContentLoadingCallback<GWorksheet>() {
            @Override
            public void resultFromBackend(GWorksheet result, ContentConst tag, GetContentCallback contentCallback) {
                if (tag == ContentConst.WORKSHEET_RESPONSE) {
                    if (contentCallback != null) {
                        if (result != null) {
                            setWorksheet(result);
                            getWorksheetSynchronously(worksheetKey, new GetContentCallback<GWorksheet>() {
                                @Override
                                public void resultOfDataLoading(GWorksheet result) {
                                    if(result != null){
                                        result.setOpenSearchStartIndex(mWorksheet.getOpenSearchStartIndex());
                                        result.setOpenSearchTotalResults(mWorksheet.getOpenSearchTotalResults());
                                        updateWorksheet(result);
                                    }
                                }
                            });
                            contentCallback.resultOfDataLoading(result);
                        } else {
                            contentCallback.resultOfDataLoading(null);
                        }
                    }
                }
            }
        }, getContentCallback);
    }

    private void updateWorksheet(GWorksheet worksheet) {
        ContentFromDatabase.updateWorksheet(worksheet, new IResult<Integer>() {
            @Override
            public void onResult(Integer result) {
                Logger.i(TAG, "Worksheet " + String.valueOf(result) + " updated ");
            }

            @Override
            public void onError(Exception e) {
                Logger.e(TAG, "Worksheet update error ", e);
            }
        });
    }

    private void getWorksheetSynchronously(final String worksheetKey, final GetContentCallback<GWorksheet> getContentCallback) {
        ContentFromDatabase.getWorksheet(worksheetKey, new IResult<List<GWorksheet>>() {
            @Override
            public void onResult(List<GWorksheet> result) {
                GWorksheet worksheet = null;
                if (!result.isEmpty()) {
                    worksheet = result.get(0);
                }
                getContentCallback.resultOfDataLoading(worksheet);
            }

            @Override
            public void onError(Exception e) {
                getWorksheetAsynchronously(worksheetKey, getContentCallback);
            }
        });
    }

    public List<GWorksheet> getWorksheetsList() {
        return mWorksheetsList;
    }

    public void setWorksheetsList(List<GWorksheet> worksheetsList) {
        mWorksheetsList = worksheetsList;
    }

    public void setWorksheetsListItem(GWorksheet worksheet) {
        if (mWorksheetsList == null) {
            mWorksheetsList = new ArrayList<>();
        }
        mWorksheetsList.add(worksheet);
    }
    //endregion

    //region TestCase
    public GEntryWorksheet getTestcaseByIdGSX(String idGSX) {
        GEntryWorksheet testcase = null;
        if (mWorksheet != null) {
            testcase = mWorksheet.getEntryById(idGSX);
        }
        return testcase;
    }

    public GEntryWorksheet getLastTestcase(){
        if(mLastTestcaseId != null){
            setLastTestCase(getTestcaseByIdGSX(mLastTestcaseId));
        }
        return mLastTestCase;
    }

    public void setLastTestCase(GEntryWorksheet testCase){
        this.mLastTestCase = testCase;
    }

    public String getLastTestcaseId() {
        return mLastTestcaseId;
    }

    public void setLastTestcaseId(String lastTestcaseId) {
        this.mLastTestcaseId = lastTestcaseId;
    }
    //endregion

    //region Tags
    public List<GTag> getTags() {
        return mTags;
    }

    public void setTags(List<GTag> tags) {
        this.mTags = tags;
    }

    public void setTag(GTag tag) {
        if (mTags == null) {
            mTags = new ArrayList<>();
        }
        if (tag != null) {
            mTags.add(tag);
        }
    }

    public void setTag(String testCaseName, String testCaseIdLink) {
        if (testCaseName != null && testCaseIdLink != null) {
            GTag gTag;
            String[] tags = testCaseName.split(" - ");
            for (String tag : tags) {
                gTag = new GTag();
                gTag.setName(tag);
                gTag.setIdLinkTestCase(testCaseIdLink);
                setTag(gTag);
            }
        }
    }
    //endregion

}
