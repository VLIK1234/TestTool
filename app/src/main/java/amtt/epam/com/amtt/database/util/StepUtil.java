package amtt.epam.com.amtt.database.util;

import android.content.ComponentName;
import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

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

    public static void saveActivityMeta(ActivityMeta activityMeta) {
        DbObjectManager.INSTANCE.add(activityMeta, null);
    }

    public static void cleanStep() {
        DbObjectManager.INSTANCE.removeAll(new Step());
    }

    public static void cleanActivityMeta() {
        DbObjectManager.INSTANCE.removeAll(new ActivityMeta());
    }

    public static void clearAllStep() {
        cleanStep();
        cleanActivityMeta();
    }

    public static void checkUser(String userName, IResult<List<JUserInfo>> result) {
        DbObjectManager.INSTANCE.query(new JUserInfo(), null, new String[]{UsersTable._USER_NAME}, new String[]{userName}, result);
    }

    public static Spanned getStepInfo(Step step) {
        Context context = ContextHolder.getContext();
        return Html.fromHtml(
                "<b>" + context.getString(R.string.label_activity) + "</b>" + "<small>" + step.getActivity() + "</small>" + "<br />" +
                        "<b>" + context.getString(R.string.label_screen_orientation) + "</b>" + "<small>" + step.getOrientation() + "</small>" + "<br />" +
                        "<b>" + context.getString(R.string.label_package_name) + "</b>" + "<small>" + step.getPackageName() + "</small>" + "<br />" + "<br />");
    }

    public static Spanned getStepInfo(List<DatabaseEntity> listStep) {
        ArrayList<Step> list = (ArrayList) listStep;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        Context context = ContextHolder.getContext();
        for (int i = 0; i < list.size(); i++) {
            Step step = list.get(i);
            if (!step.isStepWithoutActivityInfo()) {
                builder.append(Html.fromHtml("<h5>" + context.getString(R.string.label_step) + String.valueOf(i + 1) + "</h5>"));
                if (!step.isStepWithActivityInfo()) {
                    builder.append(Html.fromHtml("<b>" + context.getString(R.string.label_file_name) + "</b>" + "<small>" + FileUtil.getFileName(step.getFilePath()) + "</small>" + "<br />"));
                }
                builder.append(getStepInfo(step));
            }
        }
        return builder;
    }
}
