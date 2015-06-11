package amtt.epam.com.amtt.bo.issue.createmeta;

/**
 @author Iryna Monchanka
 @version on 3/31/2015
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.issue.createmeta.issuetypes.JFields;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
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
public class JIssueTypes extends DatabaseEntity<JIssueTypes>{

    @SerializedName("self")
    private String mSelf;
    @SerializedName("id")
    private String mJiraId;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("iconUrl")
    private String mIconUrl;
    @SerializedName("name")
    private String mName;
    @SerializedName("subtask")
    private Boolean mSubTask;
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

    public JIssueTypes(String self, String jiraId, String description, String iconUrl, String name, Boolean subTask, String expand, JFields fields) {
        this.mSelf = self;
        this.mJiraId = jiraId;
        this.mDescription = description;
        this.mIconUrl = iconUrl;
        this.mName = name;
        this.mSubTask = subTask;
        this.mExpand = expand;
        this.mFields = fields;
    }

    @Override
    public JIssueTypes parse(Cursor cursor) {
        return new JIssueTypes(cursor);
    }

    public String getSelf() {
        return mSelf;
    }

    public void setSelf(String self) {
        this.mSelf = self;
    }

    public String getJiraId() {
        return mJiraId;
    }

    public void setJiraId(String id) {
        this.mJiraId = id;
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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Boolean getSubTask() {
        return mSubTask;
    }

    public void setSubTask(Boolean subTask) {
        this.mSubTask = subTask;
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
        return AmttUri.PRIORITY.get();
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
