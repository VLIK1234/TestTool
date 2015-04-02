package amtt.epam.com.amtt.processing;

import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


/**
 * Created by shiza on 01.04.2015.
 */
public class ProjectsToJsonProcessor implements Processor<JMetaResponse, HttpEntity> {
    @Override
    public JMetaResponse process(HttpEntity inputStream) throws Exception {
        String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);
        Gson gson = new GsonBuilder().create();
        Log.i("GSON", _response);
        JMetaResponse response = (JMetaResponse) gson.fromJson(_response, JMetaResponse.class);

        Log.i("JSON", _response);
        return response;
    }


}

