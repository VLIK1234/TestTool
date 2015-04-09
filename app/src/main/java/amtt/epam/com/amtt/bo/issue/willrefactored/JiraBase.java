package amtt.epam.com.amtt.bo.issue.willrefactored;

import com.google.gson.annotations.SerializedName;

import amtt.epam.com.amtt.bo.issue.willrefactored.issuekey.JiraBaseFields;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class JiraBase {
    @SerializedName("fields")
    private JiraBaseFields mFields;

    public JiraBase() {
    }

    public JiraBase(JiraBaseFields fields) {
        this.mFields = fields;
    }

    public JiraBaseFields getFields() {
        return mFields;
    }

    public void setFields(JiraBaseFields fields) {
        this.mFields = fields;
    }
}
