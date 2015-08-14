package amtt.epam.com.amtt.googleapi.api.loadcontent;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.api.ContentLoadingCallback;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.googleapi.bo.GSpreadsheet;
import amtt.epam.com.amtt.googleapi.bo.GTag;
import amtt.epam.com.amtt.googleapi.bo.GWorksheet;
import amtt.epam.com.amtt.util.InputsUtil;
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

    private void setSpreadsheetInDB(final GSpreadsheet result) {
        ContentFromDatabase.setSpreadsheet(result, new IResult<Integer>() {
            @Override
            public void onResult(Integer res) {
                setWorksheets(result);
                Logger.d(TAG, "Spreadsheet " + mSpreadsheet.getTitle() + " added " + String.valueOf(res));
            }

            @Override
            public void onError(Exception e) {
                Logger.e(TAG, "Spreadsheet " + mSpreadsheet.getTitle() + " saving error ", e);
            }
        });
    }

    public void getSpreadsheet(final String idLink, final GetContentCallback<GSpreadsheet> getContentCallback) {
        ContentFromDatabase.getSpreadsheet(idLink, new IResult<List<GSpreadsheet>>() {
            @Override
            public void onResult(List<GSpreadsheet> result) {
                if (result != null && !result.isEmpty()) {
                    getContentCallback.resultOfDataLoading(result.get(0));
                } else {
                    getSpreadsheetFromBackend(idLink, getContentCallback);
                }
            }

            @Override
            public void onError(Exception e) {
                Logger.e(TAG, e.getMessage(), e);
                getSpreadsheetFromBackend(idLink, getContentCallback);
            }
        });
    }

    public void getAllSpreadsheets(final GetContentCallback<Integer> getContentCallback) {
        ContentFromDatabase.getAllSpreadsheets(new IResult<List<DatabaseEntity>>() {
            @Override
            public void onResult(List<DatabaseEntity> result) {
                if (result != null) {
                    getContentCallback.resultOfDataLoading(result.size());
                }
            }

            @Override
            public void onError(Exception e) {
                Logger.e(TAG, e.getMessage(), e);
                getContentCallback.resultOfDataLoading(-1);
            }
        });
    }

    private void getSpreadsheetFromBackend(String idLink, final GetContentCallback<GSpreadsheet> getContentCallback) {
        ContentFromBackend.getInstance().getSpreadsheet(idLink, new ContentLoadingCallback<GSpreadsheet, GSpreadsheet>() {
            @Override
            public void resultFromBackend(GSpreadsheet result, GetContentCallback<GSpreadsheet> contentCallback) {
                if (result != null) {
                    setSpreadsheet(result);
                    setSpreadsheetInDB(result);
                    contentCallback.resultOfDataLoading(result);
                } else {
                    contentCallback.resultOfDataLoading(null);
                }
            }
        }, getContentCallback);
    }
    //endregion

    //region Worksheet
    private void setWorksheetFromDB(final GWorksheet worksheet) {
        ContentFromDatabase.setWorksheet(worksheet, new IResult<Integer>() {
            @Override
            public void onResult(Integer result) {
                Logger.d(TAG, "Worksheet " + worksheet.getTitle() + " added " + String.valueOf(result));
            }

            @Override
            public void onError(Exception e) {
                Logger.e(TAG, "Worksheet " + worksheet.getTitle() + " saving error ", e);
            }
        });
    }

    private void setWorksheets(GSpreadsheet spreadsheet) {
        if (spreadsheet != null && spreadsheet.getEntry() != null && !spreadsheet.getEntry().isEmpty()) {
            for (int i = 0; i < spreadsheet.getEntry().size(); i++) {
                getWorksheetFromBackend(spreadsheet.getEntry().get(i).getListFeedLink().getHref(), new GetContentCallback<GWorksheet>() {
                    @Override
                    public void resultOfDataLoading(GWorksheet result) {
                        if (result != null && result.getTitle() != null) {
                            Logger.d(TAG, result.getTitle());
                        }
                    }
                });
            }
        }
    }

    private void getWorksheetFromBackend(final String linkWorksheet, final GetContentCallback<GWorksheet> getContentCallback) {
        ContentFromBackend.getInstance().getWorksheet(linkWorksheet, new ContentLoadingCallback<GWorksheet, GWorksheet>() {
            @Override
            public void resultFromBackend(GWorksheet result, GetContentCallback<GWorksheet> contentCallback) {
                if (result != null) {
                    result.setSpreadsheetIdLink(mSpreadsheet.getIdLink());
                    setWorksheetFromDB(result);
                    setTestCasesInDB(result.getEntry(), result.getIdLink(), result.getSpreadsheetIdLink());
                    contentCallback.resultOfDataLoading(result);
                } else {
                    contentCallback.resultOfDataLoading(null);
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

    private void setTestCasesInDB(List<GEntryWorksheet> testcases, String idLinkWorksheet, String idLinkSpreadsheet) {
        GEntryWorksheet testcase;
        for (int i = 0; i < testcases.size(); i++) {
            testcase = testcases.get(i);
            testcase.setIdWorksheetLink(idLinkWorksheet);
            testcase.setIdSpreadsheetLink(idLinkSpreadsheet);
            setTestCaseInDB(testcase);
            setTag(testcase.getTestCaseNameGSX(), testcase.getIdLink(), testcase.getIdSpreadsheetLink());
        }
    }

    private void setTestCaseInDB(GEntryWorksheet testcase) {
        ContentFromDatabase.setTestCase(testcase, new IResult<Integer>() {
            @Override
            public void onResult(Integer result) {
                Logger.d(TAG, "TestCase added " + String.valueOf(result));
            }

            @Override
            public void onError(Exception e) {
                Logger.e(TAG, "TestCase saving error ", e);
            }
        });
    }

    public void getAllTestCases(String idSpreadsheetLink, final GetContentCallback<List<GEntryWorksheet>> getContentCallback) {
            getAllTestCasesInDB(idSpreadsheetLink, getContentCallback);
    }

    private void getAllTestCasesInDB(final String idSpreadsheetLink, final GetContentCallback<List<GEntryWorksheet>> getContentCallback) {
        if (!InputsUtil.isEmpty(idSpreadsheetLink)) {
            ContentFromDatabase.getTestCasesByLinkSpreadsheet(idSpreadsheetLink, new IResult<List<GEntryWorksheet>>() {
                @Override
                public void onResult(List<GEntryWorksheet> result) {
                    if (result != null && !result.isEmpty()) {
                        getContentCallback.resultOfDataLoading(result);
                    } else {
                        getContentCallback.resultOfDataLoading(null);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Logger.e(TAG, e.getMessage(), e);
                    getContentCallback.resultOfDataLoading(null);
                }
            });
        } else {
            getContentCallback.resultOfDataLoading(null);
            Logger.d(TAG, "Spreadsheet link invalide");
        }
    }

    public void getTestcasesByIdLinksTestcases(String spreadsheetLink, ArrayList<String> testcasesIdLinks, final GetContentCallback<List<GEntryWorksheet>> getContentCallback) {
        if (!InputsUtil.isEmpty(spreadsheetLink) && testcasesIdLinks != null && !testcasesIdLinks.isEmpty()) {
            ContentFromDatabase.getTestcasesByIdLinksTestcases(spreadsheetLink, testcasesIdLinks, new IResult<List<GEntryWorksheet>>() {
                @Override
                public void onResult(List<GEntryWorksheet> result) {
                    if (result != null && !result.isEmpty()) {
                        getContentCallback.resultOfDataLoading(result);
                    } else {
                        Logger.d(TAG, "testcases not found");
                        getContentCallback.resultOfDataLoading(null);
                    }
                }

                @Override
                public void onError(Exception e) {
                    getContentCallback.resultOfDataLoading(null);
                    Logger.e(TAG, e.getMessage(), e);
                }
            });
        } else {
            Logger.d(TAG, "testcases not found, spreadsheetLink or testcasesIdLinks invalide");
            getContentCallback.resultOfDataLoading(null);
        }
    }
    //endregion

    //region Tags
    public void setTag(String testCaseName, String testCaseIdLink, String spreadsheetIdLink) {
        if (testCaseName != null && testCaseIdLink != null) {
            GTag gTag;
            List<GTag> mTags = new ArrayList<>();
            String[] tags = testCaseName.split(" - ");
            for (String tag : tags) {
                gTag = new GTag();
                gTag.setName(tag);
                gTag.setIdLinkTestCase(testCaseIdLink);
                gTag.setIdLinkSpreadsheet(spreadsheetIdLink);
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

    public void getAllTags(String spreadsheetIdLink, final GetContentCallback<List<GTag>> getContentCallback) {
        if (!InputsUtil.isEmpty(spreadsheetIdLink)) {
            ContentFromDatabase.getTagsByIdLinkSpreadsheet(spreadsheetIdLink, new IResult<List<GTag>>() {
                @Override
                public void onResult(List<GTag> result) {
                    if (result != null) {
                        getContentCallback.resultOfDataLoading(result);
                    } else {
                        getContentCallback.resultOfDataLoading(null);
                        Logger.d(TAG, "Tags not found in db");
                    }
                }

                @Override
                public void onError(Exception e) {
                    Logger.e(TAG, e.getMessage(), e);
                    getContentCallback.resultOfDataLoading(null);
                }
            });
        } else {
            getContentCallback.resultOfDataLoading(null);
            Logger.d(TAG, "SpreadsheetLink invalide");
        }
    }

    public void getTagsByIdLinksTestcases(String spreadsheetLink, ArrayList<String> testcasesIdLinks, final GetContentCallback<List<GTag>> getContentCallback) {
        if (!InputsUtil.isEmpty(spreadsheetLink) && testcasesIdLinks != null && !testcasesIdLinks.isEmpty()) {
            ContentFromDatabase.getTagsByIdLinksTestcases(spreadsheetLink, testcasesIdLinks, new IResult<List<GTag>>() {
                @Override
                public void onResult(List<GTag> result) {
                    if (result != null) {
                        getContentCallback.resultOfDataLoading(result);
                    } else {
                        Logger.d(TAG, "tags not found");
                        getContentCallback.resultOfDataLoading(null);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Logger.e(TAG, e.getMessage(), e);
                    getContentCallback.resultOfDataLoading(null);
                }
            });
        } else {
            Logger.d(TAG, "tags not found, spreadsheetLink or testcasesIdLinks invalide");
            getContentCallback.resultOfDataLoading(null);
        }
    }
    //endregion
}
