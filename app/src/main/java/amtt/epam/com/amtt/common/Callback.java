package amtt.epam.com.amtt.common;

/**
 @author Artsiom_Kaliaha
 @version on 18.06.2015
 */

public interface Callback<Result> {

    void onLoadStart();

    void onLoadExecuted(Result result);

    void onLoadError(Exception e);

}