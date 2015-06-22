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
    private ArrayList<Smoke> smokes;
    private int mIdCell;
    private int mTestCaseNameCell;
    private int mTestCaseDescriptionCell;
    private int mTestStepsCell;
    private int mExpectedResultsCell;

    public void parseExcel(InputStream inputStream) {

        smokes = new ArrayList<>();

        try {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(3);
            Iterator<Row> rowIterator = hssfSheet.rowIterator();

            while (rowIterator.hasNext()) {
                HSSFRow hssfRow = (HSSFRow) rowIterator.next();

                if (hssfRow.getRowNum() < 2) {
                    continue;
                }

                Smoke smoke = new Smoke();
                Iterator<Cell> cellIterator = hssfRow.cellIterator();

                while (cellIterator.hasNext()) {
                    HSSFCell hssfCell = (HSSFCell) cellIterator.next();
                    String cellValue;

                    if (hssfCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                        cellValue = hssfCell.getStringCellValue();
                    } else {
                        cellValue = String.valueOf(hssfCell.getNumericCellValue());
                    }

                    Logger.v(TAG, cellValue);

                    switch (hssfCell.getColumnIndex()) {
                        case 0:
                            smoke.setId(Double.valueOf(cellValue));
                            break;
                        case 1:
                            //smoke.setSummary(cellValue);
                            break;
                        case 2:
                            smoke.setTestSteps(cellValue);
                            break;
                        case 3:
                            smoke.setExpectedResult(cellValue);
                            break;
                        case 4:
                            //smoke.setAndroid(cellValue);
                            break;
                        case 5:
                            //smoke.setNotes(cellValue);
                            break;
                        case 6:
                            //smoke.setIOS(cellValue);
                            break;
                        case 7:
                            //smoke.setTestNotes(cellValue);
                            break;
                        default:
                            break;
                    }
                }
                smokes.add(smoke);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

