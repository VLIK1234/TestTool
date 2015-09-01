package amtt.epam.com.amtt.bo;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.issue.JAvatarUrls;

/**
 * @author Iryna Monchanka
 * @version on 9/1/2015
 */
public class JBaseObject extends JBase {

    @SerializedName("key")
    protected String mKey;
    @SerializedName("avatarUrls")
    protected JAvatarUrls mAvatarUrls;

    public JBaseObject(){}

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public JAvatarUrls getAvatarUrls() {
        return mAvatarUrls;
    }

    public void setAvatarUrls(JAvatarUrls avatarUrls) {
        mAvatarUrls = avatarUrls;
    }
}
