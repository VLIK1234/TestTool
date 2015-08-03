package amtt.epam.com.amtt.bo.user;

import amtt.epam.com.amtt.bo.issue.JAvatarUrls;
import com.google.gson.annotations.SerializedName;

/**
 @author Iryna Monchanka
 @version on 30.03.2015
 */

public class JUser {

    @SerializedName("key")
    private String mKey;
    @SerializedName("self")
    private String mSelf;
    @SerializedName("name")
    private String mName;
    @SerializedName("avatarUrls")
    private JAvatarUrls mAvatarUrls;
    @SerializedName("emailAddress")
    private String mEmailAddress;
    @SerializedName("displayName")
    private String mDisplayName;
    @SerializedName("active")
    private Boolean mActive;
    @SerializedName("timeZone")
    private String mTimeZone;

    public JUser(){}

    public JUser(String key, String self, String name, JAvatarUrls avatarUrls, String emailAddress, String displayName, Boolean active, String timeZone) {
        this.mKey = key;
        this.mSelf = self;
        this.mName = name;
        this.mAvatarUrls = avatarUrls;
        this.mEmailAddress = emailAddress;
        this.mDisplayName = displayName;
        this.mActive = active;
        this.mTimeZone = timeZone;
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

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.mEmailAddress = emailAddress;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public Boolean isActive() {
        return mActive;
    }

    public void setActive(Boolean active) {
        this.mActive = active;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        this.mTimeZone = timeZone;
    }
}
