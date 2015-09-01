package amtt.epam.com.amtt.database.util;

import java.util.List;

import amtt.epam.com.amtt.bo.ticket.Step;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.database.DataBaseApi;
import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.util.Constants;

/**
 * @author Iryna Monchanka
 * @version on 8/17/2015
 */

public class ContentFromDatabase {

    private static final DataBaseApi mDataBaseApi = DataBaseApi.getInstance();

    public static void setStep(Step step, Callback<Integer> result) {
        mDataBaseApi.insert(step, result);
    }

    public static void getStepById(int idStep, Callback<List<Step>> result) {
        String selection = DbSelectionUtil.equal(StepsTable._ID);
        mDataBaseApi.query(new Step(), StepsTable.PROJECTION, selection,
                new String[]{String.valueOf(idStep)}, null, result);
    }

    public static void getAllSteps(Callback<List<Step>> result) {
        mDataBaseApi.query(new Step(), null, null, null, null, result);
    }

    public static void updateStep(Step step, Callback<Integer> result) {
        String selection = DbSelectionUtil.equal(StepsTable._ID);
        mDataBaseApi.update(step, selection, new String[]{String.valueOf(step.getId())}, result);
    }

    public static void removeStep(Step step, Callback<Integer> result) {
        mDataBaseApi.delete(step, result);
    }

    public static void removeAllSteps(Callback<Integer> result) {
        mDataBaseApi.delete(new Step(), result);
    }

    public static void setUser(JUserInfo user, Callback<Integer> result) {
        mDataBaseApi.insert(user, result);
    }

    public static void updateUser(int userId, JUserInfo user, Callback<Integer> result) {
        mDataBaseApi.update(user, BaseColumns._ID + Constants.Symbols.EQUAL + userId, null, result);
    }

    public static void getUserByNameAndUrl(String userName, String url, Callback<List<JUserInfo>> result) {
        String selectionUserName = DbSelectionUtil.equal(UsersTable._USER_NAME);
        String selectionUrl = DbSelectionUtil.equal(UsersTable._URL);
        String selection = DbSelectionUtil.and(selectionUserName, selectionUrl);
        mDataBaseApi.query(new JUserInfo(), null,  selection, new String[]{userName, url}, null, result);
    }
}
