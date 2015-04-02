package amtt.epam.com.amtt.loaders;

import amtt.epam.com.amtt.authorization.JiraApi;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.processing.ProjectsToJsonProcessor;
import android.content.Context;
import android.util.Log;
import org.apache.http.HttpEntity;

import java.io.IOException;

/**
 * Created by shiza on 02.04.2015.
 */
public class ProjectsLoader extends BaseRestLoader<JMetaResponse> {
    private static String mUserName, mPassword, mUrl;

    public ProjectsLoader(Context ctx, String userName, String password, String url) {
        super(ctx);

        this.mUserName = userName;
        this.mPassword = password;
        this.mUrl = url;

    }

    @Override
    protected JMetaResponse retrieveResponse() throws IOException {
        HttpEntity i;
        JMetaResponse jMetaResponse;
        try {
            i = new JiraApi().searchIssue(mUserName, mPassword, mUrl);
            ProjectsToJsonProcessor projects = new ProjectsToJsonProcessor();
            jMetaResponse = projects.process(i);
            Log.d("STATUS", jMetaResponse.getExpand());
            //throw new AuthenticationException("issue can`t be create");

        } catch (Exception e) {
            Log.d("STATUS", e.getMessage());
            return jMetaResponse = null;
        }
        return jMetaResponse;
    }
}