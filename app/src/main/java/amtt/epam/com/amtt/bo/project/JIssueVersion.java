package amtt.epam.com.amtt.bo.project;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.JBase;

/**
 @author Iryna Monchanka
 @version on 4/23/2015
 */

public class JIssueVersion extends JBase{

    @SerializedName("description")
    private String mDescription;
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

    public JIssueVersion(){}

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
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