package amtt.epam.com.amtt.googleapi.api.loadcontent;

import java.util.List;

import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.googleapi.bo.GSpreadsheet;
import amtt.epam.com.amtt.googleapi.bo.GTag;
import amtt.epam.com.amtt.googleapi.bo.GWorksheet;
import amtt.epam.com.amtt.googleapi.database.table.TestcaseTable;

/**
 * @author Iryna Monchanka
 * @version on 6/10/2015
 */

public class ContentFromDatabase {

    public static void setSpreadsheet(GSpreadsheet spreadsheet, IResult<Integer> result) {
        DbObjectManager.INSTANCE.add(spreadsheet, result);
    }

    public static void setWorksheet(GWorksheet worksheet, IResult<Integer> result) {
        DbObjectManager.INSTANCE.add(worksheet, result);
    }

    public static void setTestCase(GEntryWorksheet testCase, IResult<Integer> result) {
        DbObjectManager.INSTANCE.add(testCase, result);
    }

    public static void setTags(List<GTag> tags, IResult<Integer> result) {
        List list = tags;
        DbObjectManager.INSTANCE.add(list, result);
    }

    public static void getTestcaseByIdLink(String idLink, IResult<List<GEntryWorksheet>> result) {
        DbObjectManager.INSTANCE.query(new GEntryWorksheet(), null, new String[]{TestcaseTable._TESTCASE_ID_LINK}, new String[]{idLink}, result);
    }

}
