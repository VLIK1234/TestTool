package amtt.epam.com.amtt.bo.issue;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class JiraIssueProject {


    @SerializedName("id")
    private String mId;
    @SerializedName("key")
    private String mKey;
    @SerializedName("self")
    private String mSelf;
    @SerializedName("name")
    private String mName;
    @SerializedName("avatarUrls")
    private JiraAvatarUrls mAvatarUrls;


    public JiraIssueProject() {
    }

    public JiraIssueProject(String id, String key, String self, String name, JiraAvatarUrls avatarUrls) {
        this.mId = id;
        this.mKey = key;
        this.mSelf = self;
        this.mName = name;
        this.mAvatarUrls = avatarUrls;
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

    public String getSelf() {
        return mSelf;
    }

    public void setSelf(String self) {
        this.mSelf = self;
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
}