package amtt.epam.com.amtt.bo.issue.createmeta.issuetypes;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.issue.JAvatarUrls;

/**
 @author Iryna Monchanka
 @version on 3/31/2015
 */

public class JFieldsItemAllowedValues {

    @SerializedName("avatarUrls")
    private JAvatarUrls mAvatarUrls;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("iconUrl")
    private String mIconUrl;
    @SerializedName("id")
    private String mId;
    @SerializedName("key")
    private String mKey;
    @SerializedName("name")
    private String mName;
    @SerializedName("self")
    private String mSelf;
    @SerializedName("subtask")
    private Boolean mSubTask;

    public JFieldsItemAllowedValues() {
    }

    public JFieldsItemAllowedValues(JAvatarUrls avatarUrls, String description, String iconUrl, String id, String key, String name, String self, Boolean subTask) {
        this.mAvatarUrls = avatarUrls;
        this.mDescription = description;
        this.mIconUrl = iconUrl;
        this.mId = id;
        this.mKey = key;
        this.mName = name;
        this.mSelf = self;
        this.mSubTask = subTask;
    }

    public JAvatarUrls getAvatarUrls() {
        return mAvatarUrls;
    }

    public void setAvatarUrls(JAvatarUrls avatarUrls) {
        this.mAvatarUrls = avatarUrls;
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

    public String getSelf() {
        return mSelf;
    }

    public void setSelf(String self) {
        this.mSelf = self;
    }

    public Boolean getSubTask() {
        return mSubTask;
    }

    public void setSubTask(Boolean subTask) {
        this.mSubTask = subTask;
    }
}
