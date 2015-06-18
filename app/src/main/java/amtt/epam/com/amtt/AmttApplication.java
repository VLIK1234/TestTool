package amtt.epam.com.amtt;

import amtt.epam.com.amtt.common.CoreApplication;
import amtt.epam.com.amtt.http.HttpClient;
import amtt.epam.com.amtt.processing.PostCreateIssueProcessor;
import amtt.epam.com.amtt.processing.PriorityProcessor;
import amtt.epam.com.amtt.processing.ProjectsProcessor;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.processing.UsersAssignableProcessor;
import amtt.epam.com.amtt.processing.VersionsProcessor;

/**
 * Created by Ivan_Bakach on 19.03.2015.
 */
@SuppressWarnings("unchecked")
public class AmttApplication extends CoreApplication {

    @Override
    public void performRegistration() {
        registerDataSource(HttpClient.NAME, HttpClient.getClient());

        registerProcessor(UserInfoProcessor.NAME, new UserInfoProcessor());
        registerProcessor(VersionsProcessor.NAME, new VersionsProcessor());
        registerProcessor(UsersAssignableProcessor.NAME, new UsersAssignableProcessor());
        registerProcessor(ProjectsProcessor.NAME, new ProjectsProcessor());
        registerProcessor(PriorityProcessor.NAME, new PriorityProcessor());
        registerProcessor(PostCreateIssueProcessor.NAME, new PostCreateIssueProcessor());
    }

}
