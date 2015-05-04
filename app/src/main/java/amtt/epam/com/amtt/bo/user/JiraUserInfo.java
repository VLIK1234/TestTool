package amtt.epam.com.amtt.bo.user;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.issue.JiraAvatarUrls;

/**
 * Created by Iryna_Monchanka on 3/31/2015.
 */
public class JiraUserInfo extends JiraUser {

    @SerializedName("locale")
    private String mLocale;
    @SerializedName("groups")
    private JiraUserGroup mGroups;
    @SerializedName("expand")
    private String mExpand;

    public JiraUserInfo() {
    }

    public JiraUserInfo(String key, String self, String name, JiraAvatarUrls avatarUrls, String emailAddress, String displayName, Boolean active, String timeZone, String locale, JiraUserGroup groups, String expand) {
       super(key, self, name, avatarUrls, emailAddress, displayName, active, timeZone);
        this.mLocale = locale;
        this.mGroups = groups;
        this.mExpand = expand;
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
