package amtt.epam.com.amtt.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.R;

/**
 * Created by Artsiom_Kaliaha on 01.04.2015.
 */
public class InternalStorageImageLoader implements ImageLoadingCallback {

    private final LruCache<String, Bitmap> mCache;
    private final AmttExecutor mExecutor;
    private static final Map<String,ImageView> mLoadedImages;
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
                mExecutor.execute(new ImageLoadingTask(path,mImageViewWidth, mImageViewHeight, this));
            }
        }
    }


    //help methods
    private boolean isImageLoaded(String path) {
        return mLoadedImages.get(path) != null;
    }

    private void putImageToLoaded(String path, ImageView imageView) {
        mLoadedImages.put(path,imageView);
    }


    //ImageLoadingCallback implementation
    @Override
    public void onLoadingFinished(String path, final Bitmap bitmap) {
        if (mLoadedImages.get(path) != null) {
            final ImageView targetImageView = mLoadedImages.remove(path);
            targetImageView.post(new Runnable() {
                @Override
                public void run() {
                    targetImageView.setImageBitmap(bitmap);
                }
            });
            synchronized (mCache) {
                mCache.put(path,bitmap);
            }
        }
    }

}
