package amtt.epam.com.amtt.googleapi.api;

import org.apache.http.HttpEntity;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.http.Request;
import amtt.epam.com.amtt.processing.Processor;
import amtt.epam.com.amtt.util.ThreadManager;

/**
 * @author Iryna Monchanka
 * @version on 07.07.2015
 */

public class GSpreadsheetApi<Entity extends DatabaseEntity> {

    private static final GSpreadsheetApi INSTANCE;

    static {
        INSTANCE = new GSpreadsheetApi();
    }

    private GSpreadsheetApi() {
    }

    public static GSpreadsheetApi get() {
        return INSTANCE;
    }

    public void loadDocument(String url, Processor<HttpEntity, Entity> processor, Callback<Entity> callback) {
        Request.Builder requestBuilder = new Request.Builder()
                .setType(Request.Type.GET)
                .setUrl(url);
        execute(requestBuilder, processor, callback);
    }

    private void execute(Request.Builder requestBuilder, Processor<HttpEntity, Entity> processor, Callback<Entity> callback) {
        Request request = requestBuilder.build();
        ThreadManager.execute(request, AmttApplication.getHttpClient(), processor, callback);
    }
}
