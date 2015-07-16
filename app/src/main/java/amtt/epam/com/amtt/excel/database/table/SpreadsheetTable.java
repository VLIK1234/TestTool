package amtt.epam.com.amtt.excel.database.table;

import java.util.ArrayList;

import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.table.Table;
import amtt.epam.com.amtt.database.util.MultiValueMap;

/**
 * @author Iryna Monchanka
 * @version on 09.07.2015
 */

public class SpreadsheetTable extends Table {

    public static final String TABLE_NAME = "spreadsheet";

    public static final String _SPREADSHEET_ID_LINK = "_spreadsheet_id_link";
    public static final String _UPDATED = "_updated";
    public static final String _TITLE = "_title";
    public static final String _TOTAL_RESULTS = "_total_results";
    public static final String _START_INDEX = "_start_index";

    private static final MultiValueMap<String, String> sColumnsMap;

    public static final String[] PROJECTION = {
            _ID,
            _SPREADSHEET_ID_LINK,
            _UPDATED,
            _TITLE,
            _TOTAL_RESULTS,
            _START_INDEX
    };

    static {
        sColumnsMap = new MultiValueMap<>();
        sColumnsMap.put(BaseColumns.TYPE_TEXT, new ArrayList<String>() {{
            add(_SPREADSHEET_ID_LINK);
            add(_UPDATED);
            add(_TITLE);
            add(_TOTAL_RESULTS);
            add(_START_INDEX);
        }});
        sColumnsMap.put(BaseColumns.TYPE_INTEGER + BaseColumns.PRIMARY_KEY, new ArrayList<String>() {{
            add(_ID);
        }});
    }

    @Override
    public MultiValueMap<String, String> getColumnsMap() {
        return sColumnsMap;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
