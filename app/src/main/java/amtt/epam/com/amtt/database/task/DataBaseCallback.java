package amtt.epam.com.amtt.database.task;

/**
 * Created by Artsiom_Kaliaha on 13.04.2015.
 */
public interface DataBaseCallback<ResultType> {

    void onDataBaseActionDone(DataBaseResponse<ResultType> dataBaseResponse);

}
