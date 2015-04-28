package amtt.epam.com.amtt.bo;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.issue.JiraIssueFields;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class JCreatingIssueRequest {
    @SerializedName("fields")
    private JiraIssueFields mFields;

    public JCreatingIssueRequest() {
    }

    public JCreatingIssueRequest(JiraIssueFields fields) {
        this.mFields = fields;
    }

    public JiraIssueFields getFields() {
        return mFields;
    }

    public void setFields(JiraIssueFields fields) {
        this.mFields = fields;
    }
}
