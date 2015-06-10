package amtt.epam.com.amtt.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import amtt.epam.com.amtt.CoreApplication;
import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.bo.database.ActivityMeta;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbObjectManger;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.table.ActivityInfoTable;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.database.table.StepsWithMetaTable;

/**
 @author Artsiom_Kaliaha
 @version on 31.03.2015
 */

public class StepAdapter extends CursorAdapter {

    public interface ClickListner{
        void onItemRemove(int position);
        void onItemShow(int position);
    }

    private ClickListner clickListner;

    private static class ViewHolder {
        ImageView mImageView;
        ImageView mClose;
        TextView mActivityInfo;
        TextView mStep;
    }

    public StepAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_step, parent, false);

        ViewHolder vh = new ViewHolder();
        vh.mImageView = (ImageView) view.findViewById(R.id.screenshot_image);
        vh.mImageView.setMaxHeight(context.getResources().getDisplayMetrics().heightPixels/2);
        vh.mImageView.setMaxHeight(context.getResources().getDisplayMetrics().widthPixels/2);
        vh.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        vh.mClose = (ImageView) view.findViewById(R.id.iv_close);
        vh.mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DbObjectManger.INSTANCE.getAll(new Step(), new IResult<List<DatabaseEntity>>() {
//                    @Override
//                    public void onResult(List<DatabaseEntity> result) {
//                        Log.d("TAG", ((Step)result.get(0)).getActivity()+" name");
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//                });
            }
        });
        vh.mActivityInfo = (TextView) view.findViewById(R.id.activity_info_text);
        vh.mStep = (TextView) view.findViewById(R.id.step_text);
        view.setTag(vh);

        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("TAG", position+" position" + "class");
        return super.getView(position, convertView, parent);
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

        if (cursor.getString(cursor.getColumnIndex(StepsTable._SCREEN_PATH))!=null) {
            setBitmap(vh.mImageView, cursor.getString(cursor.getColumnIndex(StepsTable._SCREEN_PATH)));
        }
    }

    private void setBitmap(ImageView imageView, String path) {
        ImageLoader.getInstance().displayImage("file:///" + path, imageView);
    }
}
