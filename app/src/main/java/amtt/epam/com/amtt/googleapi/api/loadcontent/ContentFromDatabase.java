package amtt.epam.com.amtt.googleapi.api.loadcontent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.database.DataBaseApi;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.util.DbSelectionUtil;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.googleapi.bo.GSpreadsheet;
import amtt.epam.com.amtt.googleapi.bo.GTag;
import amtt.epam.com.amtt.googleapi.bo.GWorksheet;
import amtt.epam.com.amtt.googleapi.database.table.SpreadsheetTable;
import amtt.epam.com.amtt.googleapi.database.table.TagsTable;
import amtt.epam.com.amtt.googleapi.database.table.TestcaseTable;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 6/10/2015
 */

public class ContentFromDatabase {

    private static final String TAG = ContentFromDatabase.class.getSimpleName();
    private static DataBaseApi mDataBaseApi = DataBaseApi.getInstance();

    public static void setSpreadsheet(GSpreadsheet spreadsheet, Callback<Integer> result) {
        mDataBaseApi.insert(spreadsheet, result);
    }

    public static void getSpreadsheet(String spreadsheetIdLink, Callback<List<GSpreadsheet>> result) {
        String selection = DbSelectionUtil.equal(SpreadsheetTable._SPREADSHEET_ID_LINK);
        mDataBaseApi.query(new GSpreadsheet(), null, selection, new String[]{spreadsheetIdLink}, null, result);
    }

    public static void getAllSpreadsheets(Callback<List<DatabaseEntity>> result) {
        mDataBaseApi.query(new GSpreadsheet(), null, null, null, null, result);
    }

    public static void setWorksheet(GWorksheet worksheet, Callback<Integer> result) {
        mDataBaseApi.insert(worksheet, result);
    }

    public static void setTestCase(GEntryWorksheet testCase, Callback<Integer> result) {
        mDataBaseApi.insert(testCase, result);
    }

    public static void getTestCasesByLinkSpreadsheet(String idLinkSpreadsheet, Callback<List<GEntryWorksheet>> result) {
        String selection = DbSelectionUtil.equal(TestcaseTable._SPREADSHEET_ID_LINK);
        mDataBaseApi.query(new GEntryWorksheet(), null, selection, new String[]{idLinkSpreadsheet}, null, result);
    }

    public static void getTestCasesByLinks(String idLink, String idLinkSpreadsheet, Callback<List<GEntryWorksheet>> result) {
        String selectionSpreadsheetLink = DbSelectionUtil.equal(TestcaseTable._SPREADSHEET_ID_LINK);
        String selectionTestcaseLink = DbSelectionUtil.equal(TestcaseTable._TESTCASE_ID_LINK);
        String selection = DbSelectionUtil.and(selectionSpreadsheetLink, selectionTestcaseLink);
        mDataBaseApi.query(new GEntryWorksheet(), null, selection, new String[]{idLinkSpreadsheet, idLink}, null, result);
    }

    public static void setTags(List<GTag> tags, Callback<Integer> result) {
        mDataBaseApi.bulkInsert(tags, result);
    }

    public static void getTagsByIdLinkSpreadsheet(String idLinkSpreadsheet, Callback<List<GTag>> result) {
        String selection = DbSelectionUtil.equal(TagsTable._SPREADSHEET_ID_LINK);
        mDataBaseApi.query(new GTag(), null, selection, new String[]{idLinkSpreadsheet}, null, result);
    }

    public static void getTestcaseByIdLink(String idLink, Callback<List<GEntryWorksheet>> result) {
        mDataBaseApi.query(new GEntryWorksheet(), null, TestcaseTable._TESTCASE_ID_LINK, new String[]{idLink}, null, result);
    }

    public static void getTestcasesByIdLinksTestcases(String spreadsheetLink, ArrayList<String> testcasesIdLinks,
                                                      Callback<List<GEntryWorksheet>> result) {
        String selection = TestcaseTable._SPREADSHEET_ID_LINK + "=? AND " + TestcaseTable._TESTCASE_ID_LINK + " IN (";
        String[] selectionArgs = new String[testcasesIdLinks.size() + 1];
        selectionArgs[0] = spreadsheetLink;
        for (int i = 0; i < testcasesIdLinks.size(); i++) {
            selection = selection.concat("?");
            selectionArgs[i + 1] = testcasesIdLinks.get(i);
            if ((i + 1) < testcasesIdLinks.size()) {
                selection = selection.concat(", ");
            }
        }
        selection = selection.concat(")");
        Logger.e(TAG, selection);
        Logger.e(TAG, Arrays.toString(selectionArgs));
        mDataBaseApi.query(new GEntryWorksheet(), null, selection, selectionArgs, null, result);
    }

    public static void getTagsByIdLinksTestcases(String spreadsheetLink, ArrayList<String> testcasesIdLinks, Callback<List<GTag>> result) {
        String selection = TagsTable._SPREADSHEET_ID_LINK + "=? AND " + TagsTable._TESTCASE_ID_LINK + " IN (";
        String[] selectionArgs = new String[testcasesIdLinks.size() + 1];
        selectionArgs[0] = spreadsheetLink;
        for (int i = 0; i < testcasesIdLinks.size(); i++) {
            selection = selection.concat("?");
            selectionArgs[i + 1] = testcasesIdLinks.get(i);
            if ((i + 1) < testcasesIdLinks.size()) {
                selection = selection.concat(", ");
            }
        }
        selection = selection.concat(")");
        Logger.e(TAG, selection);
        Logger.e(TAG, Arrays.toString(selectionArgs));
        mDataBaseApi.query(new GTag(), null, selection, selectionArgs, null, result);
    }

}
