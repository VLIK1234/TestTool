package amtt.epam.com.amtt.bo.issue;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class JIssueProject {


    @SerializedName("id")
    private String mId;
    @SerializedName("key")
    private String mKey;
    @SerializedName("self")
    private String mSelf;
    @SerializedName("name")
    private String mName;
    @SerializedName("avatarUrls")
    private JAvatarUrls mAvatarUrls;


    public JIssueProject() {
    }

    public JIssueProject(String id, String key, String self, String name, JAvatarUrls avatarUrls) {
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

    public JAvatarUrls getAvatarUrls() {
        return mAvatarUrls;
    }

    public void setAvatarUrls(JAvatarUrls avatarUrls) {
        this.mAvatarUrls = avatarUrls;
    }
}