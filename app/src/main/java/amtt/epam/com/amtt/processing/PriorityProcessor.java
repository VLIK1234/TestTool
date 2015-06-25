package amtt.epam.com.amtt.processing;

import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.project.JPriority;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

/**
 @author Iryna Monchanka
 @version on 06.05.2015
 */

public class PriorityProcessor  implements Processor<JPriorityResponse, HttpEntity> {

    public static final String NAME = PriorityProcessor.class.getName();
    @Override
    public JPriorityResponse process(HttpEntity inputStream) throws Exception {
        String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(_response).getAsJsonArray();
        JPriorityResponse priorityResponse = new JPriorityResponse();
        ArrayList<JPriority> priorities = new ArrayList<>();
        for(JsonElement obj : jsonArray )
        {
            JPriority jiraIssueVersion = Gson.getInstance().fromJson(obj, JPriority.class);
            priorities.add(jiraIssueVersion);
        }
        priorityResponse.setPriorities(priorities);
        return priorityResponse;
    }


    @Override
    public String getName() {
        return NAME;
    }

}

