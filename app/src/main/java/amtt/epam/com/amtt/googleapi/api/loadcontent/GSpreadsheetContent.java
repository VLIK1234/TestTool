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
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 7/6/2015
 */

public class GSpreadsheetContent {

    //region Variables
    private final String TAG = this.getClass().getSimpleName();
    private GSpreadsheet mSpreadsheet;
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
    public void getWorksheet(final String linkWorksheet, final GetContentCallback<GWorksheet> getContentCallback) {
        getWorksheetAsynchronously(linkWorksheet, getContentCallback);
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

    private void getWorksheetAsynchronously(final String linkWorksheet, final GetContentCallback<GWorksheet> getContentCallback) {
        ContentFromBackend.getInstance().getWorksheetAsynchronously(linkWorksheet, new ContentLoadingCallback<GWorksheet>() {
            @Override
            public void resultFromBackend(GWorksheet result, ContentConst tag, GetContentCallback contentCallback) {
                if (tag == ContentConst.WORKSHEET_RESPONSE) {
                    if (contentCallback != null) {
                        if (result != null) {
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
    //endregion

    //region TestCase
    public void getTestcaseByIdLink(String idLink, final GetContentCallback<GEntryWorksheet> getContentCallback) {
        ContentFromDatabase.getTestcaseByIdLink(idLink, new IResult<List<GEntryWorksheet>>() {
            @Override
            public void onResult(List<GEntryWorksheet> result) {
                if (result != null && !result.isEmpty()) {
                    getContentCallback.resultOfDataLoading(result.get(0));
                }else {
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
            getTestCasesAsynchronously(getContentCallback);
    }

    private void getTestCasesAsynchronously(final GetContentCallback<List<GEntryWorksheet>> getContentCallback) {
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
    //endregion

    //region Tags
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
                    Logger.d(TAG, "Tags added " + String.valueOf(result));
                }

                @Override
                public void onError(Exception e) {
                    Logger.e(TAG, "Saving tags error", e);
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
