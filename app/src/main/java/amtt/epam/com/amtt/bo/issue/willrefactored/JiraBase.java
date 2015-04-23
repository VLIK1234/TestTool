package amtt.epam.com.amtt.bo.issue.willrefactored;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.issue.JiraIssueFields;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class JiraBase {
    @SerializedName("fields")
    private JiraIssueFields mFields;

    public JiraBase() {
    }

    public JiraBase(JiraIssueFields fields) {
        this.mFields = fields;
    }

    public JiraIssueFields getFields() {
        return mFields;
    }

    public void setFields(JiraIssueFields fields) {
        this.mFields = fields;
    }
}
