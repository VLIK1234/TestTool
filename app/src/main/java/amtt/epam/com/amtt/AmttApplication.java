package amtt.epam.com.amtt;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
public class AmttApplication extends CoreApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void performRegistration() {
        registerPlugin(new HttpClient());

        registerPlugin(new UserInfoProcessor());
        registerPlugin(new VersionsProcessor());
        registerPlugin(new UsersAssignableProcessor());
        registerPlugin(new ProjectsProcessor());
        registerPlugin(new PriorityProcessor());
        registerPlugin(new PostCreateIssueProcessor());
    }

}
