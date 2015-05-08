package amtt.epam.com.amtt.database.table;

import java.util.ArrayList;

import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.util.MultiValueMap;

/**
 * Created by Iryna_Monchanka on 07.05.2015.
 */
public class ProjectTable extends Table implements BaseColumns {

    public static final String TABLE_NAME = "project";

    public static final String _AVATAR_MEDIUM_URL = "_avatar_medium_url";
    public static final String _AVATAR_SMALL_URL = "_avatar_small_url";
    public static final String _AVATAR_URL = "_avatar_url";
    public static final String _AVATAR_X_SMALL_URL = "_avatar_x_small_url";
    public static final String _JIRA_ID = "_jira_id";
    public static final String _KEY = "_key";
    public static final String _NAME = "_name";

    private static MultiValueMap<String, String> sColumnsMap;

    public static final String[] PROJECTION = {
            _ID,
            _AVATAR_MEDIUM_URL,
            _AVATAR_SMALL_URL,
            _AVATAR_URL,
            _AVATAR_X_SMALL_URL,
            UsersTable._EMAIL,
            _JIRA_ID,
            _KEY,
            _NAME
    };

    static {
        sColumnsMap = new MultiValueMap<>();
        sColumnsMap.put(TYPE_TEXT, new ArrayList<String>() {{
            add(_AVATAR_MEDIUM_URL);
            add(_AVATAR_SMALL_URL);
            add(_AVATAR_URL);
            add(_AVATAR_X_SMALL_URL);
            add(UsersTable._EMAIL);
            add(_JIRA_ID);
            add(_KEY);
            add(_NAME);
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

