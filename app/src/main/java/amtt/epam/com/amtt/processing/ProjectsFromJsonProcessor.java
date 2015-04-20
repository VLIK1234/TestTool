package amtt.epam.com.amtt.processing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;


/**
 * Created by Irina Monchenko on 01.04.2015.
 */
public class ProjectsFromJsonProcessor implements Processor<JMetaResponse, HttpEntity> {

    @Override
    public JMetaResponse process(HttpEntity inputStream) throws Exception {
        String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);
        //TODO can we use gson as singleton?
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(_response, JMetaResponse.class);
    }
}

