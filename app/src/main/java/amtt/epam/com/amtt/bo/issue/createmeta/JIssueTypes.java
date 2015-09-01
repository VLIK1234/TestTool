package amtt.epam.com.amtt.bo.issue.createmeta;

/**
 @author Iryna Monchanka
 @version on 3/31/2015
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.JBaseType;
import amtt.epam.com.amtt.bo.issue.createmeta.issuetypes.JFields;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.table.IssuetypeTable;

/**
 * JiraIssueTypes = JIssueTypes
 * <p/>
 * request URL : .../issue/createmeta ...
 * <p/>
 * response json : {...
 * projects{
 * ...
 * issuetypes{
 * }
 * }
 * }
 */
public class JIssueTypes extends JBaseType {

    @SerializedName("expand")
    private String mExpand;
    @SerializedName("fields")
    private JFields mFields;

    private int mId;
    private String mKeyProject;

    public JIssueTypes() {
    }

    public JIssueTypes(Cursor cursor) {
        if (cursor.getPosition() == -1) {
            cursor.moveToNext();
        }
        mId = cursor.getInt(cursor.getColumnIndex(IssuetypeTable._ID));
        mJiraId = cursor.getString(cursor.getColumnIndex(IssuetypeTable._JIRA_ID));
        mName = cursor.getString(cursor.getColumnIndex(IssuetypeTable._NAME));
        mKeyProject = cursor.getString(cursor.getColumnIndex(IssuetypeTable._KEY_PROJECT));
    }

    @Override
    public JIssueTypes parse(Cursor cursor) {
        return new JIssueTypes(cursor);
    }

    public String getExpand() {
        return mExpand;
    }

    public void setExpand(String expand) {
        this.mExpand = expand;
    }

    public JFields getFields() {
        return mFields;
    }

    public void setFields(JFields fields) {
        this.mFields = fields;
    }

    public String getKeyProject() {
        return mKeyProject;
    }

    public void setKeyProject(String keyProject) {
        this.mKeyProject = keyProject;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public Uri getUri() {
        return AmttUri.ISSUETYPE.get();
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(IssuetypeTable._JIRA_ID, mJiraId);
        values.put(IssuetypeTable._NAME, mName);
        values.put(IssuetypeTable._KEY_PROJECT, mKeyProject);
        return values;
    }
}
