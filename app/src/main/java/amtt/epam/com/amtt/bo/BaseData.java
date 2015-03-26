package amtt.epam.com.amtt.bo;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class BaseData extends Entity {

    public static BaseDataFields fields;

    public BaseData(){
    }

    public BaseData(BaseDataFields fields) {
        this.fields = fields;
    }

    public class BaseDataFields{

        private DataProject project;
        private String summary;
        private String description;
        private IssueType issuetype;

        private BaseDataFields(){}
       // timetracking
       //private String parent = "parent";
        // customfield_11050

        public DataProject getProject() {
            return project;
        }

        public BaseDataFields(DataProject project, String summary, String description, IssueType issuetype) {
            this.project = project;
            this.summary = summary;
            this.description = description;
            this.issuetype = issuetype;
        }

        public void setProject(DataProject project) {
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

        public IssueType getIssuetype() {
            return issuetype;
        }

        public void setIssuetype(IssueType issuetype) {
            this.issuetype = issuetype;
        }
    }

}
