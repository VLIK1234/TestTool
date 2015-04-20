package amtt.epam.com.amtt.database.task;

/**
 * Created by Artsiom_Kaliaha on 26.03.2015.
 */
public interface StepSavingCallback extends DataBaseGeneralCallback {

    int getScreenNumber();

    void incrementScreenNumber();

}
