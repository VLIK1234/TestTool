package amtt.epam.com.amtt.bo;

import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by Iryna_Monchanka on 3/26/2015.
 */
public class CreateIssue {

    public String createSimpleIssue(){
        DataProject project = new DataProject();
            project.setIdProject("id");
            project.setKeyProject("key");

        String summary = "summary";

        String description = "description";

        IssueType issuetype = new IssueType();
            issuetype.setId("id");
            issuetype.setName("name");

        BaseData.BaseDataFields fields = BaseData.fields;
            fields.setProject(project);
            fields.setSummary(summary);
            fields.setDescription(description);
            fields.setIssuetype(issuetype);

        BaseData bData = new BaseData(fields);

        Gson gson = new Gson();
        String jsonString = gson.toJson(bData);
        Log.d("test", jsonString);
        return jsonString;
    }
}
