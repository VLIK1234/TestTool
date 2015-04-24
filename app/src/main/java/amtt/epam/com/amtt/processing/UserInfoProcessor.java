package amtt.epam.com.amtt.processing;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;


/**
 * Created by Irina Monchenko on 01.04.2015.
 */
public class UserInfoProcessor implements Processor<JiraUserInfo, HttpEntity> {

    @Override
    public JiraUserInfo process(HttpEntity inputStream) throws Exception {
        String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);
        return Gson.getInstance().fromJson(_response, JiraUserInfo.class);
    }
}