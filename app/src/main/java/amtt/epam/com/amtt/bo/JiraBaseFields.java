package amtt.epam.com.amtt.bo;

/**
 * Created by shiza on 26.03.2015.
 */
public class JiraBaseFields extends Entity {

    private JiraProject project;
    private String summary;
    private String description;
    private JiraIssueType issuetype;
    // timetracking
    //private String parent = "parent";
    // customfield_11050

    public JiraBaseFields() {
    }

    public JiraBaseFields(JiraProject project, String summary, String description, JiraIssueType issuetype) {
        this.project = project;
        this.summary = summary;
        this.description = description;
        this.issuetype = issuetype;
    }

    public JiraProject getProject() {
        return project;
    }

    public void setProject(JiraProject project) {
        this.project = project;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JiraIssueType getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(JiraIssueType issuetype) {
        this.issuetype = issuetype;
    }
}


