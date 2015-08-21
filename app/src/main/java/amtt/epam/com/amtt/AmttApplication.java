package amtt.epam.com.amtt;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import amtt.epam.com.amtt.common.CoreApplication;
import amtt.epam.com.amtt.database.DataBaseSource;
import amtt.epam.com.amtt.database.processing.BulkInsertProcessor;
import amtt.epam.com.amtt.database.processing.DeleteProcessor;
import amtt.epam.com.amtt.database.processing.InsertProcessor;
import amtt.epam.com.amtt.database.processing.QueryProcessor;
import amtt.epam.com.amtt.database.processing.UpdateProcessor;
import amtt.epam.com.amtt.googleapi.processing.SpreadsheetProcessor;
import amtt.epam.com.amtt.googleapi.processing.WorksheetProcessor;
import amtt.epam.com.amtt.http.HttpClient;
import amtt.epam.com.amtt.processing.ComponentsProcessor;
import amtt.epam.com.amtt.processing.PostCreateIssueProcessor;
import amtt.epam.com.amtt.processing.PriorityProcessor;
import amtt.epam.com.amtt.processing.ProjectsProcessor;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.processing.UsersAssignableProcessor;
import amtt.epam.com.amtt.processing.VersionsProcessor;
import io.fabric.sdk.android.Fabric;

/**
 @author Ivan_Bakach
 @version on 19.03.2015
 */

public class AmttApplication extends CoreApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void performRegistration() {
        registerPlugin(new HttpClient());
        registerPlugin(new DataBaseSource<>());

        registerPlugin(new InsertProcessor());
        registerPlugin(new BulkInsertProcessor());
        registerPlugin(new QueryProcessor<>());
        registerPlugin(new UpdateProcessor());
        registerPlugin(new DeleteProcessor());

        registerPlugin(new ComponentsProcessor());
        registerPlugin(new UserInfoProcessor());
        registerPlugin(new VersionsProcessor());
        registerPlugin(new UsersAssignableProcessor());
        registerPlugin(new ProjectsProcessor());
        registerPlugin(new PriorityProcessor());
        registerPlugin(new PostCreateIssueProcessor());
        registerPlugin(new SpreadsheetProcessor());
        registerPlugin(new WorksheetProcessor());
    }

}
