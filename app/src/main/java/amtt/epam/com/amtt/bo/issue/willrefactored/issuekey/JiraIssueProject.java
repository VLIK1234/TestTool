package amtt.epam.com.amtt.bo.issue.willrefactored.issuekey;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.issue.willrefactored.JiraAvatarUrls;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class JiraIssueProject {


    @SerializedName("id")
    private String mId;
    @SerializedName("key")
    private String mKey;
    private String self;
    @SerializedName("name")
    private String mName;
    private JiraAvatarUrls avatarUrls;


    public JiraIssueProject() {
    }

    public JiraIssueProject(String id, String name) {
        this.mId = id;
        this.mName = name;
    }

    public String getIdProject() {
        return mId;
    }

    public void setIdProject(String id) {
        this.mId = id;
    }

    public String getKeyProject() {
        return mKey;
    }

    public void setKeyProject(String key) {
        this.mKey = key;
    }

    public String getNameProject() {
        return mName;
    }

    public void setNameProject(String mName) {
        this.mName = mName;
    }
}
