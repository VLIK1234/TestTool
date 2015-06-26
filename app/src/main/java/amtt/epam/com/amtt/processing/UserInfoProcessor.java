package amtt.epam.com.amtt.processing;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import amtt.epam.com.amtt.bo.user.JUserInfo;


/**
 * Created by Irina Monchenko on 01.04.2015.
 */
public class UserInfoProcessor implements Processor<JUserInfo, HttpEntity> {

    public static final String NAME = UserInfoProcessor.class.getName();
    @Override
    public JUserInfo process(HttpEntity httpEntity) {
        String _response = null;
        try {
            _response = EntityUtils.toString(httpEntity, HTTP.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Gson.getInstance().fromJson(_response, JUserInfo.class);
    }


    @Override
    public String getName() {
        return NAME;
    }

}