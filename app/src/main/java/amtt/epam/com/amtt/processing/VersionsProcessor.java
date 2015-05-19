package amtt.epam.com.amtt.processing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

import amtt.epam.com.amtt.bo.JVersionsResponse;
import amtt.epam.com.amtt.bo.project.JiraIssueVersion;
import amtt.epam.com.amtt.ticket.JiraContent;

/**
 * Created by Irina Monchenko on 05.05.2015.
 */
public class VersionsProcessor implements Processor<JVersionsResponse, HttpEntity> {

    @Override
    public JVersionsResponse process(HttpEntity inputStream) throws Exception {
        String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(_response).getAsJsonArray();
        JVersionsResponse projectExtVersionsResponse = new JVersionsResponse();
        ArrayList<JiraIssueVersion> jiraIssueVersions = new ArrayList<JiraIssueVersion>();
        for (JsonElement obj : jsonArray) {
            JiraIssueVersion jiraIssueVersion = Gson.getInstance().fromJson(obj, JiraIssueVersion.class);
            jiraIssueVersions.add(jiraIssueVersion);
        }
        projectExtVersionsResponse.setVersions(jiraIssueVersions);
        JiraContent.getInstance().setVersionsNames(projectExtVersionsResponse.getVersionsNames());
        return projectExtVersionsResponse;
    }
}
