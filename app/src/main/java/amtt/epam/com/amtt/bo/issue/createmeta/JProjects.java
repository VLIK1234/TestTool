package amtt.epam.com.amtt.bo.issue.createmeta;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import amtt.epam.com.amtt.bo.issue.JAvatarUrls;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.table.ProjectTable;

/**
 @author Iryna Monchanka
 @version on 3/31/2015
 */

public class JProjects extends DatabaseEntity<JProjects> {

    @SerializedName("expand")
    private String mExpand;
    @SerializedName("self")
    private String mSelf;
    @SerializedName("id")
    private String mJiraId;
    @SerializedName("key")
    private String mKey;
    @SerializedName("name")
    private String mName;
    @SerializedName("avatarUrls")
    private JAvatarUrls mAvatarUrls;
    @SerializedName("issuetypes")
    private ArrayList<JIssueTypes> mIssueTypes;

    private int mId;
    private String mIdUser;

    public JProjects() {
    }

    public JProjects(Cursor cursor) {
        if (cursor.getPosition() == -1) {
            cursor.moveToNext();
        }
        mId = cursor.getInt(cursor.getColumnIndex(ProjectTable._ID));
        mJiraId = cursor.getString(cursor.getColumnIndex(ProjectTable._JIRA_ID));
        mKey = cursor.getString(cursor.getColumnIndex(ProjectTable._KEY));
        mName = cursor.getString(cursor.getColumnIndex(ProjectTable._NAME));
        mIdUser = cursor.getString(cursor.getColumnIndex(ProjectTable._ID_USER));
    }

    public JProjects(String expand, String self, String jiraId, String key, String name, JAvatarUrls avatarUrls, ArrayList<JIssueTypes> issueTypes) {
        this.mExpand = expand;
        this.mSelf = self;
        this.mJiraId = jiraId;
        this.mKey = key;
        this.mName = name;
        this.mAvatarUrls = avatarUrls;
        this.mIssueTypes = issueTypes;
    }

    public JProjects(String self, String id, String key, String name, JAvatarUrls avatarUrls, ArrayList<JIssueTypes> issueTypes) {
        this.mSelf = self;
        this.mJiraId = id;
        this.mKey = key;
        this.mName = name;
        this.mAvatarUrls = avatarUrls;
        this.mIssueTypes = issueTypes;
    }

    @Override
    public JProjects parse(Cursor cursor) {
        return new JProjects(cursor);
    }

    public ArrayList<String> getIssueTypesNames() {
        ArrayList<String> issueTypesNames;
        if (mIssueTypes==null) {
            issueTypesNames = null;
        } else {
            issueTypesNames = new ArrayList<>();
            int size = mIssueTypes.size();
            for (int i = 0; i < size; i++) {
                issueTypesNames.add(mIssueTypes.get(i).getName());
            }
        }
        return issueTypesNames;
    }

    public String getExpand() {
        return mExpand;
    }

    public void setExpand(String expand) {
        this.mExpand = expand;
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

    public void setJiraId(String jiraId) {
        this.mJiraId = jiraId;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public JAvatarUrls getAvatarUrls() {
        return mAvatarUrls;
    }

    public void setAvatarUrls(JAvatarUrls avatarUrls) {
        this.mAvatarUrls = avatarUrls;
    }

    public ArrayList<JIssueTypes> getIssueTypes() {
        return mIssueTypes;
    }

    public void setIssueTypes(ArrayList<JIssueTypes> issueTypes) {
        this.mIssueTypes = issueTypes;
    }

    public String getIdUser() {
        return mIdUser;
    }

    public void setIdUser(String idUser) {
        this.mIdUser = idUser;
    }

    public JIssueTypes getIssueTypeByName(String issueName) {
        JIssueTypes issueType = null;
        for (JIssueTypes type : mIssueTypes) {
            if (type.getName().equals(issueName)) {
                issueType = type;
            }
        }
        return issueType;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public Uri getUri() {
        return AmttUri.PROJECT.get();
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(ProjectTable._JIRA_ID, mJiraId);
        values.put(ProjectTable._KEY, mKey);
        values.put(ProjectTable._NAME, mName);
        values.put(ProjectTable._ID_USER, mIdUser);
        return values;
    }
}
