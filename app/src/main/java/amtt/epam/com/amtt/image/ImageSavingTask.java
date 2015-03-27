package amtt.epam.com.amtt.image;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;

import java.io.FileOutputStream;

/**
 * Created by Artsiom_Kaliaha on 18.03.2015.
 */
public class ImageSavingTask extends AsyncTask<Void, Void, ImageSavingResult> {

    private final ImageSavingCallback mCallback;
    private Bitmap mBitmap;
    private final Rect mRect;
    private final String mPath;

    public ImageSavingTask(ImageSavingCallback callback, Bitmap bitmap, Rect rect, String path) {
        mCallback = callback;
        mBitmap = bitmap;
        mRect = rect;
        mPath = path;
    }

    @Override
    protected ImageSavingResult doInBackground(Void... params) {
        mBitmap = Bitmap.createBitmap(mBitmap, 0, mRect.top, mRect.width(), mRect.height());

        try {
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(mPath + "/screen" + mCallback.getScreenNumber() + ".png"));
        } catch (Exception e) {
            Log.e("TAG",e.getMessage());
            return ImageSavingResult.ERROR;
        }
        return ImageSavingResult.SAVED;
    }

    @Override
    protected void onPostExecute(ImageSavingResult imageSavingResult) {
        mCallback.onImageSaved(imageSavingResult);
    }
}
