package amtt.epam.com.amtt.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 19.06.2015
 */

public class ReadExcel {

    private static final String TAG = ReadExcel.class.getSimpleName();
    private static ArrayList<TestCase> testCases;
    private static int mIdCell;
    private static int mPriorityCell;
    private static int mTestCaseNameCell;
    private static int mTestCaseDescriptionCell;
    private static int mTestStepsCell;
    private static int mExpectedResultsCell;

    public static void parseExcel(InputStream inputStream) {

        testCases = new ArrayList<>();

        try {
            Workbook[] wbs = new Workbook[] { new HSSFWorkbook(), new XSSFWorkbook() };
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(3);
            Iterator<Row> rowIterator = hssfSheet.rowIterator();
            while (rowIterator.hasNext()) {
                HSSFRow hssfRow = (HSSFRow) rowIterator.next();
                Iterator<Cell> cellIterator = hssfRow.cellIterator();
                if (hssfRow.getRowNum() == 1) {
                    while (cellIterator.hasNext()) {
                        HSSFCell hssfCell = (HSSFCell) cellIterator.next();
                        setCellsIndexes(hssfCell);
                    }
                } else {
                    while (cellIterator.hasNext()) {
                        if (getTestCaseValue(cellIterator) != null) {
                            testCases.add(getTestCaseValue(cellIterator));
                        }
                    }
                }
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static TestCase getTestCaseValue(Iterator<Cell> cellIterator) {
        TestCase testCase = new TestCase();
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
            Logger.d(TAG, "id = " + cellValue);
            testCase.setId(cellValue);
        } else if (i == mPriorityCell) {
            Logger.d(TAG, "priotity = " + cellValue);
            testCase.setPriority(cellValue);
        } else if (i == mTestCaseNameCell) {
            if (cellValue != null) {
                Logger.d(TAG, "TCName = " + cellValue);
                testCase.setTestCaseName(cellValue);
            } else {
                return null;
            }
        } else if (i == mTestCaseDescriptionCell) {
            Logger.d(TAG, "TCDescription = " +  cellValue);
            testCase.setTestCaseDescription(cellValue);

        } else if (i == mTestStepsCell) {
            Logger.d(TAG, "TSteps = " +  cellValue);
            testCase.setTestSteps(cellValue);

        } else if (i == mExpectedResultsCell) {
            Logger.d(TAG, "ExpectedResults = " + cellValue);
            testCase.setExpectedResult(cellValue);

        }
        return testCase;
    }

    private static void setCellsIndexes(HSSFCell hssfCell) {
        String s = hssfCell.getStringCellValue();
        switch (s) {
            case "ID":
                mIdCell = hssfCell.getColumnIndex();

                break;
            case "Priority":
                mPriorityCell = hssfCell.getColumnIndex();

                break;
            case "Test Case Name":
                mTestCaseNameCell = hssfCell.getColumnIndex();

                break;
            case "Test Case Description":
                mTestCaseDescriptionCell = hssfCell.getColumnIndex();

                break;
            case "Test Steps":
                mTestStepsCell = hssfCell.getColumnIndex();

                break;
            case "Expected Result":
                mExpectedResultsCell = hssfCell.getColumnIndex();

                break;
            default:
                break;
        }
    }
}

