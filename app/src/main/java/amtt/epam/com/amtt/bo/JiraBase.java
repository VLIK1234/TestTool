package amtt.epam.com.amtt.bo;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class JiraBase extends Entity {

    private JiraBaseFields fields;

    public JiraBase() {
    }

    public JiraBase(JiraBaseFields fields) {
        this.fields = fields;
    }

    public JiraBaseFields getFields() {
        return fields;
    }

    public void setFields(JiraBaseFields fields) {
        this.fields = fields;
    }
}
