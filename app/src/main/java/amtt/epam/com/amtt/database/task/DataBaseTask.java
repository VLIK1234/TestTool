package amtt.epam.com.amtt.database.task;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by Artsiom_Kaliaha on 13.04.2015.
 */
public class DataBaseTask {

    private static class DataBaseTaskHolder {

        private static final DataBaseTask DATA_BASE_TASK = new DataBaseTask();

    }

    private Context mContext;

    private DataBaseTask() { }

    public static DataBaseTask getInstance(Context context) {
        DataBaseTask dataBaseTask = DataBaseTaskHolder.DATA_BASE_TASK;
        dataBaseTask.setContext(context);
        return dataBaseTask;
    }

    private void setContext(Context context) {
        mContext = context;
    }

    public void clear() {
        new DbClearTask(mContext).execute();
    }

    public void saveStep(Context context, StepSavingCallback callback, Bitmap bitmap, Rect rect, ComponentName componentName, int stepNumber) {
        new StepSavingTask(context, callback, bitmap, rect, componentName, stepNumber).execute();
    }

}
