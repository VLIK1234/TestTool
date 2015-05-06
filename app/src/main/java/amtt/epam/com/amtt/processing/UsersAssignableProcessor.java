package amtt.epam.com.amtt.processing;

import amtt.epam.com.amtt.bo.JUserAssignableResponse;
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
 * Created by shiza on 06.05.2015.
 */
public class UsersAssignableProcessor implements Processor<JUserAssignableResponse, HttpEntity> {

    @Override
    public JUserAssignableResponse process(HttpEntity inputStream) throws Exception {
        String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);
        JsonParser parser = new JsonParser();
        JsonArray jArray = parser.parse(_response).getAsJsonArray();
        JUserAssignableResponse jUserAssignableResponse = new JUserAssignableResponse();
        ArrayList<JiraUser> lcs = new ArrayList<JiraUser>();

        for(JsonElement obj : jArray )
        {
            JiraUser cse = Gson.getInstance().fromJson(obj, JiraUser.class);
            lcs.add(cse);
        }
        jUserAssignableResponse.setAssignableUsers(lcs);
        return Gson.getInstance().fromJson(_response, JUserAssignableResponse.class);
    }
}