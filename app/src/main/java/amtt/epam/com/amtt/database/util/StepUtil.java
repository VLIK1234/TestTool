package amtt.epam.com.amtt.database.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.http.MimeType;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.IOUtils;

/**
 * @author Artsiom_Kaliaha
 * @version on 16.05.2015
 */
public class StepUtil {

    public static void saveStep(String title, String activityClassName, String packageName, String mScreenPath, String listFragments) {
        Step step = new Step(title, activityClassName, packageName, mScreenPath, listFragments);
        DbObjectManager.INSTANCE.add(step, null);
    }

    public static void savePureScreenshot(String mScreenPath) {
        Step step = new Step(null, null, null, mScreenPath, null);
        DbObjectManager.INSTANCE.add(step, null);
    }

    public static void clearAllSteps() {
        DbObjectManager.INSTANCE.removeAll(new Step());
    }

    public static void applyNotesToScreenshot(final Bitmap drawingCache, final String screenshotPath, final Step step) {
        step.setScreenshotState(Step.ScreenshotState.IS_BEING_WRITTEN);
        DbObjectManager.INSTANCE.update(step, StepsTable._ID + "=?", new String[]{String.valueOf(step.getId())});
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap.CompressFormat compressFormat = FileUtil.getExtension(screenshotPath).equals(MimeType.IMAGE_PNG.getFileExtension()) ?
                        Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;

                FileOutputStream outputStream = null;
                try {
                    outputStream = IOUtils.openFileOutput(screenshotPath, true);
                } catch (FileNotFoundException e) {
                    //ignored in this implementation, because exception won't be thrown as we need to create new file
                    //look through IOUtils.openFileOutput method for more information
                }
                if (outputStream != null) {
                    drawingCache.compress(compressFormat, 100, outputStream);
                }

                step.setScreenshotState(Step.ScreenshotState.WRITTEN);
                DbObjectManager.INSTANCE.update(step, StepsTable._ID + "=?", new String[]{String.valueOf(step.getId())});
            }
        }).start();
    }

    public static void checkUser(String userName, String url, IResult<List<JUserInfo>> result) {
        DbObjectManager.INSTANCE.query(new JUserInfo(), null, new String[]{UsersTable._USER_NAME, UsersTable._URL}, new String[]{userName, url}, result);
    }

    public static Spanned getStepInfo(Step step) {
        Context context = AmttApplication.getContext();
        return Html.fromHtml(
                "<b>" + context.getString(R.string.label_title) + "</b>" + "<small>" + step.getTitle() + "</small>" + "<br />" +
                        "<b>" + context.getString(R.string.label_activity) + "</b>" + "<small>" + step.getActivity() + "</small>" + "<br />" +
                        "<b>" + context.getString(R.string.lable_list_fragments) + "</b>" + "<small>" + step.getListFragments() + "</small>" + "<br />" +
                        "<b>" + context.getString(R.string.label_screen_orientation) + "</b>" + "<small>" + step.getOrientation() + "</small>" + "<br />" +
                        "<b>" + context.getString(R.string.label_package_name) + "</b>" + "<small>" + step.getPackageName() + "</small>");
    }

    public static Spanned getStepInfo(List<DatabaseEntity> listStep) {
        ArrayList<Step> list = (ArrayList) listStep;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        Context context = AmttApplication.getContext();
        if (list.size() > 0) {
            builder.append(Html.fromHtml("<br/>" + "<br/>" + "<h5>" + "New steps : "
                    + "</h5>"));
        }
        for (int i = 0; i < list.size(); i++) {
            Step step = list.get(i);
            builder.append(Html.fromHtml("<h5>" + context.getString(R.string.label_step) + String.valueOf(i + 1) + "</h5>"));
            if (step.isStepWithScreenshot()) {
                builder.append(Html.fromHtml("<b>" + context.getString(R.string.label_file_name) + "</b>" + "<small>" + FileUtil.getFileName(step.getScreenshotPath()) + "</small>"));
            }
            if (step.isStepWithActivityInfo()) {
                if (step.isStepWithScreenshot()) {
                    builder.append(Html.fromHtml("<br />"));
                }
                builder.append(getStepInfo(step));
            }
            builder.append(Html.fromHtml("<br />" + "<br />"));
        }
        return builder;
    }

    public static List<Bitmap> getStepBitmaps(List<Step> stepsList) throws Throwable {
        List<Bitmap> bitmaps = new ArrayList<>();
        for (Step step : stepsList) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            bitmaps.add(BitmapFactory.decodeFile(step.getScreenshotPath()));
        }
        return bitmaps;
    }

    public static void removeAllAttachFile() {
        File folderPath = new File(FileUtil.getCacheAmttDir());
        File[] allAttachFile = folderPath.listFiles();
        if (allAttachFile != null) {
            for (File file : allAttachFile) {
                if (file.exists()) {
                    FileUtil.delete(file.getPath());
                }
            }
        }
    }

}
