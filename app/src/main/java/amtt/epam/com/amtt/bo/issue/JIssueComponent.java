package amtt.epam.com.amtt.bo.issue;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 4/23/2015.
 */
public class JIssueComponent {

    @SerializedName("id")
    private String mId;
    @SerializedName("self")
    private String mSelf;
    @SerializedName("name")
    private String mName;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("isAssigneeTypeValid")
    private Boolean mIsAssigneeTypeValid;

    public JIssueComponent(){}

    public JIssueComponent(String id) {
        this.mId = id;
    }

    public JIssueComponent(String id, String self, String name, String description, Boolean isAssigneeTypeValid) {
        this.mId = id;
        this.mSelf = self;
        this.mName = name;
        this.mDescription = description;
        this.mIsAssigneeTypeValid = isAssigneeTypeValid;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public Boolean isAssigneeTypeValid() {
        return mIsAssigneeTypeValid;
    }

    public void setIsAssigneeTypeValid(Boolean isAssigneeTypeValid) {
        this.mIsAssigneeTypeValid = isAssigneeTypeValid;
    }
}
