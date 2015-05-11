package amtt.epam.com.amtt.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import amtt.epam.com.amtt.R;

/**
 * Created by Iryna_Monchanka on 5/11/2015.
 */
public class ProjectsAdapter  extends CursorAdapter {

    private static class ViewHolder {
        TextView mActivityInfo;
        TextView mStep;
    }

    public ProjectsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_projects, parent, false);

        ViewHolder vh = new ViewHolder();

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder vh = (ViewHolder) view.getTag();

    }

}
