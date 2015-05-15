package amtt.epam.com.amtt;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import amtt.epam.com.amtt.util.ContextHolder;
/**
 * Created by Ivan_Bakach on 19.03.2015.
 */
public class CoreApplication extends Application {

    private static ImageLoader sImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.setContext(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        sImageLoader = ImageLoader.getInstance();
        sImageLoader.init(config);
    }

    public static ImageLoader getImageLoader() {
        return sImageLoader;
    }

}
