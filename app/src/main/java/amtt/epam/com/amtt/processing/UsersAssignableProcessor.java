package amtt.epam.com.amtt.processing;

import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.JUserAssignableResponse;
import amtt.epam.com.amtt.bo.project.JPriority;
import amtt.epam.com.amtt.bo.user.JiraUser;
import amtt.epam.com.amtt.bo.user.JiraUserInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 06.05.2015.
 */
public class UsersAssignableProcessor implements Processor<JUserAssignableResponse, HttpEntity> {

    @Override
    public JUserAssignableResponse process(HttpEntity inputStream) throws Exception {
        String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(_response).getAsJsonArray();
        JUserAssignableResponse userAssignableResponse = new JUserAssignableResponse();
        ArrayList<JiraUser> jiraUsers = new ArrayList<JiraUser>();
        for(JsonElement obj : jsonArray )
        {
            JiraUser jiraUser = Gson.getInstance().fromJson(obj, JiraUser.class);
            jiraUsers.add(jiraUser);
        }
        userAssignableResponse.setAssignableUsers(jiraUsers);
        return userAssignableResponse;
    }
}