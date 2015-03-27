package amtt.epam.com.amtt.bo;

import amtt.epam.com.amtt.processing.IssueGsonProcessor;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class CreateIssue {

    public String createSimpleIssue(){
        JiraProject project = new JiraProject();
           // project.setIdProject("id");
            project.setKeyProject("AMTT");

        String summary = "summary";

        String description = "description";

        JiraIssueType issuetype = new JiraIssueType();
            //issuetype.setId("id");
            issuetype.setName("Bug");

            JiraBaseFields fields = new JiraBaseFields();
            fields.setProject(project);
            fields.setSummary(summary);
            fields.setDescription(description);
            fields.setIssuetype(issuetype);

        JiraBase bData = new JiraBase();
        bData.setFields(fields);
        IssueGsonProcessor<JiraBase> issueGsonProcessor = new IssueGsonProcessor<>();
        String jsonString = null;

        try {
            jsonString = issueGsonProcessor.process(bData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("test", jsonString);
        return jsonString;
    }
}
