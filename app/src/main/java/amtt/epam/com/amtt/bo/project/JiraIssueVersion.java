package amtt.epam.com.amtt.bo.project;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 4/23/2015.
 */
public class JiraIssueVersion {

    @SerializedName("id")
    private String mId;
    @SerializedName("self")
    private String mSelf;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("name")
    private String mName;
    @SerializedName("archived")
    private Boolean mArchived;
    @SerializedName("released")
    private Boolean mReleased;
    @SerializedName("releaseDate")
    private String mReleaseDate;
    @SerializedName("overdue")
    private Boolean mOverdue;
    @SerializedName("userReleaseDate")
    private String mUserReleaseDate;
    //@SerializedName("projectId")
   // private int mProjectId;

    public JiraIssueVersion(){}

    public JiraIssueVersion(String id) {
        this.mId = id;
    }

    public JiraIssueVersion(String id, String self, String description, String name, Boolean archived, Boolean released, String releaseDate, Boolean overdue, String userReleaseDate, int projectId) {
        this.mId = id;
        this.mSelf = self;
        this.mDescription = description;
        this.mName = name;
        this.mArchived = archived;
        this.mReleased = released;
        this.mReleaseDate = releaseDate;
        this.mOverdue = overdue;
        this.mUserReleaseDate = userReleaseDate;
        //this.mProjectId = projectId;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Boolean getArchived() {
        return mArchived;
    }

    public void setArchived(Boolean archived) {
        this.mArchived = archived;
    }

    public Boolean getReleased() {
        return mReleased;
    }

    public void setReleased(Boolean released) {
        this.mReleased = released;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public Boolean getOverdue() {
        return mOverdue;
    }

    public void setOverdue(Boolean overdue) {
        this.mOverdue = overdue;
    }

    public String getUserReleaseDate() {
        return mUserReleaseDate;
    }

    public void setUserReleaseDate(String userReleaseDate) {
        this.mUserReleaseDate = userReleaseDate;
    }
/*
    public int getProjectId() {
        return mProjectId;
    }

    public void setProjectId(int projectId) {
        this.mProjectId = projectId;
    }*/
}