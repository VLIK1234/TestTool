package amtt.epam.com.amtt.database.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import amtt.epam.com.amtt.CoreApplication;
import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.bo.ticket.Step;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.datasource.DataSource;
import amtt.epam.com.amtt.http.MimeType;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.Logger;
import amtt.epam.com.amtt.util.ThreadManager;

/**
 * @author Artsiom_Kaliaha
 * @version on 16.05.2015
 */
public class LocalContent {

    private static final String TAG =  LocalContent.class.getSimpleName();

    public static void saveStep(String title, String activityClassName, String packageName, String mScreenPath, String listFragments) {
        String template = FileUtil.getCacheLocalDir() + "/%s";
        String stepDescription;
        File screenshot = new File(mScreenPath);
        stepDescription = String.format(template, screenshot.getName()+".html");

        Step step = new Step(title, activityClassName, packageName, mScreenPath, listFragments);
        FileUtil.writeFile(stepDescription, LocalContent.getStepInfo(step));
//        ContentFromDatabase.setStep(step, null);
    }

    public static void saveOnlyScreenshot(String mScreenPath) {
        Step step = new Step(null, null, null, mScreenPath, null);
        ContentFromDatabase.setStep(step, null);
    }

    public static void getAllSteps(final GetContentCallback<List<Step>> contentCallback) {
        ContentFromDatabase.getAllSteps(new Callback<List<Step>>() {
            @Override
            public void onLoadStart() {
            }

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
        ContentFromDatabase.removeStep(step, null);
    }

    public static void removeAllSteps() {
        ContentFromDatabase.removeAllSteps(null);
    }

    public static void applyNotesToScreenshot(final Bitmap drawingCache, final String screenshotPath) {
//        step.setScreenshotState(Step.ScreenshotState.IS_BEING_WRITTEN);
//        ContentFromDatabase.updateStep(step, null);
        Bitmap.CompressFormat compressFormat = FileUtil.getExtension(screenshotPath).equals(MimeType.IMAGE_PNG.getFileExtension()) ?
                Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;
        ThreadManager.execute(compressFormat, new DataSource<Bitmap.CompressFormat, Void>() {

            @Override
            public Void getData(Bitmap.CompressFormat compressFormat) throws Exception {
                FileOutputStream outputStream = null;
                try {
                    outputStream = IOUtils.openFileOutput(screenshotPath, true);
                    if (outputStream != null) {
                        drawingCache.compress(compressFormat, 100, outputStream);
                    }
                } catch (FileNotFoundException e) {
                    //ignored in this implementation, because exception won't be thrown as we need to create new file
                    //look through IOUtils.openFileOutput method for more information
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }
        }, new Callback<Void>() {
            @Override
            public void onLoadStart() {
            }

            @Override
            public void onLoadExecuted(Void aVoid) {
//                step.setScreenshotState(Step.ScreenshotState.WRITTEN);
//                ContentFromDatabase.updateStep(step, null);
            }

            @Override
            public void onLoadError(Exception e) {
                Logger.e(TAG, e.getMessage(), e);
            }
        });
    }

    public static String getStepInfo(Step step) {
        Context context = CoreApplication.getContext();
        return 
                "<b>" + context.getString(R.string.label_title) + "</b>" + "<small>" + step.getTitle() + "</small>" + "<br />" +
                        "<b>" + context.getString(R.string.label_file_name) + "</b>" + "<small>" + FileUtil.getFileName(step.getScreenshotPath())+ "</small>" + "<br />"+
                        "<b>" + context.getString(R.string.label_activity) + "</b>" + "<small>" + step.getActivity() + "</small>" + "<br />" +
                        "<b>" + context.getString(R.string.lable_list_fragments) + "</b>" + "<small>" + step.getListFragments() + "</small>" + "<br />" +
                        "<b>" + context.getString(R.string.label_screen_orientation) + "</b>" + "<small>" + step.getOrientation() + "</small>" + "<br />" +
                        "<b>" + context.getString(R.string.label_package_name) + "</b>" + "<small>" + step.getPackageName() + "</small>";
    }

    public static String getStepInfo(List<Step> listStep) {
        ArrayList<Step> list = (ArrayList<Step>) listStep;
        StringBuilder builder = new StringBuilder();
        Context context = CoreApplication.getContext();
        if (list.size() > 0) {
            builder.append("<h5>" + "New steps : "
                    + "</h5>");
        }
        for (int i = 0; i < list.size(); i++) {
            Step step = list.get(i);
            builder.append("<h5>").append(context.getString(R.string.label_step)).append(String.valueOf(i + 1)).append("</h5>");
            if (step.isStepWithScreenshot()) {
                builder.append("<b>").append(context.getString(R.string.label_file_name)).append("</b>").append("<small>").append(FileUtil.getFileName(step.getScreenshotPath())).append("</small>");
            }
            if (step.isStepWithActivityInfo()) {
                if (step.isStepWithScreenshot()) {
                    builder.append("<br />");
                }
                builder.append(getStepInfo(step));
            }
            builder.append("<br />" + "<br />");
        }
        return builder.toString();
    }

    public static List<Bitmap> getStepBitmaps(List<Step> stepsList) throws Throwable {
        try {
            List<Bitmap> bitmaps = new ArrayList<>();
            for (Step step : stepsList) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmaps.add(BitmapFactory.decodeFile(step.getScreenshotPath(), options));
            }
            return bitmaps;
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }
    }

    public static void removeAllAttachFile() {
        File folderPath = new File(FileUtil.getCacheLocalDir());
        File[] allAttachFile = folderPath.listFiles();
        if (allAttachFile != null) {
            for (File file : allAttachFile) {
                if (file.exists()) {
                    FileUtil.deleteRecursive(file.getPath());
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

    public static void readTextLogFromFile(String filePath, final GetContentCallback<ArrayList<CharSequence>> contentCallback) {
        ThreadManager.execute(filePath, new DataSource<String, ArrayList<CharSequence>>() {

            @Override
            public ArrayList<CharSequence> getData(String filePath) throws Exception {
                File file = new File(filePath);
                ArrayList<CharSequence> lines = new ArrayList<>();
                String line;
                try {
                    BufferedReader br = new BufferedReader(new java.io.FileReader(file));
                    while ((line = br.readLine()) != null) {
                        lines.add(line);
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return lines;
            }
        }, new Callback<ArrayList<CharSequence>>() {
            @Override
            public void onLoadStart() {

            }

            @Override
            public void onLoadExecuted(ArrayList<CharSequence> charSequences) {
                contentCallback.resultOfDataLoading(charSequences);
            }

            @Override
            public void onLoadError(Exception e) {
                contentCallback.resultOfDataLoading(null);
            }
        });
    }
}
