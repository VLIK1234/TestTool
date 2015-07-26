package amtt.epam.com.amtt.googleapi.api.loadcontent;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.api.ContentConst;
import amtt.epam.com.amtt.api.ContentLoadingCallback;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
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
    private List<GTag> mAllTags;
    private List<GWorksheet> mWorksheetsList;
    private List<GEntryWorksheet> mAllTestCasesList;
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

    private void setSpreadsheetSynchronously(GSpreadsheet result) {
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
    }

    private void getSpreadsheetAsynchronously(final GetContentCallback<GSpreadsheet> getContentCallback) {
        ContentFromBackend.getInstance().getSpreadsheetAsynchronously(new ContentLoadingCallback<GSpreadsheet>() {
            @Override
            public void resultFromBackend(GSpreadsheet result, ContentConst tag, GetContentCallback contentCallback) {
                if (tag == ContentConst.SPREADSHEET_RESPONSE) {
                    if (contentCallback != null) {
                        if (result != null) {
                            setSpreadsheet(result);
                            setSpreadsheetSynchronously(result);
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

    public void getWorksheet(final String linkWorksheet, final GetContentCallback<GWorksheet> getContentCallback) {
        if (mWorksheet != null) {
            getContentCallback.resultOfDataLoading(mWorksheet);
        } else {
            getWorksheetSynchronously(linkWorksheet, getContentCallback);
        }
    }

    private void setWorksheetSynchronously(final GWorksheet worksheet) {
        ContentFromDatabase.setWorksheet(worksheet, new IResult<Integer>() {
            @Override
            public void onResult(Integer result) {
                Logger.i(TAG, "Worksheet " + worksheet.getTitle() + " added " + String.valueOf(result));
            }

            @Override
            public void onError(Exception e) {
                Logger.e(TAG, "Worksheet " + worksheet.getTitle() + " saving error ", e);
            }
        });
    }

    private void getWorksheetSynchronously(final String linkWorksheet, final GetContentCallback<GWorksheet> getContentCallback) {
        ContentFromDatabase.getWorksheet(linkWorksheet, new IResult<List<GWorksheet>>() {
            @Override
            public void onResult(List<GWorksheet> result) {
                if (!result.isEmpty()) {
                    GWorksheet worksheet = result.get(0);
                    getContentCallback.resultOfDataLoading(worksheet);
                } else {
                    getWorksheetAsynchronously(linkWorksheet, getContentCallback);
                }
            }

            @Override
            public void onError(Exception e) {
                getWorksheetAsynchronously(linkWorksheet, getContentCallback);
            }
        });
    }

    private void getWorksheetAsynchronously(final String linkWorksheet, final GetContentCallback<GWorksheet> getContentCallback) {
        ContentFromBackend.getInstance().getWorksheetAsynchronously(linkWorksheet, new ContentLoadingCallback<GWorksheet>() {
            @Override
            public void resultFromBackend(GWorksheet result, ContentConst tag, GetContentCallback contentCallback) {
                if (tag == ContentConst.WORKSHEET_RESPONSE) {
                    if (contentCallback != null) {
                        if (result != null) {
                            result.setSpreadsheetIdLink(mSpreadsheet.getIdLink());
                            setWorksheet(result);
                            setWorksheetSynchronously(result);
                            setTestCasesSynchronously(result.getEntry(), result.getIdLink());
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

    public GEntryWorksheet getLastTestcase() {
        if (mLastTestcaseId != null) {
            setLastTestCase(getTestcaseByIdGSX(mLastTestcaseId));
        }
        return mLastTestCase;
    }

    public void setLastTestCase(GEntryWorksheet testCase) {
        this.mLastTestCase = testCase;
    }

    public String getLastTestcaseId() {
        return mLastTestcaseId;
    }

    public void setLastTestcaseId(String lastTestcaseId) {
        this.mLastTestcaseId = lastTestcaseId;
    }

    private void setTestCasesSynchronously(List<GEntryWorksheet> testcases, String idLinkWorksheet) {
        GEntryWorksheet testcase;
        for (int i = 0; i < testcases.size(); i++) {
            testcase = testcases.get(i);
            testcase.setIdWorksheetLink(idLinkWorksheet);
            setTestCaseSynchronously(testcase);
            setTag(testcase.getTestCaseNameGSX(), testcase.getIdLink());
        }
    }

    private void setTestCaseSynchronously(GEntryWorksheet testcase) {
        ContentFromDatabase.setTestCase(testcase, new IResult<Integer>() {
            @Override
            public void onResult(Integer result) {
                Logger.i(TAG, "TestCase added " + String.valueOf(result));
            }

            @Override
            public void onError(Exception e) {
                Logger.e(TAG, "TestCase saving error ", e);
            }
        });
    }

    public void getAllTestCases(final GetContentCallback<List<GEntryWorksheet>> getContentCallback) {
        if (getAllTestCases() != null) {
            getContentCallback.resultOfDataLoading(getAllTestCases());
        } else {
            ContentFromDatabase.getAllTestCases(new IResult<List<DatabaseEntity>>() {
                @Override
                public void onResult(List<DatabaseEntity> result) {
                    if (result != null && !result.isEmpty()) {
                        ArrayList<GEntryWorksheet> testcases = (ArrayList) result;
                        getContentCallback.resultOfDataLoading(testcases);
                    } else {
                        getSpreadsheetAsynchronously(new GetContentCallback<GSpreadsheet>() {
                            @Override
                            public void resultOfDataLoading(GSpreadsheet result) {
                                if (result != null) {
                                    getWorksheet(getLinkWorksheet(), new GetContentCallback<GWorksheet>() {
                                        @Override
                                        public void resultOfDataLoading(GWorksheet result) {
                                            if (result != null) {
                                                getContentCallback.resultOfDataLoading(result.getEntry());
                                            } else {
                                                Logger.e(TAG, "Worksheet == null");
                                            }
                                        }
                                    });
                                } else {
                                    Logger.e(TAG, "Spreadsheet == null");
                                }
                            }
                        });
                    }
                }

                @Override
                public void onError(Exception e) {
                    getContentCallback.resultOfDataLoading(null);
                    Logger.e(TAG, "TestCase loading error ", e);
                }
            });
        }
    }

    public List<GEntryWorksheet> getAllTestCases() {
        if (mAllTestCasesList != null) {
            return mAllTestCasesList;
        } else {
            return null;
        }
    }
    //endregion

    //region Tags
    public void getAllTags(final GetContentCallback<List<GTag>> getContentCallback) {
        if (mAllTags != null) {
            getContentCallback.resultOfDataLoading(mAllTags);
        } else {
            ContentFromDatabase.getAllTags(new IResult<List<DatabaseEntity>>() {
                @Override
                public void onResult(List<DatabaseEntity> result) {
                    if (result != null) {
                        ArrayList<GTag> tags = (ArrayList) result;
                        setTags(tags);
                        getContentCallback.resultOfDataLoading(tags);
                    } else {
                        getContentCallback.resultOfDataLoading(null);
                    }
                }

                @Override
                public void onError(Exception e) {
                    getContentCallback.resultOfDataLoading(null);
                    Logger.e(TAG, e.getMessage(), e);
                }
            });
        }
    }

    public void setTags(List<GTag> tags) {
        this.mAllTags = tags;
    }

    public void setTag(GTag tag) {
        if (mAllTags == null) {
            mAllTags = new ArrayList<>();
        }
        if (tag != null) {
            mAllTags.add(tag);
        }
    }

    public void setTag(String testCaseName, String testCaseIdLink) {
        if (testCaseName != null && testCaseIdLink != null) {
            GTag gTag;
            List<GTag> mTags = new ArrayList<>();
            String[] tags = testCaseName.split(" - ");
            for (String tag : tags) {
                gTag = new GTag();
                gTag.setName(tag);
                gTag.setIdLinkTestCase(testCaseIdLink);
                mTags.add(gTag);
            }
            ContentFromDatabase.setTags(mTags, new IResult<Integer>() {
                @Override
                public void onResult(Integer result) {

                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }
    //endregion

    public String getLinkWorksheet() {
        if (mSpreadsheet != null && mSpreadsheet.getListWorksheets() != null) {
            return mSpreadsheet.getListWorksheets().get(0);
        } else {
            return null;
        }
    }

}
