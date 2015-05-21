package amtt.epam.com.amtt.database.task;

import amtt.epam.com.amtt.database.task.DataBaseTask.DataBaseResponse;

/**
 * Created by Artsiom_Kaliaha on 13.04.2015.
 */
public interface DataBaseCallback<ResultType> {

    void onDataBaseRequestPerformed(DataBaseResponse<ResultType> dataBaseResponse);

    void onDataBaseRequestError(Exception e);

}
