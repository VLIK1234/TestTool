package amtt.epam.com.amtt.bo.issue.willrefactored;

import amtt.epam.com.amtt.bo.issue.willrefactored.issuekey.JiraBaseFields;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class JiraBase extends Entity {
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
