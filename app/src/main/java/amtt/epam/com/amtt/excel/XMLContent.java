package amtt.epam.com.amtt.excel;

import amtt.epam.com.amtt.excel.bo.GoogleSpreadsheet;
import amtt.epam.com.amtt.excel.bo.GoogleWorksheet;

/**
 * @author Iryna Monchanka
 * @version on 7/6/2015
 */
public class XMLContent {

    private GoogleSpreadsheet mSpreadsheet;
    private GoogleWorksheet mWorksheet;

    private static class XMLContentHolder {
        public static final XMLContent INSTANCE = new XMLContent();
    }

    public static XMLContent getInstance() {
        return XMLContentHolder.INSTANCE;
    }

    public GoogleSpreadsheet getSpreadsheet() {
        return mSpreadsheet;
    }

    public void setSpreadsheet(GoogleSpreadsheet spreadsheet) {
        this.mSpreadsheet = spreadsheet;
    }

    public GoogleWorksheet getWorksheet() {
        return mWorksheet;
    }

    public void setWorksheet(GoogleWorksheet worksheet) {
        this.mWorksheet = worksheet;
    }
}
