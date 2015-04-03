package amtt.epam.com.amtt.bo.issue.user;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.issue.willrefactored.JiraAvatarUrls;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */
public class JiraUserInfo {

    @SerializedName("mKey")
    private String mKey;
    @SerializedName("self")
    private String mSelf;
    @SerializedName("name")
    private String mName;
    @SerializedName("avatarUrls")
    private JiraAvatarUrls mAvatarUrls;
    @SerializedName("emailAddress")
    private String mEmailAddress;
    @SerializedName("displayName")
    private String mDisplayName;
    @SerializedName("active")
    private Boolean mActive;
    @SerializedName("timeZone")
    private String mTimeZone;
    @SerializedName("locale")
    private String mLocale;
    @SerializedName("groups")
    private JiraUserGroup mGroups;
    @SerializedName("expand")
    private String mExpand;

    public JiraUserInfo(){}

    public JiraUserInfo(String key, String self, String name, JiraAvatarUrls avatarUrls, String emailAddress, String displayName, Boolean active, String timeZone, String locale, JiraUserGroup groups, String expand) {
        this.mKey = key;
        this.mSelf = self;
        this.mName = name;
        this.mAvatarUrls = avatarUrls;
        this.mEmailAddress = emailAddress;
        this.mDisplayName = displayName;
        this.mActive = active;
        this.mTimeZone = timeZone;
        this.mLocale = locale;
        this.mGroups = groups;
        this.mExpand = expand;
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

    public Boolean getActive() {
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

    public String getLocale() {
        return mLocale;
    }

    public void setLocale(String locale) {
        this.mLocale = locale;
    }

    public JiraUserGroup getGroups() {
        return mGroups;
    }

    public void setGroups(JiraUserGroup groups) {
        this.mGroups = groups;
    }

    public String getExpand() {
        return mExpand;
    }

    public void setExpand(String expand) {
        this.mExpand = expand;
    }
}




