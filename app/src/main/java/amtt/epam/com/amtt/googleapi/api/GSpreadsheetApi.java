package amtt.epam.com.amtt.googleapi.api;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.http.Request;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.ThreadManager;

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

    public void loadDocument(String url, Processor processor, Callback callback) {
        Request.Builder requestBuilder = new Request.Builder()
                .setType(Request.Type.GET)
                .setUrl(url);
        execute(requestBuilder, processor, callback);
    }

    private void execute(Request.Builder requestBuilder, Processor processor, Callback callback) {
        Request request = requestBuilder.build();
        ThreadManager.execute(request, AmttApplication.getHttpClient(), processor, callback);
    }
}
