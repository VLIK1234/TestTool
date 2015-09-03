package amtt.epam.com.amtt.bo;

import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.database.object.DatabaseEntity;

/**
 * @author Iryna Monchanka
 * @version on 9/1/2015
 */

public class JBase<T> extends DatabaseEntity<T> {

    @SerializedName("id")
    protected String mJiraId;
    @SerializedName("name")
    protected String mName;
    @SerializedName("self")
    protected String mSelf;

    public JBase(){}

    @Override
    public T parse(Cursor cursor) {
        return null;
    }

    public String getJiraId() {
        return mJiraId;
    }

    public void setJiraId(String jiraId) {
        mJiraId = jiraId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSelf() {
        return mSelf;
    }

    public void setSelf(String self) {
        mSelf = self;
    }
}
