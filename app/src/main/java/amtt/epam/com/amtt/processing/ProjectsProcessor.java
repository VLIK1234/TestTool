package amtt.epam.com.amtt.processing;


import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import amtt.epam.com.amtt.bo.JProjectsResponse;
import amtt.epam.com.amtt.ticket.JiraContent;


/**
 * Created by Irina Monchenko on 01.04.2015.
 */
public class ProjectsProcessor implements Processor<JProjectsResponse, HttpEntity> {

    @Override
    public JProjectsResponse process(HttpEntity inputStream) throws Exception {
        String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);
        JProjectsResponse result = Gson.getInstance().fromJson(_response, JProjectsResponse.class);
        JiraContent.getInstance().setProjectsNames(result.getProjectsNames());
        return result;
    }
}