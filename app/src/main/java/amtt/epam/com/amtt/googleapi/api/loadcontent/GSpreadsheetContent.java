package amtt.epam.com.amtt.googleapi.api.loadcontent;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.api.ContentLoadingCallback;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.googleapi.bo.GSpreadsheet;
import amtt.epam.com.amtt.googleapi.bo.GTag;
import amtt.epam.com.amtt.googleapi.bo.GWorksheet;
import amtt.epam.com.amtt.util.Constants;
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
        ContentFromDatabase.setSpreadsheet(result, new Callback<Integer>() {
            @Override
            public void onLoadStart() {}

            @Override
            public void onLoadExecuted(Integer integer) {
                setWorksheets(result);
                Logger.d(TAG, mSpreadsheet.getTitle() + Constants.Logs.S_SHEET_ADDED + String.valueOf(integer));
            }

            @Override
            public void onLoadError(Exception e) {
                Logger.e(TAG, mSpreadsheet.getTitle() + Constants.Logs.S_SHEET_ERR, e);
            }
        });
    }

    public void getSpreadsheet(final String idLink, final GetContentCallback<GSpreadsheet> getContentCallback) {
        ContentFromDatabase.getSpreadsheet(idLink, new Callback<List<GSpreadsheet>>() {
            @Override
            public void onLoadStart() {

            }

            @Override
            public void onLoadExecuted(List<GSpreadsheet> spreadsheets) {
                if (spreadsheets != null && !spreadsheets.isEmpty()) {
                    getContentCallback.resultOfDataLoading(spreadsheets.get(0));
                } else {
                    getSpreadsheetFromBackend(idLink, getContentCallback);
                }
            }

            @Override
            public void onLoadError(Exception e) {
                Logger.e(TAG, e.getMessage(), e);
                getSpreadsheetFromBackend(idLink, getContentCallback);
            }
        });
    }

    public void getAllSpreadsheets(final GetContentCallback<Integer> getContentCallback) {
        ContentFromDatabase.getAllSpreadsheets(new Callback<List<DatabaseEntity>>() {
            @Override
            public void onLoadStart() {}

            @Override
            public void onLoadExecuted(List<DatabaseEntity> databaseEntities) {
                if (databaseEntities != null) {
                    getContentCallback.resultOfDataLoading(databaseEntities.size());
                }
            }

            @Override
            public void onLoadError(Exception e) {
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
        ContentFromDatabase.setWorksheet(worksheet, new Callback<Integer>() {
            @Override
            public void onLoadStart() {}

            @Override
            public void onLoadExecuted(Integer integer) {
                Logger.d(TAG, worksheet.getTitle() + Constants.Logs.W_SHEET_ADDED + String.valueOf(integer));
            }

            @Override
            public void onLoadError(Exception e) {
                Logger.e(TAG, worksheet.getTitle() + Constants.Logs.W_SHEET_ERR, e);
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
        ContentFromDatabase.getTestcaseByIdLink(idLink, new Callback<List<GEntryWorksheet>>() {
            @Override
            public void onLoadStart() {}

            @Override
            public void onLoadExecuted(List<GEntryWorksheet> entryWorksheets) {
                if (entryWorksheets != null && !entryWorksheets.isEmpty()) {
                    getContentCallback.resultOfDataLoading(entryWorksheets.get(0));
                } else {
                    getContentCallback.resultOfDataLoading(null);
                }
            }

            @Override
            public void onLoadError(Exception e) {
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
        ContentFromDatabase.setTestCase(testcase, new Callback<Integer>() {
            @Override
            public void onLoadStart() {}

            @Override
            public void onLoadExecuted(Integer integer) {
                Logger.d(TAG, Constants.Logs.TC_ADDED + String.valueOf(integer));
            }

            @Override
            public void onLoadError(Exception e) {
                Logger.e(TAG, Constants.Logs.TC_ERR, e);
            }
        });
    }

    public void getAllTestCases(String idSpreadsheetLink, final GetContentCallback<List<GEntryWorksheet>> getContentCallback) {
            getAllTestCasesInDB(idSpreadsheetLink, getContentCallback);
    }

    private void getAllTestCasesInDB(final String idSpreadsheetLink, final GetContentCallback<List<GEntryWorksheet>> getContentCallback) {
        if (!InputsUtil.isEmpty(idSpreadsheetLink)) {
            ContentFromDatabase.getTestCasesByLinkSpreadsheet(idSpreadsheetLink, new Callback<List<GEntryWorksheet>>() {
                @Override
                public void onLoadStart() {}

                @Override
                public void onLoadExecuted(List<GEntryWorksheet> entryWorksheets) {
                    if (entryWorksheets != null && !entryWorksheets.isEmpty()) {
                        getContentCallback.resultOfDataLoading(entryWorksheets);
                    } else {
                        getContentCallback.resultOfDataLoading(null);
                    }
                }

                @Override
                public void onLoadError(Exception e) {
                    Logger.e(TAG, e.getMessage(), e);
                    getContentCallback.resultOfDataLoading(null);
                }
            });
        } else {
            getContentCallback.resultOfDataLoading(null);
            Logger.d(TAG, Constants.Logs.S_SHEET_LINK_ERR);
        }
    }

    public void getTestcasesByIdLinksTestcases(String spreadsheetLink, ArrayList<String> testcasesIdLinks, final GetContentCallback<List<GEntryWorksheet>> getContentCallback) {
        if (!InputsUtil.isEmpty(spreadsheetLink) && testcasesIdLinks != null && !testcasesIdLinks.isEmpty()) {
            ContentFromDatabase.getTestcasesByIdLinksTestcases(spreadsheetLink, testcasesIdLinks, new Callback<List<GEntryWorksheet>>() {
                @Override
                public void onLoadStart() {}

                @Override
                public void onLoadExecuted(List<GEntryWorksheet> entryWorksheets) {
                    if (entryWorksheets != null && !entryWorksheets.isEmpty()) {
                        getContentCallback.resultOfDataLoading(entryWorksheets);
                    } else {
                        Logger.d(TAG, Constants.Logs.TC_NOT_FOUND);
                        getContentCallback.resultOfDataLoading(null);
                    }
                }

                @Override
                public void onLoadError(Exception e) {
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
            String[] tags = testCaseName.split(Constants.Symbols.TAG_DIVIDER);
            for (String tag : tags) {
                gTag = new GTag();
                gTag.setName(tag);
                gTag.setIdLinkTestCase(testCaseIdLink);
                gTag.setIdLinkSpreadsheet(spreadsheetIdLink);
                mTags.add(gTag);
            }
            ContentFromDatabase.setTags(mTags, new Callback<Integer>() {
                @Override
                public void onLoadStart() {}

                @Override
                public void onLoadExecuted(Integer integer) {
                    Logger.d(TAG, Constants.Logs.TAGS_ADDED + String.valueOf(integer));
                }

                @Override
                public void onLoadError(Exception e) {
                    Logger.e(TAG, Constants.Logs.TAGS_ERR, e);
                }
            });
        }
    }

    public void getAllTags(String spreadsheetIdLink, final GetContentCallback<List<GTag>> getContentCallback) {
        if (!InputsUtil.isEmpty(spreadsheetIdLink)) {
            ContentFromDatabase.getTagsByIdLinkSpreadsheet(spreadsheetIdLink, new Callback<List<GTag>>() {
                @Override
                public void onLoadStart() {

                }

                @Override
                public void onLoadExecuted(List<GTag> tags) {
                    if (tags != null) {
                        getContentCallback.resultOfDataLoading(tags);
                    } else {
                        getContentCallback.resultOfDataLoading(null);
                        Logger.d(TAG, Constants.Logs.TAGS_NOT_FOUND);
                    }
                }

                @Override
                public void onLoadError(Exception e) {
                    Logger.e(TAG, e.getMessage(), e);
                    getContentCallback.resultOfDataLoading(null);
                }
            });
        } else {
            getContentCallback.resultOfDataLoading(null);
            Logger.d(TAG, Constants.Logs.S_SHEET_LINK_ERR);
        }
    }

    public void getTagsByIdLinksTestcases(String spreadsheetLink, ArrayList<String> testcasesIdLinks, final GetContentCallback<List<GTag>> getContentCallback) {
        if (!InputsUtil.isEmpty(spreadsheetLink) && testcasesIdLinks != null && !testcasesIdLinks.isEmpty()) {
            ContentFromDatabase.getTagsByIdLinksTestcases(spreadsheetLink, testcasesIdLinks, new Callback<List<GTag>>() {
                @Override
                public void onLoadStart() {}

                @Override
                public void onLoadExecuted(List<GTag> tags) {
                    if (tags != null) {
                        getContentCallback.resultOfDataLoading(tags);
                    } else {
                        Logger.d(TAG, Constants.Logs.TAGS_NOT_FOUND);
                        getContentCallback.resultOfDataLoading(null);
                    }
                }

                @Override
                public void onLoadError(Exception e) {
                    Logger.e(TAG, e.getMessage(), e);
                    getContentCallback.resultOfDataLoading(null);
                }
            });
        } else {
            getContentCallback.resultOfDataLoading(null);
        }
    }
    //endregion
}
