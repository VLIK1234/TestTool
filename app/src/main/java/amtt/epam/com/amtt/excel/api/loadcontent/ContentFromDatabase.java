package amtt.epam.com.amtt.excel.api.loadcontent;

import java.util.List;

import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.excel.bo.GoogleEntryWorksheet;
import amtt.epam.com.amtt.excel.bo.GoogleSpreadsheet;
import amtt.epam.com.amtt.excel.bo.GoogleWorksheet;
import amtt.epam.com.amtt.excel.database.table.SpreadsheetTable;
import amtt.epam.com.amtt.excel.database.table.TestcaseTable;
import amtt.epam.com.amtt.excel.database.table.WorksheetTable;

/**
 * @author Iryna Monchanka
 * @version on 6/10/2015
 */

public class ContentFromDatabase {

    public static void setSpreadsheet(GoogleSpreadsheet spreadsheet, IResult<Integer> result) {
        DbObjectManager.INSTANCE.add(spreadsheet, result);
    }

    public static void setSpreadsheets(List<GoogleSpreadsheet> spreadsheets, IResult<Integer> result) {
        List list = spreadsheets;
        DbObjectManager.INSTANCE.add(list, result);
    }

    public static void getSpreadsheet(String spreadsheetTitle, IResult<List<GoogleSpreadsheet>> result) {
        DbObjectManager.INSTANCE.query(new GoogleSpreadsheet(), null, new String[]{SpreadsheetTable._SPREADSHEET_ID_LINK}, new String[]{spreadsheetTitle}, result);
    }

    public static void setWorksheet(GoogleWorksheet worksheet, IResult<Integer> result) {
        DbObjectManager.INSTANCE.add(worksheet, result);
    }

    public static void setWorksheets(List<GoogleWorksheet> worksheets, IResult<Integer> result) {
        List list = worksheets;
        DbObjectManager.INSTANCE.add(list, result);
    }

    public static void getWorksheets(String spreadsheetIdLink, IResult<List<GoogleWorksheet>> result) {
        DbObjectManager.INSTANCE.query(new GoogleWorksheet(), null, new String[]{WorksheetTable._SPREADSHEET_ID_LINK}, new String[]{spreadsheetIdLink}, result);
    }

    public static void setTestCase(GoogleEntryWorksheet testCase, IResult<Integer> result) {
        DbObjectManager.INSTANCE.add(testCase, result);
    }

    public static void setTestCases(List<GoogleEntryWorksheet> testCases, IResult<Integer> result) {
        List list = testCases;
        DbObjectManager.INSTANCE.add(list, result);
    }

    public static void getTestCases(String worksheetIdLink, IResult<List<GoogleEntryWorksheet>> result) {
        DbObjectManager.INSTANCE.query(new GoogleEntryWorksheet(), null, new String[]{TestcaseTable._WORKSHEET_ID_LINK}, new String[]{worksheetIdLink}, result);
    }
}
