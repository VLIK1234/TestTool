package amtt.epam.com.amtt.adapter;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.table.ActivityInfoTable;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.database.table.StepsWithMetaTable;
import amtt.epam.com.amtt.loader.InternalStorageImageLoader;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Artsiom_Kaliaha on 31.03.2015.
 */
public class StepsAdapter extends CursorAdapter {

    public static class ViewHolder {
        public ImageView mImageView;
        public TextView mActivityInfo;
        public TextView mStep;
        public ProgressBar mProgressBar;
    }

    private final InternalStorageImageLoader mImageLoader;

    public StepsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mImageLoader = new InternalStorageImageLoader();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_steps, parent, false);

        ViewHolder vh = new ViewHolder();
        vh.mImageView = (ImageView) view.findViewById(R.id.screenshot_image);
        vh.mActivityInfo = (TextView) view.findViewById(R.id.activity_info_text);
        vh.mStep = (TextView) view.findViewById(R.id.step_text);
        vh.mProgressBar = (ProgressBar) view.findViewById(android.R.id.progress);
        view.setTag(vh);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder vh = (ViewHolder) view.getTag();

        String activityInfo = "Activity: " + cursor.getString(cursor.getColumnIndex(StepsTable._ASSOCIATED_ACTIVITY)) + "\n" +
            "Configuration changes: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._CONFIG_CHANGES)) + "\n" +
            "Flags: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._FLAGS)) + "\n" +
            "Launch mode: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._LAUNCH_MODE)) + "\n" +
            "Max recents: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._MAX_RECENTS)) + "\n" +
            "Parent activity: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PARENT_ACTIVITY_NAME)) + "\n" +
            "Permission: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PERMISSION)) + "\n" +
            "Persistable mode: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PERSISTABLE_MODE)) + "\n" +
            "Screen orientation: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._SCREEN_ORIENTATION)) + "\n" +
            "Soft input mode: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._SOFT_INPUT_MODE)) + "\n" +
            "Target activity name: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._TARGET_ACTIVITY_NAME)) + "\n" +
            "Task affinity: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._TASK_AFFINITY)) + "\n" +
            "Theme: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._THEME)) + "\n" +
            "Ui options: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._UI_OPTIONS)) + "\n" +
            "Process name: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PROCESS_NAME)) + "\n" +
            "Package name: " + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PACKAGE_NAME)) + "\n";

        vh.mActivityInfo.setText(activityInfo);
        vh.mStep.setText("Step " + cursor.getString(cursor.getColumnIndex(StepsWithMetaTable._ID)));

        setBitmap(vh, cursor.getString(cursor.getColumnIndex(StepsTable._SCREEN_PATH)));
    }

    private void setBitmap(ViewHolder vh, String path) {
        mImageLoader.load(vh, path);
    }

}
