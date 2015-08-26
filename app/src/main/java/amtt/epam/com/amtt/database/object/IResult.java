package amtt.epam.com.amtt.database.object;

/**
 @author Ivan_Bakach
 @version on 20.05.2015
 */

public interface IResult<T> {
    void onResult(T result);
    void onError(Exception e);
}
