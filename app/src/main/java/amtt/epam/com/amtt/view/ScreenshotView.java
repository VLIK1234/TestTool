package amtt.epam.com.amtt.view;

import android.content.Context;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.task.DataBaseCallback;
import amtt.epam.com.amtt.database.task.DataBaseOperationType;
import amtt.epam.com.amtt.database.task.DataBaseTask;
import amtt.epam.com.amtt.database.task.DataBaseTaskResult;

/**
 * Created by Ivan_Bakach on 12.05.2015.
 */
public class ScreenshotView extends TopUnitView implements DataBaseCallback {

    //Database fields
    private static int sStepNumber; //responsible for steps ordering in database
    private int mScreenNumber; //responsible for nonrecurring screenshot names

    public ScreenshotView(Context context) {
        super(context, context.getString(R.string.label_screenshot));
    }

    @Override
    protected void onTouchAction() {
        Toast.makeText(getContext(), getContext().getString(R.string.label_screenshot)+" Вова, что здесь должно быть?", Toast.LENGTH_LONG).show();
        sStepNumber++;
        new DataBaseTask.Builder()
                .setOperationType(DataBaseOperationType.SAVE_STEP)
                .setContext(getContext())
                .setCallback(ScreenshotView.this)
                .setStepNumber(sStepNumber)
                .create()
                .execute();
    }

    @Override
    public void onDataBaseActionDone(DataBaseTaskResult result) {
        int resultMessage;
        switch (result) {
            case DONE:
                resultMessage = R.string.data_base_action_done;
                break;
            case ERROR:
                resultMessage = R.string.data_base_action_error;
                break;
            default:
                resultMessage = R.string.data_base_cleared;
                break;
        }
        Toast.makeText(getContext(), resultMessage, Toast.LENGTH_SHORT).show();
    }
}
