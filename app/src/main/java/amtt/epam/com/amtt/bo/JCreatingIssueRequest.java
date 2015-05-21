package amtt.epam.com.amtt.bo;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.issue.JIssueFields;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class JCreatingIssueRequest {
    @SerializedName("fields")
    private JIssueFields mFields;

    public JCreatingIssueRequest() {
    }

    public JCreatingIssueRequest(JIssueFields fields) {
        this.mFields = fields;
    }

    public JIssueFields getFields() {
        return mFields;
    }

    public void setFields(JIssueFields fields) {
        this.mFields = fields;
    }
}
