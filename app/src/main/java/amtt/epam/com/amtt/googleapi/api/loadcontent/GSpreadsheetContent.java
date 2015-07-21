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

/**
 * @author Iryna Monchanka
 * @version on 7/6/2015
 */
public class GSpreadsheetContent {

    private GSpreadsheet mSpreadsheet;
    private GWorksheet mWorksheet;
    private GEntryWorksheet mLastTestCase;
    private String mLastTestcaseId;
    private List<GTag> mTags;

    private static class XMLContentHolder {
        public static final GSpreadsheetContent INSTANCE = new GSpreadsheetContent();
    }

    public static GSpreadsheetContent getInstance() {
        return XMLContentHolder.INSTANCE;
    }

    public void getSpreadsheet(final GetContentCallback<GSpreadsheet> getContentCallback) {
        if (mSpreadsheet != null) {
            getContentCallback.resultOfDataLoading(mSpreadsheet);
        } else {
            getSpreadsheetSynchronously(getContentCallback);
        }
    }

    public void setSpreadsheet(GSpreadsheet spreadsheet) {
        this.mSpreadsheet = spreadsheet;
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

    public void setWorksheet(GWorksheet worksheet) {
        this.mWorksheet = worksheet;
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
                            GSpreadsheetContent.getInstance().setSpreadsheet(result);
                            contentCallback.resultOfDataLoading(result);
                        } else {
                            contentCallback.resultOfDataLoading(null);
                        }
                    }
                }
            }
        }, getContentCallback);
    }

    private void getWorksheetAsynchronously(String worksheetKey, final GetContentCallback<GWorksheet> getContentCallback) {
        ContentFromBackend.getInstance().getWorksheetAsynchronously(worksheetKey, new ContentLoadingCallback<GWorksheet>() {
            @Override
            public void resultFromBackend(GWorksheet result, ContentConst tag, GetContentCallback contentCallback) {
                if (tag == ContentConst.WORKSHEET_RESPONSE) {
                    if (contentCallback != null) {
                        if (result != null) {
                            GSpreadsheetContent.getInstance().setWorksheet(result);
                            contentCallback.resultOfDataLoading(result);
                        } else {
                            contentCallback.resultOfDataLoading(null);
                        }
                    }
                }
            }
        }, getContentCallback);
    }

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

}
