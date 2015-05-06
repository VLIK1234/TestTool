package amtt.epam.com.amtt.processing;

import amtt.epam.com.amtt.bo.JProjectExtVersionsResponse;
import amtt.epam.com.amtt.bo.project.JiraIssueVersion;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

/**
 * Created by Irina Monchenko on 05.05.2015.
 */
public class VersionsProcessor implements Processor<JProjectExtVersionsResponse, HttpEntity> {

        @Override
        public JProjectExtVersionsResponse process(HttpEntity inputStream) throws Exception {
            String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);

            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(_response).getAsJsonArray();
            JProjectExtVersionsResponse projectExtVersionsResponse = new JProjectExtVersionsResponse();
            ArrayList<JiraIssueVersion> jiraIssueVersions = new ArrayList<JiraIssueVersion>();

            for(JsonElement obj : jsonArray )
            {
                JiraIssueVersion jiraIssueVersion = Gson.getInstance().fromJson(obj, JiraIssueVersion.class);
                jiraIssueVersions.add(jiraIssueVersion);
            }
            projectExtVersionsResponse.setVersions(jiraIssueVersions);
            return projectExtVersionsResponse;
        }
    }
