package amtt.epam.com.amtt.loader;

import android.graphics.Bitmap;

/**
 * Created by Artsiom_Kaliaha on 03.04.2015.
 */
public interface ImageLoadingCallback {

    void onLoadingFinished(String path, Bitmap bitmap);

}
