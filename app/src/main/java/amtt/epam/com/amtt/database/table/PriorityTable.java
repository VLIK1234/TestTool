package amtt.epam.com.amtt.database.table;

import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.util.MultiValueMap;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 8/05/2015.
 */
public class PriorityTable extends Table implements BaseColumns {

    public static final String TABLE_NAME = "priority";

    public static final String _JIRA_ID = "_jira_id";
    public static final String _NAME = "_name";

    private static MultiValueMap<String, String> sColumnsMap;

    public static final String[] PROJECTION = {
        _ID,
        _JIRA_ID,
        _NAME,
        UsersTable._USER_NAME
    };

    static {
        sColumnsMap = new MultiValueMap<>();
        sColumnsMap.put(TYPE_TEXT, new ArrayList<String>() {{
            add(_JIRA_ID);
            add(_NAME);
            add(UsersTable._USER_NAME);
        }});
        sColumnsMap.put(TYPE_INTEGER + BaseColumns.PRIMARY_KEY, new ArrayList<String>() {{
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
