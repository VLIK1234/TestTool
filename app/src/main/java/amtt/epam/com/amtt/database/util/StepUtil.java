package amtt.epam.com.amtt.database.util;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.bo.database.ActivityMeta;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.util.ContextHolder;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.view.PaintView;

/**
 @author Artsiom_Kaliaha
 @version on 16.05.2015
 */

public class StepUtil {

    public static void saveStep(ComponentName componentName, String mScreenPath){
        Step step = new Step(componentName, mScreenPath);
        DbObjectManager.INSTANCE.add(step, null);
    }

    public static void cleanStep() {
        DbObjectManager.INSTANCE.removeAll(new Step());
    }

    public static void cleanActivityMeta() {
        DbObjectManager.INSTANCE.removeAll(new ActivityMeta());
    }

    public static void clearAllStep(){
        cleanStep();
        cleanActivityMeta();
    }

    public static void applyNotesToScreenshot(PaintView paintView, ImageView screenshotImageView, String screenshotPath) {
        Bitmap drawnNotesBitmap = paintView.getDrawingCache();
        Bitmap screenshotBitmap = BitmapFactory.decodeFile(screenshotPath).copy(Bitmap.Config.ARGB_8888, true);

        if (screenshotBitmap == null) {
            //In case the screenshot has been deleted
            screenshotBitmap = Bitmap.createBitmap(screenshotImageView.getWidth(), screenshotImageView.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(screenshotBitmap);
        canvas.drawBitmap(drawnNotesBitmap, 0, 0, new Paint(Paint.DITHER_FLAG));

        //TODO use CONSTANTS from dev
        Bitmap.CompressFormat compressFormat = FileUtil.getExtension(screenshotPath).equals("png") ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;
        FileOutputStream outputStream = IOUtils.openFileOutput(screenshotPath);
        if (outputStream != null) {
            screenshotBitmap.compress(compressFormat, 100, outputStream);
        }
    }

    public static void checkUser(String userName, IResult<List<JUserInfo>> result) {
        DbObjectManager.INSTANCE.query(new JUserInfo(), null, new String[]{UsersTable._USER_NAME}, new String[]{userName}, result);
    }

     public static Spanned getStepInfo(Step step){
         Context context = ContextHolder.getContext();
         return Html.fromHtml(
                 "<b>" + context.getString(R.string.label_activity) + "</b>" + "<small>" + step.getActivity() + "</small>" + "<br />" +
                 "<b>" + context.getString(R.string.label_screen_orientation) + "</b>" + "<small>" + step.getOreintation() + "</small>" + "<br />" +
                 "<b>" + context.getString(R.string.label_package_name) + "</b>" + "<small>" + step.getPackageName() + "</small>" + "<br />" + "<br />");
     }

    public static Spanned getStepInfo(List<DatabaseEntity> listStep){
        ArrayList<Step> list = (ArrayList)listStep;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        Context context = ContextHolder.getContext();
        for (int i = 0; i < listStep.size(); i++) {
            builder.append(Html.fromHtml("<h5>" + context.getString(R.string.label_step) + String.valueOf(i + 1) + "</h5>"));
            builder.append(Html.fromHtml("<b>" + context.getString(R.string.label_file_name) + "</b>" + "<small>" + FileUtil.getFileName(list.get(i).getFilePath()) + "</small>" + "<br />"));
            builder.append(getStepInfo(list.get(i)));
        }
        return builder;
    }
}
