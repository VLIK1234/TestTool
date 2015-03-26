package amtt.epam.com.amtt.step;

/**
 * Created by Artsiom_Kaliaha on 26.03.2015.
 */
public interface StepSavingCallback {

    void onStepSaved(StepSavingResult result);

    int getScreenNumber();

}
