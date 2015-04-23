package amtt.epam.com.amtt.bo.issue.createmeta;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import amtt.epam.com.amtt.bo.issue.JiraAvatarUrls;

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
    private ArrayList<JIssueTypes> mIssueTypes;

    public JProjects() {
    }

    public JProjects(String expand, String self, String id, String key, String name, JiraAvatarUrls avatarUrls, ArrayList<JIssueTypes> issueTypes) {
        this.mExpand = expand;
        this.mSelf = self;
        this.mId = id;
        this.mKey = key;
        this.mName = name;
        this.mAvatarUrls = avatarUrls;
        this.mIssueTypes = issueTypes;
    }

    public JProjects(String self, String id, String key, String name, JiraAvatarUrls avatarUrls, ArrayList<JIssueTypes> issueTypes) {
        this.mSelf = self;
        this.mId = id;
        this.mKey = key;
        this.mName = name;
        this.mAvatarUrls = avatarUrls;
        this.mIssueTypes = issueTypes;
    }
    public ArrayList<String> getIssueTypesNames() {
        ArrayList<String> issueTypesNames = new ArrayList<>();
        int size = mIssueTypes.size();
        for (int i = 0; i < size; i++) {
            issueTypesNames.add(mIssueTypes.get(i).getName());
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

    public ArrayList<JIssueTypes> getIssueTypes() {
        return mIssueTypes;
    }

    public void setIssueTypes(ArrayList<JIssueTypes> issueTypes) {
        this.mIssueTypes = issueTypes;
    }
}
