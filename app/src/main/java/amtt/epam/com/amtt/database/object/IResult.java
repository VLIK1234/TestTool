package amtt.epam.com.amtt.database.object;

/**
 * Created by Ivan_Bakach on 20.05.2015.
 */
public interface IResult<T> {
    void onResult(T result);
}
