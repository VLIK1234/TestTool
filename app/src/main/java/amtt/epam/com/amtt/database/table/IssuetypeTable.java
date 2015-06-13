package amtt.epam.com.amtt.database.table;

import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.util.MultiValueMap;

import java.util.ArrayList;

/**
 * @author Iryna Monchanka
 * @version on 07.05.2015
 */
public class IssuetypeTable extends Table {

    public static final String TABLE_NAME = "issuetype";

    public static final String _JIRA_ID = "_jira_id";
    public static final String _NAME = "_name";
    public static final String _KEY_PROJECT = "_id_project";

    private static MultiValueMap<String, String> sColumnsMap;

    public static final String[] PROJECTION = {
            _ID,
            _JIRA_ID,
            _NAME,
            _KEY_PROJECT
    };

    static {
        sColumnsMap = new MultiValueMap<>();
        sColumnsMap.put(BaseColumns.TYPE_TEXT, new ArrayList<String>() {{
            add(_JIRA_ID);
            add(_NAME);
            add(_KEY_PROJECT);
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


