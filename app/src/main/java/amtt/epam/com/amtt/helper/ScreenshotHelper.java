package amtt.epam.com.amtt.helper;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import amtt.epam.com.amtt.util.IOUtils;

/**
 * @author Ivan_Bakach
 * @version on 22.07.2015
 */
public class ScreenshotHelper {
    public static final String AMTT_CACHE_DIRECTORY = "Amtt_cache";
    private static final String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot_%s.png";
    public static final String SCREENSHOT_DATETIME_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    public static final int QUALITY_COMPRESS_SCREENSHOT = 90;
    public static String sPath;

    public static void saveBitmapIntoFile(Bitmap bitmap){
        long imageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat(SCREENSHOT_DATETIME_FORMAT).format(new Date(imageTime));
        String imageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);
        sPath = Environment.getExternalStorageDirectory().toString() + File.separatorChar + AMTT_CACHE_DIRECTORY + File.separatorChar + imageFileName;
        OutputStream fout = null;
        File imageFile = new File(sPath);

        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, QUALITY_COMPRESS_SCREENSHOT, fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(fout);
        }
    }
}
