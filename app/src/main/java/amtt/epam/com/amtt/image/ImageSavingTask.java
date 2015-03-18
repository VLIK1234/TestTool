package amtt.epam.com.amtt.image;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.view.View;

import java.io.FileOutputStream;

import amtt.epam.com.amtt.MainActivity;

/**
 * Created by Artsiom_Kaliaha on 18.03.2015.
 */
public class ImageSavingTask extends AsyncTask<Activity, Void, ImageSavingResult> {

    private MainActivity mActivity;

    @Override
    protected ImageSavingResult doInBackground(Activity... params) {
        mActivity = (MainActivity) params[0];

        View rootView = mActivity.getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = rootView.getDrawingCache();
        Rect rect = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        bitmap = Bitmap.createBitmap(bitmap, 0, rect.top, rect.width(), rect.height());

        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(mActivity.getCacheDir().getPath() + "/screen" + mActivity.getScreenNumber() + ".png"));
            mActivity.incrementScreenNumber();
        } catch (Exception e) {
            return ImageSavingResult.ERROR;
        }
        return ImageSavingResult.SAVED;
    }

    @Override
    protected void onPostExecute(ImageSavingResult imageSavingResult) {
        mActivity.onImageSaved(imageSavingResult);
    }
}
