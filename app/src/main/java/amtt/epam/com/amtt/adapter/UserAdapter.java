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
import amtt.epam.com.amtt.database.table.UsersTable;

/**
 * Created by Artsiom_Kaliaha on 04.05.2015.
 */
public class UserAdapter extends CursorAdapter {

    private static class ViewHolder {
        ImageView mUserImage;
        TextView mUserName;
    }

    public UserAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View layout = LayoutInflater.from(context).inflate(R.layout.adapter_user, parent, false);

        ViewHolder vh = new ViewHolder();
        vh.mUserImage = (ImageView)layout.findViewById(R.id.user_image);
        vh.mUserName = (TextView)layout.findViewById(R.id.user_name);
        layout.setTag(vh);
        return layout;
    }



    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder)view.getTag();
        String userName = cursor.getString(cursor.getColumnIndex(UsersTable._USER_NAME));
        vh.mUserName.setText(userName);
    }

}