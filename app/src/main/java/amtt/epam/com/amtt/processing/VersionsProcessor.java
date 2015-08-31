package amtt.epam.com.amtt.processing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

import amtt.epam.com.amtt.bo.JVersionsResponse;
import amtt.epam.com.amtt.bo.project.JIssueVersion;
import amtt.epam.com.amtt.api.loadcontent.JiraContent;

/**
 @author Iryna Monchanka
 @version on 05.05.2015
 */

public class VersionsProcessor implements Processor<HttpEntity, JVersionsResponse> {

    public static final String NAME = VersionsProcessor.class.getName();
    @Override
    public JVersionsResponse process(HttpEntity inputStream) throws Exception {
        String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);
        inputStream.consumeContent();
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(_response).getAsJsonArray();
        JVersionsResponse projectExtVersionsResponse = new JVersionsResponse();
        ArrayList<JIssueVersion> jIssueVersions = new ArrayList<>();
        for (JsonElement obj : jsonArray) {
            JIssueVersion jIssueVersion = Gson.getInstance().fromJson(obj, JIssueVersion.class);
            jIssueVersions.add(jIssueVersion);
        }
        projectExtVersionsResponse.setVersions(jIssueVersions);
        JiraContent.getInstance().setVersionsNames(projectExtVersionsResponse.getVersionsNames());
        return projectExtVersionsResponse;
    }

    @Override
    public String getPluginName() {
        return NAME;
    }

}
