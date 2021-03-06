package amtt.epam.com.amtt.processing;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import amtt.epam.com.amtt.bo.user.JUserInfo;

/**
 @author Iryna Monchanka
 @version on 01.04.2015
 */

public class UserInfoProcessor implements Processor<HttpEntity, JUserInfo> {

    @Override
    public JUserInfo process(HttpEntity httpEntity) {
        String _response = null;
        try {
            _response = EntityUtils.toString(httpEntity, HTTP.UTF_8);
            httpEntity.consumeContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Gson.getInstance().fromJson(_response, JUserInfo.class);
    }
}