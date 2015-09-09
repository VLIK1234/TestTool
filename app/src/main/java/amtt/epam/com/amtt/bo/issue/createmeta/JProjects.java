package amtt.epam.com.amtt.bo.issue.createmeta;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.bo.JBaseObject;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.table.ProjectTable;

/**
 @author Iryna Monchanka
 @version on 3/31/2015
 */

public class JProjects extends JBaseObject {

    @SerializedName("expand")
    private String mExpand;
    @SerializedName("issuetypes")
    private List<JIssueTypes> mIssueTypes;

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

    @Override
    public JProjects parse(Cursor cursor) {
        return new JProjects(cursor);
    }

    public List<String> getIssueTypesNames() {
        List<String> issueTypesNames;
        if (mIssueTypes == null) {
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

    public List<JIssueTypes> getIssueTypes() {
        return mIssueTypes;
    }

    public void setIssueTypes(List<JIssueTypes> issueTypes) {
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
