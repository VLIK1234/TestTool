package amtt.epam.com.amtt.bo.issue.createmeta;

import amtt.epam.com.amtt.bo.issue.willrefactored.JiraAvatarUrls;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */
public class JProjects {

    @SerializedName("expand")
    private String mExpand;
    @SerializedName("self")
    private String mSelf;
    @SerializedName("id")
    private String mId;
    @SerializedName("key")
    private String mKey;
    @SerializedName("name")
    private String mName;
    @SerializedName("avatarUrls")
    private JiraAvatarUrls mAvatarUrls;
    @SerializedName("issuetypes")
    private ArrayList<JIssueTypes> mIssuetypes;

    public JProjects() {
    }

    public JProjects(String expand, String self, String id, String key, String name, JiraAvatarUrls avatarUrls, ArrayList<JIssueTypes> issuetypes) {
        this.mExpand = expand;
        this.mSelf = self;
        this.mId = id;
        this.mKey = key;
        this.mName = name;
        this.mAvatarUrls = avatarUrls;
        this.mIssuetypes = issuetypes;
    }

    public JProjects(String self, String id, String key, String name, JiraAvatarUrls avatarUrls, ArrayList<JIssueTypes> issuetypes) {
        this.mSelf = self;
        this.mId = id;
        this.mKey = key;
        this.mName = name;
        this.mAvatarUrls = avatarUrls;
        this.mIssuetypes = issuetypes;
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

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
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

    public JiraAvatarUrls getAvatarUrls() {
        return mAvatarUrls;
    }

    public void setAvatarUrls(JiraAvatarUrls avatarUrls) {
        this.mAvatarUrls = avatarUrls;
    }

    public ArrayList<JIssueTypes> getIssuetypes() {
        return mIssuetypes;
    }

    public void setIssuetypes(ArrayList<JIssueTypes> issuetypes) {
        this.mIssuetypes = issuetypes;
    }
}
