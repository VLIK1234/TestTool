package amtt.epam.com.amtt.processing;

import amtt.epam.com.amtt.bo.JCreateIssueResponse;
import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * @author Iryna Monchanka
 * @version on 27.05.2015
 */

public class PostCreateIssueProcessor implements Processor<JCreateIssueResponse, HttpEntity> {

    public static final String NAME = PostCreateIssueProcessor.class.getName();
    @Override
    public JCreateIssueResponse process(HttpEntity inputStream) throws Exception {
        String _response = EntityUtils.toString(inputStream, HTTP.UTF_8);
        return Gson.getInstance().fromJson(_response, JCreateIssueResponse.class);
    }

    @Override
    public String getName() {
        return NAME;
    }

}
