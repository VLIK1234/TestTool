package amtt.epam.com.amtt.bo.project;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.JBase;
import amtt.epam.com.amtt.contentprovider.LocalUri;
import amtt.epam.com.amtt.database.table.PriorityTable;

/**
 @author Iryna Monchanka
 @version on 04.05.2015
 */

public class JPriority extends JBase {

    @SerializedName("statusColor")
    private String mStatusColor;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("iconUrl")
    private String mIconUrl;

    private String mUrl;
    private int mId;

    public JPriority(){}

    public JPriority(Cursor cursor) {
        if (cursor.getPosition() == -1) {
            cursor.moveToNext();
        }
        mId = cursor.getInt(cursor.getColumnIndex(PriorityTable._ID));
        mJiraId = cursor.getString(cursor.getColumnIndex(PriorityTable._JIRA_ID));
        mName = cursor.getString(cursor.getColumnIndex(PriorityTable._NAME));
        mUrl = cursor.getString(cursor.getColumnIndex(PriorityTable._URL));
    }

    public JPriority(String self, String statusColor, String description, String iconUrl, String name, String jiraId) {
        this.mSelf = self;
        this.mStatusColor = statusColor;
        this.mDescription = description;
        this.mIconUrl = iconUrl;
        this.mName = name;
        this.mJiraId = jiraId;
    }

    @Override
    public JPriority parse(Cursor cursor) {
        return new JPriority(cursor);
    }

    public String getStatusColor() {
        return mStatusColor;
    }

    public void setStatusColor(String statusColor) {
        this.mStatusColor = statusColor;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.mIconUrl = iconUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }
    @Override
    public int getId() {
        return mId;
    }

    @Override
    public Uri getUri() {
        return LocalUri.PRIORITY.get();
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(PriorityTable._JIRA_ID, mJiraId);
        values.put(PriorityTable._NAME, mName);
        values.put(PriorityTable._URL, mUrl);
        return values;
    }
}
