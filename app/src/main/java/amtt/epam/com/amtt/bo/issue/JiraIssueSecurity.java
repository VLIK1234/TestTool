package amtt.epam.com.amtt.bo.issue;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 4/23/2015.
 */
public class JiraIssueSecurity {

    @SerializedName("id")
    private String mId;

    public JiraIssueSecurity(){}

    public JiraIssueSecurity(String id) {
        this.mId = id;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }
}
