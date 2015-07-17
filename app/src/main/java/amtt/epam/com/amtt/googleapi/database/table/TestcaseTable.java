package amtt.epam.com.amtt.googleapi.database.table;

import java.util.ArrayList;

import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.table.Table;
import amtt.epam.com.amtt.database.util.MultiValueMap;

/**
 * @author Iryna Monchanka
 * @version on 09.07.2015
 */

public class TestcaseTable extends Table {

    public static final String TABLE_NAME = "testcase";

    public static final String _TESTCASE_ID_LINK = "_testcase_id_link";
    public static final String _WORKSHEET_ID_LINK = "_worksheet_id_link";
    public static final String _UPDATED = "_updated";
    public static final String _TITLE = "_title";
    public static final String _TESTCASE_ID = "_testcase_id";
    public static final String _PRIORITY = "_priority";
    public static final String _NAME = "_name";
    public static final String _DESCRIPTION = "_description";
    public static final String _STEPS = "_steps";
    public static final String _LABEL = "_label";
    public static final String _EXPECTED_RESULTS = "_expected_results";

    private static final MultiValueMap<String, String> sColumnsMap;

    public static final String[] PROJECTION = {
            _ID,
            _TESTCASE_ID_LINK,
            _WORKSHEET_ID_LINK,
            _UPDATED,
            _TITLE,
            _TESTCASE_ID,
            _PRIORITY,
            _NAME,
            _DESCRIPTION,
            _STEPS,
            _LABEL,
            _EXPECTED_RESULTS
    };

    static {
        sColumnsMap = new MultiValueMap<>();
        sColumnsMap.put(BaseColumns.TYPE_TEXT, new ArrayList<String>() {{
            add(_TESTCASE_ID_LINK);
            add(_WORKSHEET_ID_LINK);
            add(_UPDATED);
            add(_TITLE);
            add(_TESTCASE_ID);
            add(_PRIORITY);
            add(_NAME);
            add(_DESCRIPTION);
            add(_STEPS);
            add(_LABEL);
            add(_EXPECTED_RESULTS);
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
