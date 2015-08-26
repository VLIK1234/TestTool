package amtt.epam.com.amtt.helper;

import android.graphics.Bitmap;
import android.support.v4.content.AsyncTaskLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.IOUtils;

/**
 * @author Ivan_Bakach
 * @version on 22.07.2015
 */
public class ScreenshotHelper {

    private static final String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot_%s.png";
    private static final String SCREENSHOT_DATETIME_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    private static final int QUALITY_COMPRESS_SCREENSHOT = 90;

    public static String writeBitmapInFile(final Bitmap bitmap) {
        long imageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat(SCREENSHOT_DATETIME_FORMAT).format(new Date(imageTime));
        String imageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);
        final String path = FileUtil.getCacheAmttDir() + imageFileName;
        // create bitmap screen capture
        AsyncTaskLoader loader = new AsyncTaskLoader(AmttApplication.getContext()) {
            @Override
            public Object loadInBackground() {
                OutputStream fout = null;
                File imageFile = new File(path);
                try {
                    fout = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, QUALITY_COMPRESS_SCREENSHOT, fout);
                    fout.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.close(fout);
                }
                return null;
            }
        };
        loader.loadInBackground();
        return path;

    }
}
