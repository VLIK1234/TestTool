package amtt.epam.com.amtt.processing;

import amtt.epam.com.amtt.bo.JUserAssignableResponse;
import amtt.epam.com.amtt.bo.user.JUser;
import amtt.epam.com.amtt.ticket.JiraContent;

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

    public static final String NAME = "UsersAssignableProcessor";

    @Override
    public JUserAssignableResponse process(HttpEntity inputStream) throws Exception {
        String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(_response).getAsJsonArray();
        JUserAssignableResponse userAssignableResponse = new JUserAssignableResponse();
        ArrayList<JUser> jUsers = new ArrayList<JUser>();
        for(JsonElement obj : jsonArray )
        {
            JUser jUser = Gson.getInstance().fromJson(obj, JUser.class);
            jUsers.add(jUser);
        }
        userAssignableResponse.setAssignableUsers(jUsers);
        JiraContent.getInstance().setUsersAssignableNames(userAssignableResponse.getAssignableUsersNames());
        return userAssignableResponse;
    }
}