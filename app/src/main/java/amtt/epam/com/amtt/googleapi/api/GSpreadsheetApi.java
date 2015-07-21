package amtt.epam.com.amtt.googleapi.api;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.common.DataRequest;
import amtt.epam.com.amtt.http.HttpClient;
import amtt.epam.com.amtt.http.Request;

/**
 * @author Iryna Monchanka
 * @version on 07.07.2015
 */

public class GSpreadsheetApi {

    private static final GSpreadsheetApi INSTANCE;

    static {
        INSTANCE = new GSpreadsheetApi();
    }

    private GSpreadsheetApi() {
    }

    public static GSpreadsheetApi get() {
        return INSTANCE;
    }

    public void loadDocument(String url, String processorName, Callback callback) {
        Request.Builder requestBuilder = new Request.Builder()
                .setType(Request.Type.GET)
                .setUrl(url);
        execute(requestBuilder, processorName, callback);
    }

    private void execute(Request.Builder requestBuilder, String processorName, Callback callback) {
        Request request = requestBuilder.build();
        AmttApplication.executeRequest(new DataRequest<>(HttpClient.NAME, request, processorName, callback));
    }
}
