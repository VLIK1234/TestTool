package amtt.epam.com.amtt.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * Created by Artsiom_Kaliaha on 01.04.2015.
 */
public class InternalStorageImageLoader {

    private final LruCache<String, Bitmap> mCache;

    public InternalStorageImageLoader() {
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8);
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public void load(final ImageView imageView, final String path) {
        imageView.setImageBitmap(null);

        if (mCache.get(path) != null) {
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(mCache.get(path));
                }
            });
        } else {
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(path, options);
                    options.inSampleSize = calculateInSampleSize(options, imageView);
                    options.inJustDecodeBounds = false;

                    final Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                    mCache.put(path, bitmap);

                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }
            });
        }

    }

    private int calculateInSampleSize(BitmapFactory.Options options, ImageView imageView) {
        int trimmedWidth = options.outWidth;
        int trimmedHeight = options.outHeight;
        int newInSampleSize = 1;

        while (trimmedWidth > imageView.getWidth() && trimmedHeight > imageView.getHeight()) {
            trimmedWidth /= 2;
            trimmedHeight /= 2;
            newInSampleSize *= 2;
        }

        return newInSampleSize;
    }

}
