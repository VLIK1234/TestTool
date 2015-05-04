package amtt.epam.com.amtt.bo.issue;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 4/23/2015.
 */
public class JiraIssueComponent {

    @SerializedName("id")
    private String mId;

    public JiraIssueComponent(){}

    public JiraIssueComponent(String id) {
        this.mId = id;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }
}
