package amtt.epam.com.amtt.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import amtt.epam.com.amtt.CoreApplication;
import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.table.UsersTable;

/**
 * Created by Artyom on 10.05.2015.
 */
public class LoginItemAdapter extends CursorAdapter {

    public static class ViewHolder {

        ImageView mUserImage;
        TextView mUserName;

        public ImageView getImageView() {
            return mUserImage;
        }

        public TextView getTextView() {
            return mUserName;
        }

    }

    public LoginItemAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_login_dropdown_item, parent, false);

        ViewHolder vh = new ViewHolder();
        vh.mUserImage = (ImageView)view.findViewById(R.id.user_image);
        vh.mUserName = (TextView)view.findViewById(R.id.user_name);
        view.setTag(vh);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder)view.getTag();
        String userName = cursor.getString(cursor.getColumnIndex(UsersTable._USER_NAME));
        String avatar = cursor.getString(cursor.getColumnIndex(UsersTable._AVATAR_48));
        vh.mUserName.setText(userName);
        CoreApplication.getImageLoader().displayImage(avatar,vh.mUserImage);
    }

}
