package amtt.epam.com.amtt.database.table;

import java.util.ArrayList;

import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.util.MultiValueMap;

/**
 * @author Iryna Monchanka
 * @version on 7/05/2015
 */

public class ProjectTable extends Table {

    public static final String TABLE_NAME = "project";

    public static final String _JIRA_ID = "_jira_id";
    public static final String _KEY = "_key";
    public static final String _NAME = "_name";
    public static final String _ID_USER = "_id_user";

    private static MultiValueMap<String, String> sColumnsMap;

    public static final String[] PROJECTION = {
            _ID,
            _JIRA_ID,
            _KEY,
            _NAME,
            _ID_USER
    };

    static {
        sColumnsMap = new MultiValueMap<>();
        sColumnsMap.put(BaseColumns.TYPE_TEXT, new ArrayList<String>() {{
            add(_JIRA_ID);
            add(_KEY);
            add(_NAME);
            add(_ID_USER);
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

