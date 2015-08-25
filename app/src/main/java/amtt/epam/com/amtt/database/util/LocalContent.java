package amtt.epam.com.amtt.database.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.bo.ticket.Step;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.http.MimeType;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Artsiom_Kaliaha
 * @version on 16.05.2015
 */
public class LocalContent {

    private static final String TAG =  LocalContent.class.getSimpleName();

    public static void saveStep(String title, String activityClassName, String packageName, String mScreenPath, String listFragments) {
        Step step = new Step(title, activityClassName, packageName, mScreenPath, listFragments);
        ContentFromDatabase.setStep(step, new Callback<Integer>() {
            @Override
            public void onLoadStart() {}

            @Override
            public void onLoadExecuted(Integer integer) {}

            @Override
            public void onLoadError(Exception e) {
                Logger.e(TAG, e.getMessage(), e);
            }
        });
    }

    public static void saveOnlyScreenshot(String mScreenPath) {
        Step step = new Step(null, null, null, mScreenPath, null);
        ContentFromDatabase.setStep(step, new Callback<Integer>() {
            @Override
            public void onLoadStart() {}

            @Override
            public void onLoadExecuted(Integer integer) {}

            @Override
            public void onLoadError(Exception e) {
                Logger.e(TAG, e.getMessage(), e);
            }
        });
    }

    public static void getAllSteps(final GetContentCallback<List<Step>> contentCallback) {
        ContentFromDatabase.getAllSteps(new Callback<List<Step>>() {
            @Override
            public void onLoadStart() {}

            @Override
            public void onLoadExecuted(List<Step> steps) {
                if (steps != null && !steps.isEmpty()) {
                    contentCallback.resultOfDataLoading(steps);
                } else {
                    contentCallback.resultOfDataLoading(null);
                }
            }

            @Override
            public void onLoadError(Exception e) {
                Logger.e(TAG, e.getMessage(), e);
                contentCallback.resultOfDataLoading(null);
            }
        });
    }

    public static void removeStep(Step step){
        ContentFromDatabase.removeStep(step, new Callback<Integer>() {
            @Override
            public void onLoadStart() {}

            @Override
            public void onLoadExecuted(Integer integer) {}

            @Override
            public void onLoadError(Exception e) {
                Logger.e(TAG, e.getMessage(), e);
            }
        });
    }

    public static void removeAllSteps() {
        ContentFromDatabase.removeAllSteps(new Callback<Integer>() {
            @Override
            public void onLoadStart() {}

            @Override
            public void onLoadExecuted(Integer integer) {}

            @Override
            public void onLoadError(Exception e) {
                Logger.e(TAG, e.getMessage(), e);
            }
        });
    }

    public static void applyNotesToScreenshot(final Bitmap drawingCache, final String screenshotPath, final Step step) {
        step.setScreenshotState(Step.ScreenshotState.IS_BEING_WRITTEN);
        ContentFromDatabase.updateStep(step, new Callback<Integer>() {
            @Override
            public void onLoadStart() {}

            @Override
            public void onLoadExecuted(Integer integer) {}

            @Override
            public void onLoadError(Exception e) {
                Logger.e(TAG, e.getMessage(), e);
            }
        });
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
                ContentFromDatabase.updateStep(step, new Callback<Integer>() {
                    @Override
                    public void onLoadStart() {}

                    @Override
                    public void onLoadExecuted(Integer integer) {}

                    @Override
                    public void onLoadError(Exception e) {
                        Logger.e(TAG, e.getMessage(), e);
                    }
                });
            }
        }).start();
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

    public static Spanned getStepInfo(List<Step> listStep) {
        ArrayList<Step> list = (ArrayList<Step>) listStep;
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
            bitmaps.add(BitmapFactory.decodeFile(step.getScreenshotPath(), options));
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

    public static void checkUser(String userName, String url, Callback<List<JUserInfo>> result) {
        ContentFromDatabase.getUserByNameAndUrl(userName, url, result);
    }

    public static void updateUser(int userId, JUserInfo user, Callback<Integer> result) {
        ContentFromDatabase.updateUser(userId, user, result);
    }
}
