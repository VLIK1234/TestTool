package amtt.epam.com.amtt.bo.issue.willrefactored.issuekey;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.issue.willrefactored.JiraIssueTypesIssueType;

/**
 * Created by Irina Monchenko on 26.03.2015.
 */
public class JiraBaseFields{

    @SerializedName("project")
    private JiraIssueProject mProject;
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("issuetype")
    private JiraIssueTypesIssueType mIssuetype;
  /*  private String timespent;
 private String[] fixVersions;
  private String aggregatetimespent;
  private String resolution;
   private String resolutiondate;
   private int workratio;
   private String lastViewed;
    private JiraIssueWatches watches;
    private String created;
   private String customfield_10000;
  private String customfield_10001;
   private String customfield_10002;
 private String customfield_10003;
    private String customfield_10004;
   private String customfield_10007;
    private String customfield_10008;

    private String customfield_10012;
    private String customfield_10013;
   private String customfield_10014;
   private String customfield_10015;
   private String customfield_10016;
    private String customfield_10017;
   private String customfield_10018;
   private String customfield_10019;
   private String customfield_10020;
    private String customfield_10021;
    private String customfield_10022;
    private String customfield_10023;
    private String customfield_10024;
    private String[] labels;
    private int timeestimate;
    private int aggregatetimeoriginalestimate;
    private String[] versions;
    private String[] issuelinks;
    private JiraIssueAssignee assignee;
    private JiraIssuePriority priority;
    private String updated;
    private JiraIssueStatusCategory statusCategory;
    private String[] components;
    private String timeoriginalestimate;
    private String aggregatetimeestimate;
    private JiraIssueCreator creator;
    private String environment;
    private String duedate;
    private JiraIssueProgress aggregateprogress;
    private JiraIssueProgress progress;
    private JiraIssueVotes votes;*/


    public JiraBaseFields() {
    }

    public JiraBaseFields(JiraIssueProject project, String summary, String description, JiraIssueTypesIssueType issuetype) {
        this.mProject = project;
        this.mSummary = summary;
        this.mDescription = description;
        this.mIssuetype = issuetype;
    }

    public JiraIssueProject getProject() {
        return mProject;
    }

    public void setProject(JiraIssueProject project) {
        this.mProject = project;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        this.mSummary = summary;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public JiraIssueTypesIssueType getIssuetype() {
        return mIssuetype;
    }

    public void setIssuetype(JiraIssueTypesIssueType issuetype) {
        this.mIssuetype = issuetype;
    }
}


