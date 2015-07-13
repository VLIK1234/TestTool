package amtt.epam.com.amtt.database.util;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

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

    public static void saveStep(ComponentName componentName, String mScreenPath) {
        Step step = new Step(componentName, mScreenPath);
        DbObjectManager.INSTANCE.add(step, null);
    }

    public static void savePureScreenshot(String mScreenPath) {
        Step step = new Step(null, mScreenPath);
        DbObjectManager.INSTANCE.add(step, null);
    }

    public static void cleanSteps() {
        DbObjectManager.INSTANCE.removeAll(new Step());
    }

    public static void clearAllSteps() {
        cleanSteps();
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

    public static void checkUser(String userName, IResult<List<JUserInfo>> result) {
        DbObjectManager.INSTANCE.query(new JUserInfo(), null, new String[]{UsersTable._USER_NAME}, new String[]{userName}, result);
    }

    public static Spanned getStepInfo(Step step) {
        Context context = AmttApplication.getContext();
        return Html.fromHtml(
                "<b>" + context.getString(R.string.label_activity) + "</b>" + "<small>" + step.getActivity() + "</small>" + "<br />" +
                        "<b>" + context.getString(R.string.label_screen_orientation) + "</b>" + "<small>" + step.getOrientation() + "</small>" + "<br />" +
                        "<b>" + context.getString(R.string.label_package_name) + "</b>" + "<small>" + step.getPackageName() + "</small>");
    }

    public static Spanned getStepInfo(List<DatabaseEntity> listStep) {
        ArrayList<Step> list = (ArrayList) listStep;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        Context context = AmttApplication.getContext();
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

}
