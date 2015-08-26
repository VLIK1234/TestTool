package amtt.epam.com.amtt.bo.issue;

import com.google.gson.annotations.SerializedName;

/**
 @author Iryna Monchanka
 @version on 4/23/2015
 */

public class JIssueSecurity {

    @SerializedName("id")
    private String mId;

    public JIssueSecurity(){}

    public JIssueSecurity(String id) {
        this.mId = id;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }
}
