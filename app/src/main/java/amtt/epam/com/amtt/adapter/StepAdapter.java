package amtt.epam.com.amtt.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.table.ActivityInfoTable;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.database.table.StepsWithMetaTable;
import amtt.epam.com.amtt.loader.InternalStorageImageLoader;

/**
 * Created by Artsiom_Kaliaha on 31.03.2015.
 */
public class StepAdapter extends CursorAdapter {

    private static class ViewHolder {
        ImageView mImageView;
        TextView mActivityInfo;
        TextView mStep;
    }

    private static final InternalStorageImageLoader sImageLoader;
    public static final int IMAGE_VIEW_WIDTH = 360;
    public static final int IMAGE_VIEW_HEIGHT = 640;

    static {
        sImageLoader = new InternalStorageImageLoader(10, IMAGE_VIEW_WIDTH, IMAGE_VIEW_HEIGHT);
    }

    public StepAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_step, parent, false);

        ViewHolder vh = new ViewHolder();
        vh.mImageView = (ImageView) view.findViewById(R.id.screenshot_image);
        vh.mActivityInfo = (TextView) view.findViewById(R.id.activity_info_text);
        vh.mStep = (TextView) view.findViewById(R.id.step_text);
        view.setTag(vh);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder vh = (ViewHolder) view.getTag();
        String activityInfo = context.getString(R.string.label_activity) + cursor.getString(cursor.getColumnIndex(StepsTable._ASSOCIATED_ACTIVITY)) + "\n" +
                context.getString(R.string.label_configuration_changes) + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._CONFIG_CHANGES)) + "\n" +
                context.getString(R.string.label_flags) + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._FLAGS)) + "\n" +
                context.getString(R.string.label_launch_mode) + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._LAUNCH_MODE)) + "\n" +
                context.getString(R.string.label_max_recents) + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._MAX_RECENTS)) + "\n" +
                context.getString(R.string.label_parent_app) + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PARENT_ACTIVITY_NAME)) + "\n" +
                context.getString(R.string.label_permission) + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PERMISSION)) + "\n" +
                context.getString(R.string.label_persistable_mode) + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PERSISTABLE_MODE)) + "\n" +
                context.getString(R.string.label_screen_orientation) + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._SCREEN_ORIENTATION)) + "\n" +
                context.getString(R.string.label_soft_input_mode) + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._SOFT_INPUT_MODE)) + "\n" +
                context.getString(R.string.label_target_app_name) + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._TARGET_ACTIVITY_NAME)) + "\n" +
                context.getString(R.string.label_task_affinity) + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._TASK_AFFINITY)) + "\n" +
                context.getString(R.string.label_ui_options) + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._UI_OPTIONS)) + "\n" +
                context.getString(R.string.label_process_name) + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PROCESS_NAME)) + "\n" +
                context.getString(R.string.label_package_name) + cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PACKAGE_NAME)) + "\n";

        vh.mActivityInfo.setText(activityInfo);
        vh.mStep.setText(context.getString(R.string.label_step) + cursor.getString(cursor.getColumnIndex(StepsWithMetaTable._ID)));

        setBitmap(vh.mImageView, cursor.getString(cursor.getColumnIndex(StepsTable._SCREEN_PATH)));
    }

    private void setBitmap(ImageView imageView, String path) {
        sImageLoader.load(imageView, path);
    }

}
