package amtt.epam.com.amtt.googleapi.api.loadcontent;

import java.util.List;

import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.googleapi.bo.GSpreadsheet;
import amtt.epam.com.amtt.googleapi.bo.GTag;
import amtt.epam.com.amtt.googleapi.bo.GWorksheet;
import amtt.epam.com.amtt.googleapi.database.table.SpreadsheetTable;
import amtt.epam.com.amtt.googleapi.database.table.TestcaseTable;
import amtt.epam.com.amtt.googleapi.database.table.WorksheetTable;

/**
 * @author Iryna Monchanka
 * @version on 6/10/2015
 */

public class ContentFromDatabase {

    public static void setSpreadsheet(GSpreadsheet spreadsheet, IResult<Integer> result) {
        DbObjectManager.INSTANCE.add(spreadsheet, result);
    }

    public static void setSpreadsheets(List<GSpreadsheet> spreadsheets, IResult<Integer> result) {
        List list = spreadsheets;
        DbObjectManager.INSTANCE.add(list, result);
    }

    public static void getSpreadsheet(String spreadsheetTitle, IResult<List<GSpreadsheet>> result) {
        DbObjectManager.INSTANCE.query(new GSpreadsheet(), null, new String[]{SpreadsheetTable._SPREADSHEET_ID_LINK}, new String[]{spreadsheetTitle}, result);
    }

    public static void setWorksheet(GWorksheet worksheet, IResult<Integer> result) {
        DbObjectManager.INSTANCE.add(worksheet, result);
    }

    public static void setWorksheets(List<GWorksheet> worksheets, IResult<Integer> result) {
        List list = worksheets;
        DbObjectManager.INSTANCE.add(list, result);
    }

    public static void getWorksheets(String spreadsheetIdLink, IResult<List<GWorksheet>> result) {
        DbObjectManager.INSTANCE.query(new GWorksheet(), null, new String[]{WorksheetTable._SPREADSHEET_ID_LINK}, new String[]{spreadsheetIdLink}, result);
    }

    public static void getWorksheet(String worksheetIdLink, IResult<List<GWorksheet>> result) {
        DbObjectManager.INSTANCE.query(new GWorksheet(), null, new String[]{WorksheetTable._WORKSHEET_ID_LINK}, new String[]{worksheetIdLink}, result);
    }

    public static void setTestCase(GEntryWorksheet testCase, IResult<Integer> result) {
        DbObjectManager.INSTANCE.add(testCase, result);
    }

    public static void setTestCases(List<GEntryWorksheet> testCases, IResult<Integer> result) {
        List list = testCases;
        DbObjectManager.INSTANCE.add(list, result);
    }

    public static void getTestCases(String worksheetIdLink, IResult<List<GEntryWorksheet>> result) {
        DbObjectManager.INSTANCE.query(new GEntryWorksheet(), null, new String[]{TestcaseTable._WORKSHEET_ID_LINK}, new String[]{worksheetIdLink}, result);
    }

    public static void updateWorksheet(GWorksheet worksheet, IResult<Integer> result) {
        DbObjectManager.INSTANCE.update(worksheet, BaseColumns._ID + "=" + worksheet.getId(), null, result);
    }

    public static void setTags(List<GTag> tags, IResult<Integer> result) {
        List list = tags;
        DbObjectManager.INSTANCE.add(list, result);
    }


}
