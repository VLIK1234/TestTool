package amtt.epam.com.amtt.processing;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import amtt.epam.com.amtt.bo.user.JUserInfo;


/**
 * Created by Irina Monchenko on 01.04.2015.
 */
public class UserInfoProcessor implements Processor<JUserInfo, HttpEntity> {

    @Override
    public JUserInfo process(HttpEntity httpEntity) throws Exception {
        String _response = EntityUtils.toString(httpEntity, HTTP.UTF_8);
        return Gson.getInstance().fromJson(_response, JUserInfo.class);
    }
}