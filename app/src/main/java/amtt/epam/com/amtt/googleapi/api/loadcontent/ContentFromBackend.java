package amtt.epam.com.amtt.googleapi.api.loadcontent;

import amtt.epam.com.amtt.api.ContentConst;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.api.ContentLoadingCallback;
import amtt.epam.com.amtt.googleapi.api.GSpreadsheetApi;
import amtt.epam.com.amtt.googleapi.bo.GSpreadsheet;
import amtt.epam.com.amtt.googleapi.bo.GWorksheet;
import amtt.epam.com.amtt.googleapi.processing.SpreadsheetProcessor;
import amtt.epam.com.amtt.googleapi.processing.WorksheetProcessor;

/**
 * @author Iryna Monchanka
 * @version on 07.07.2015
 */

public class ContentFromBackend {

    private static class ContentFromBackendHolder {
        public static final ContentFromBackend INSTANCE = new ContentFromBackend();
    }

    public static ContentFromBackend getInstance() {
        return ContentFromBackendHolder.INSTANCE;
    }

    public void getSpreadsheet(String idLink, ContentLoadingCallback<GSpreadsheet> contentLoadingCallback,
                               GetContentCallback getContentCallback) {
        GSpreadsheetApi.get().loadDocument(idLink, SpreadsheetProcessor.NAME,
                getCallback(ContentConst.SPREADSHEET_RESPONSE, contentLoadingCallback, getContentCallback));
    }

    public void getWorksheet(String worksheetKey, ContentLoadingCallback<GWorksheet> contentLoadingCallback,
                             GetContentCallback<GWorksheet> spreadsheetContentCallback) {
        GSpreadsheetApi.get().loadDocument(worksheetKey, WorksheetProcessor.NAME,
                getCallback(ContentConst.WORKSHEET_RESPONSE, contentLoadingCallback, spreadsheetContentCallback));
    }

    private <Result> Callback getCallback(final ContentConst requestType,
                                          final Result successResult,
                                          final Result errorResult,
                                          final ContentLoadingCallback<Result> contentLoadingCallback,
                                          final GetContentCallback getContentCallback) {
        return new Callback<Result>() {
            @Override
            public void onLoadStart() {
            }

            @Override
            public void onLoadExecuted(Result result) {
                contentLoadingCallback.resultFromBackend(successResult, requestType, getContentCallback);
            }

            @Override
            public void onLoadError(Exception e) {
                contentLoadingCallback.resultFromBackend(errorResult, requestType, getContentCallback);
            }
        };
    }

    private <Result> Callback getCallback(final ContentConst requestType,
                                          final ContentLoadingCallback<Result> contentLoadingCallback,
                                          final GetContentCallback getContentCallback) {
        return new Callback<Result>() {
            @Override
            public void onLoadStart() {
            }

            @Override
            public void onLoadExecuted(Result result) {
                contentLoadingCallback.resultFromBackend(result, requestType, getContentCallback);
            }

            @Override
            public void onLoadError(Exception e) {
                contentLoadingCallback.resultFromBackend(null, requestType, getContentCallback);
            }
        };
    }
}
