package amtt.epam.com.amtt.bo.project;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.user.JUser;

/**
 @author Iryna Monchanka
 @version on 4/23/2015
 */

public class JComponent {

    @SerializedName("id")
    private String mId;
    @SerializedName("self")
    private String mSelf;
    @SerializedName("name")
    private String mName;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("lead")
    private JUser mLead;
    @SerializedName("assigneeType")
    private String mAssigneeType;
    @SerializedName("assignee")
    private JUser mAssignee;
    @SerializedName("realAssigneeType")
    private String mRealAssigneeType;
    @SerializedName("realAssignee")
    private JUser mRealAssignee;
    @SerializedName("isAssigneeTypeValid")
    private Boolean mIsAssigneeTypeValid;
    @SerializedName("project")
    private String mProjectKey;
    @SerializedName("projectId")
    private String mProjectId;

    public JComponent(String id) {
        this.mId = id;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}
