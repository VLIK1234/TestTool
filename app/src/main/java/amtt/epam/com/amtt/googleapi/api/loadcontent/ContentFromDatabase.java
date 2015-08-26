package amtt.epam.com.amtt.googleapi.api.loadcontent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
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
    private static DbObjectManager mManager = DbObjectManager.INSTANCE;

    public static void setSpreadsheet(GSpreadsheet spreadsheet, IResult<Integer> result) {
        mManager.add(spreadsheet, result);
    }

    public static void getSpreadsheet(String spreadsheetIdLink, IResult<List<GSpreadsheet>> result) {
        mManager.query(new GSpreadsheet(), null, new String[]{SpreadsheetTable._SPREADSHEET_ID_LINK}, new String[]{spreadsheetIdLink}, result);
    }

    public static void getAllSpreadsheets(IResult<List<DatabaseEntity>> result) {
        mManager.getAll(new GSpreadsheet(), result);
    }

    public static void setWorksheet(GWorksheet worksheet, IResult<Integer> result) {
        mManager.add(worksheet, result);
    }

    public static void setTestCase(GEntryWorksheet testCase, IResult<Integer> result) {
        mManager.add(testCase, result);
    }

    public static void getTestCasesByLinkSpreadsheet(String idLinkSpreadsheet, IResult<List<GEntryWorksheet>> result) {
        mManager.query(new GEntryWorksheet(), null, new String[]{TestcaseTable._SPREADSHEET_ID_LINK}, new String[]{idLinkSpreadsheet}, result);
    }

    public static void getTestCasesByLinks(String idLink, String idLinkSpreadsheet, IResult<List<GEntryWorksheet>> result) {
        mManager.query(new GEntryWorksheet(), null, new String[]{TestcaseTable._SPREADSHEET_ID_LINK, TestcaseTable._TESTCASE_ID_LINK}, new String[]{idLinkSpreadsheet, idLink}, result);
    }

    public static void setTags(List<GTag> tags, IResult<Integer> result) {
        mManager.add(tags, result);
    }

    public static void getTagsByIdLinkSpreadsheet(String idLinkSpreadsheet, IResult<List<GTag>> result) {
        mManager.query(new GTag(), null, new String[]{TagsTable._SPREADSHEET_ID_LINK}, new String[]{idLinkSpreadsheet}, result);
    }

    public static void getTestcaseByIdLink(String idLink, IResult<List<GEntryWorksheet>> result) {
        mManager.query(new GEntryWorksheet(), null, new String[]{TestcaseTable._TESTCASE_ID_LINK}, new String[]{idLink}, result);
    }

    public static void getTestcasesByIdLinksTestcases(String spreadsheetLink, ArrayList<String> testcasesIdLinks, IResult<List<GEntryWorksheet>> result) {
        String selection = TestcaseTable._SPREADSHEET_ID_LINK + "=? AND " + TestcaseTable._TESTCASE_ID_LINK + " IN (";
        String[] selectionArgs = new String[testcasesIdLinks.size() + 1];
        selectionArgs[0] = spreadsheetLink;
        for (int i = 0; i < testcasesIdLinks.size(); i++) {
            selection = selection.concat("?");
            selectionArgs[i + 1] = testcasesIdLinks.get(i);
            if ((i+1) < testcasesIdLinks.size()) {
                selection = selection.concat(", ");
            }
        }
        selection = selection.concat(")");
        Logger.e(TAG, selection);
        Logger.e(TAG, Arrays.toString(selectionArgs));
        mManager.queryDefault(new GEntryWorksheet(), null, selection, selectionArgs, result);
    }

    public static void getTagsByIdLinksTestcases(String spreadsheetLink, ArrayList<String> testcasesIdLinks, IResult<List<GTag>> result) {
        String selection = TagsTable._SPREADSHEET_ID_LINK + "=? AND " + TagsTable._TESTCASE_ID_LINK + " IN (";
        String[] selectionArgs = new String[testcasesIdLinks.size() + 1];
        selectionArgs[0] = spreadsheetLink;
        for (int i = 0; i < testcasesIdLinks.size(); i++) {
            selection = selection.concat("?");
            selectionArgs[i + 1] = testcasesIdLinks.get(i);
            if ((i+1) < testcasesIdLinks.size()) {
                selection = selection.concat(", ");
            }
        }
        selection = selection.concat(")");
        Logger.e(TAG, selection);
        Logger.e(TAG, Arrays.toString(selectionArgs));
        mManager.queryDefault(new GTag(), null, selection, selectionArgs, result);
    }

}
