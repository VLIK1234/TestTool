package amtt.epam.com.amtt.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 19.06.2015
 */

public class ReadExcel {

    private final String TAG = this.getClass().getSimpleName();
    private ArrayList<TestCase> testCases;
    private static int mIdCell;
    private int mPriorityCell;
    private int mTestCaseNameCell;
    private int mTestCaseDescriptionCell;
    private int mTestStepsCell;
    private int mExpectedResultsCell;

    public void parseExcel(InputStream inputStream) {

        testCases = new ArrayList<>();

        try {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(3);
            Iterator<Row> rowIterator = hssfSheet.rowIterator();

            while (rowIterator.hasNext()) {
                HSSFRow hssfRow = (HSSFRow) rowIterator.next();
                TestCase testCase = new TestCase();
                Iterator<Cell> cellIterator = hssfRow.cellIterator();

                if (hssfRow.getRowNum() == 1) {
                    while (cellIterator.hasNext()) {
                        HSSFCell hssfCell = (HSSFCell) cellIterator.next();
                        setCellsIndexes(hssfCell);
                    }
                } else {
                    while (cellIterator.hasNext()) {
                        HSSFCell hssfCell = (HSSFCell) cellIterator.next();
                        String cellValue;
                        if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            cellValue = hssfCell.getStringCellValue();
                        } else {
                            cellValue = String.valueOf(hssfCell.getNumericCellValue());
                        }
                        Logger.v(TAG, cellValue);
                        int i = hssfCell.getColumnIndex();
                        if (i == mIdCell) {
                            testCase.setId(cellValue);

                        } else if (i == mPriorityCell) {
                            testCase.setPriority(cellValue);

                        } else if (i == mTestCaseNameCell) {
                            testCase.setTestCaseName(cellValue);

                        } else if (i == mTestCaseDescriptionCell) {
                            testCase.setTestCaseDescription(cellValue);

                        } else if (i == mTestStepsCell) {
                            testCase.setTestSteps(cellValue);

                        } else if (i == mExpectedResultsCell) {
                            testCase.setExpectedResult(cellValue);

                        } else {
                            //new row
                        }
                    }
                    testCases.add(testCase);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCellsIndexes(HSSFCell hssfCell) {
        String s = hssfCell.getStringCellValue();
        if (s.equals("ID")) {
            mIdCell = hssfCell.getColumnIndex();

        } else if (s.equals("Priority")) {
            mPriorityCell = hssfCell.getColumnIndex();

        } else if (s.equals("Test Case Name")) {
            mTestCaseNameCell = hssfCell.getColumnIndex();

        } else if (s.equals("Test Case Description")) {
            mTestCaseDescriptionCell = hssfCell.getColumnIndex();

        } else if (s.equals("Test Steps")) {
            mTestStepsCell = hssfCell.getColumnIndex();

        } else if (s.equals("Expected Result")) {
            mExpectedResultsCell = hssfCell.getColumnIndex();

        } else {
        }
    }
}

