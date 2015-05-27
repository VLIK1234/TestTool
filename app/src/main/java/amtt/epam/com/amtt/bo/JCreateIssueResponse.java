package amtt.epam.com.amtt.bo;

import com.google.gson.annotations.SerializedName;

/**
 @author Iryna Monchanka
 @version on 27.05.2015
 */

public class JCreateIssueResponse {

    @SerializedName("id")
    private String mId;
    @SerializedName("key")
    private String mKey;
    @SerializedName("self")
    private String mSelf;

    public JCreateIssueResponse() {
    }

    public JCreateIssueResponse(String id, String key, String self) {
        this.mId = id;
        this.mKey = key;
        this.mSelf = self;
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

    public String getSelf() {
        return mSelf;
    }

    public void setSelf(String self) {
        this.mSelf = self;
    }

}
