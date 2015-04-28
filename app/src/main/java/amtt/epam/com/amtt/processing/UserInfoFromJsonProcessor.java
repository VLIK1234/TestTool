package amtt.epam.com.amtt.processing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import amtt.epam.com.amtt.bo.user.JiraUser;


/**
 * Created by Irina Monchenko on 01.04.2015.
 */
public class UserInfoFromJsonProcessor implements Processor<JiraUser, HttpEntity> {

    @Override
    public JiraUser process(HttpEntity inputStream) throws Exception {
        String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(_response, JiraUser.class);
    }
}