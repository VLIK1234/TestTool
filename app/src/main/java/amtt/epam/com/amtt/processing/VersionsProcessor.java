package amtt.epam.com.amtt.processing;

import amtt.epam.com.amtt.bo.JProjectExtVersionsResponse;
import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * Created by Irina Monchenko on 05.05.2015.
 */
public class VersionsProcessor implements Processor<JProjectExtVersionsResponse, HttpEntity> {

        @Override
        public JProjectExtVersionsResponse process(HttpEntity inputStream) throws Exception {
            String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);
            return Gson.getInstance().fromJson(_response, JProjectExtVersionsResponse.class);
        }
    }
