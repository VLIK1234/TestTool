package amtt.epam.com.amtt.bo.issue.willrefactored.issuekey;

import amtt.epam.com.amtt.bo.issue.willrefactored.JiraAvatarUrls;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class JiraIssueProject {


    @SerializedName("id")
    private String mId;
    @SerializedName("key")
    private String mKey;
    private String self;
    private String name;
    private JiraAvatarUrls avatarUrls;


    public JiraIssueProject() {
    }

    public JiraIssueProject(String id, String key) {
        this.mId = id;
        this.mKey = key;
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
}
