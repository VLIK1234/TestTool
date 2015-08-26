package amtt.epam.com.amtt.googleapi.database.table;

import java.util.ArrayList;

import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.table.Table;
import amtt.epam.com.amtt.database.util.MultiValueMap;

/**
 * @author Iryna Monchenko
 * @version on 20.07.2015
 */

public class TagsTable extends Table {

    public static final String TABLE_NAME = "tags";

    public static final String _TESTCASE_ID_LINK = "_testcase_id_link";
    public static final String _NAME = "_name";
    public static final String _SPREADSHEET_ID_LINK = "_spreadsheet_id_link";

    private static final MultiValueMap<String, String> sColumnsMap;

    public static final String[] PROJECTION = {
            _ID,
            _TESTCASE_ID_LINK,
            _NAME,
            _SPREADSHEET_ID_LINK
    };

    static {
        sColumnsMap = new MultiValueMap<>();
        sColumnsMap.put(BaseColumns.TYPE_TEXT, new ArrayList<String>() {{
            add(_TESTCASE_ID_LINK);
            add(_NAME);
            add( _SPREADSHEET_ID_LINK);
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
