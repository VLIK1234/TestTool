package amtt.epam.com.amtt.database;

/**
 * Created by Artsiom_Kaliaha on 26.03.2015.
 */
public interface StepSavingCallback {

    void onStepSaved(StepSavingResult result);

    int getScreenNumber();

    void incrementScreenNumber();

}
