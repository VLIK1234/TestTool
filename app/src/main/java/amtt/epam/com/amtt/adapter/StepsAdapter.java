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
import amtt.epam.com.amtt.database.ActivityInfoTable;
import amtt.epam.com.amtt.database.StepsTable;

/**
 * Created by Artsiom_Kaliaha on 31.03.2015.
 */
public class StepsAdapter extends CursorAdapter {

    private static class ViewHolder {
        ImageView mImafeView;
        TextView mActivityInfo;
    }

    public StepsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_steps, parent, false);

        ViewHolder vh = new ViewHolder();
        vh.mImafeView = (ImageView) view.findViewById(R.id.screenshot_image);
        vh.mActivityInfo = (TextView) view.findViewById(R.id.activity_info_text);
        view.setTag(vh);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder) view.getTag();

        String activityInfo = cursor.getString(cursor.getColumnIndex(StepsTable._ASSOCIATED_ACTIVITY)) + "\n" +
                cursor.getString(cursor.getColumnIndex(ActivityInfoTable._CONFIG_CHANGES)) + "\n" +
                cursor.getString(cursor.getColumnIndex(ActivityInfoTable._FLAGS)) + "\n";

        vh.mActivityInfo.setText(activityInfo);
    }
}
