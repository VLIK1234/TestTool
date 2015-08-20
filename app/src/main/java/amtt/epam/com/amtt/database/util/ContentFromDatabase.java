package amtt.epam.com.amtt.database.util;

import java.util.List;

import amtt.epam.com.amtt.bo.ticket.Step;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.util.Constants;

/**
 * @author Iryna Monchanka
 * @version on 8/17/2015
 */

public class ContentFromDatabase {

    private static final String ID_STEP = StepsTable._ID;
    private static final String STEP_SELECTION = ID_STEP + "=?";
    private static DbObjectManager mManager = DbObjectManager.INSTANCE;

    public static void setStep(Step step, Callback<Integer> result) {
        mManager.add(step, result);
    }

    public static void getStepById(int idStep, Callback<List<Step>> result){
        mManager.query(new Step(), StepsTable.PROJECTION, new String[]{ID_STEP},
                new String[]{String.valueOf(idStep)}, result);
    }

    public static void getAllSteps(Callback<List<Step>> result){
        mManager.getAll(new Step(), result);
    }

    public static void updateStep(Step step, Callback<Integer> result){
        mManager.update(step, STEP_SELECTION, new String[]{String.valueOf(step.getId())}, result);
    }

    public static void removeStep(Step step, Callback<Integer> result) {
        mManager.remove(step, result);
    }

    public static void removeAllSteps(Callback<Integer> result) {
        mManager.removeAll(new Step(), result);
    }

    public static void setUser(JUserInfo user, Callback<Integer> result){
        mManager.add(user, result);
    }

    public static void updateUser(int userId, JUserInfo user, Callback<Integer> result) {
        mManager.update(user, BaseColumns._ID + Constants.Symbols.EQUAL + userId, null, result);
    }

    public static void getUserByNameAndUrl(String userName, String url, Callback<List<JUserInfo>> result) {
        mManager.query(new JUserInfo(), null, new String[]{UsersTable._USER_NAME, UsersTable._URL},
                                        new String[]{userName, url}, result);
    }
}
