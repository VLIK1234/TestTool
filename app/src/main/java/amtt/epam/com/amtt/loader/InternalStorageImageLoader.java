package amtt.epam.com.amtt.loader;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import amtt.epam.com.amtt.R;

/**
 * Created by Artsiom_Kaliaha on 01.04.2015.
 */
public class InternalStorageImageLoader implements ImageLoadingCallback {

    private final LruCache<String, Bitmap> mCache;
    private final AmttExecutor mExecutor;
    private static final Map<String, ImageView> mLoadedImages;
    private final int mImageViewWidth;
    private final int mImageViewHeight;


    static {
        mLoadedImages = new HashMap<>();
    }

    public InternalStorageImageLoader(int runnableQueueCapacity, int imageViewWidth, int imageViewHeight) {
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8);
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        mExecutor = new AmttExecutor(runnableQueueCapacity);
        mImageViewWidth = imageViewWidth;
        mImageViewHeight = imageViewHeight;
    }

    public void load(ImageView imageView, final String path) {
        imageView.setImageResource(R.drawable.image_loading);

        if (mCache.get(path) != null) {
            synchronized (mCache) {
                imageView.setImageBitmap(mCache.get(path));
            }
        } else {
            if (!isImageLoaded(path)) {
                putImageToLoaded(path, imageView);
                new ImageLoadingTask(path, imageView, mImageViewWidth, mImageViewHeight, this).executeOnExecutor(mExecutor);
            }
        }
    }


    //help methods
    private boolean isImageLoaded(String path) {
        return mLoadedImages.get(path) != null;
    }

    private void putImageToLoaded(String path, ImageView imageView) {
        mLoadedImages.put(path, imageView);
    }


    //ImageLoadingCallback implementation
    @Override
    public void onLoadingFinished(String path, final Bitmap bitmap) {
        ImageView imageview = mLoadedImages.remove(path);
        if (bitmap != null && imageview != null) {
            imageview.setImageBitmap(bitmap);
            synchronized (mCache) {
                mCache.put(path, bitmap);
            }
        }
    }

}
