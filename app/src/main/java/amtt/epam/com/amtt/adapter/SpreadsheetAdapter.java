package amtt.epam.com.amtt.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.googleapi.database.table.SpreadsheetTable;

/**
 * @author Iryna Monchanka
 * @version on 06.08.2015
 */

public class SpreadsheetAdapter extends CursorAdapter {

    private static class ViewHolder {
        TextView mTitleSpreadsheet;
        TextView mUrlSpreadsheet;
    }

    public SpreadsheetAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View layout = LayoutInflater.from(context).inflate(R.layout.adapter_spreadsheet, parent, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.mTitleSpreadsheet = (TextView)layout.findViewById(R.id.tv_spreadsheet_title);
        viewHolder.mUrlSpreadsheet = (TextView) layout.findViewById(R.id.tv_spreadsheet_url);
        layout.setTag(viewHolder);
        return layout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder)view.getTag();
        String title = cursor.getString(cursor.getColumnIndex(SpreadsheetTable._TITLE));
        String url = cursor.getString(cursor.getColumnIndex(SpreadsheetTable._SPREADSHEET_ID_LINK));
        vh.mTitleSpreadsheet.setText(title);
        vh.mUrlSpreadsheet.setText(url);
    }

}
