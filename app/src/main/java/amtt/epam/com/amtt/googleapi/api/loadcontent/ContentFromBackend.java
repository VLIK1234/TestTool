package amtt.epam.com.amtt.googleapi.api.loadcontent;

import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.api.ContentLoadingCallback;
import amtt.epam.com.amtt.googleapi.api.GSpreadsheetApi;
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

    public <Response> void getSpreadsheet(String idLink, ContentLoadingCallback<Response, Response> contentLoadingCallback,
                               GetContentCallback<Response> getContentCallback) {
        GSpreadsheetApi.get().loadDocument(idLink, SpreadsheetProcessor.NAME,
                getCallback(contentLoadingCallback, getContentCallback));
    }

    public <Response> void getWorksheet(String worksheetKey, ContentLoadingCallback<Response, Response> contentLoadingCallback,
                             GetContentCallback<Response> spreadsheetContentCallback) {
        GSpreadsheetApi.get().loadDocument(worksheetKey, WorksheetProcessor.NAME,
                getCallback(contentLoadingCallback, spreadsheetContentCallback));
    }

    private <Result> Callback getCallback(final ContentLoadingCallback<Result, Result> contentLoadingCallback,
                                          final GetContentCallback<Result> getContentCallback) {
        return new Callback<Result>() {
            @Override
            public void onLoadStart() {
            }

            @Override
            public void onLoadExecuted(Result result) {
                contentLoadingCallback.resultFromBackend(result, getContentCallback);
            }

            @Override
            public void onLoadError(Exception e) {
                contentLoadingCallback.resultFromBackend(null, getContentCallback);
            }
        };
    }
}
