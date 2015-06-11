package amtt.epam.com.amtt.api.http;

/**
 * Created by Artsiom_Kaliaha on 11.06.2015.
 */
public interface HttpCallback {

    void onTaskStart();

    void onTaskExecuted(HttpResult httpResult);

    void onTaskError(AmttHttpException e);

}
