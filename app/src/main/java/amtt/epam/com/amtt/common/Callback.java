package amtt.epam.com.amtt.common;

/**
 * Created by Artsiom_Kaliaha on 18.06.2015.
 */
public interface Callback<Result> {

    void onLoadStart();

    void onLoadExecuted(Result result);

    void onLoadError(Exception e);

}