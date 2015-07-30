package amtt.epam.com.amtt.database.table;

import java.util.ArrayList;

import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.util.MultiValueMap;

/**
 @author Artsiom_Kaliaha
 @version on 29.04.2015
 */

public class UsersTable extends Table {

    public static final String TABLE_NAME = "users";

    public static final String _CREDENTIALS = "_credentials";
    public static final String _USER_NAME = "_user_name";
    public static final String _DISPLAY_NAME = "_display_name";
    public static final String _TIME_ZONE = "_time_zone";
    public static final String _LOCALE = "_locale";
    public static final String _URL = "_url";
    public static final String _KEY = "_key";
    public static final String _EMAIL = "_email";
    public static final String _AVATAR_16 = "_avatar_16";
    public static final String _AVATAR_24 = "_avatar_24";
    public static final String _AVATAR_32= "_avatar_32";
    public static final String _AVATAR_48 = "_avatar_48";
    public static final String _LAST_PROJECT_KEY = "_last_project_key";
    public static final String _LAST_ASSIGNEE_NAME = "_last_assignee_name";
    public static final String _LAST_COMPONENTS_IDS = "_last_components_ids";
    public static final String _LAST_SPREADSHEET_URL = "_last_spreadsheet_url";

    private static MultiValueMap<String, String> sColumnsMap;

    public static final String[] PROJECTION = {
            _ID,
            _USER_NAME,
            _DISPLAY_NAME,
            _TIME_ZONE,
            _LOCALE,
            _URL,
            _KEY,
            _EMAIL,
            _AVATAR_16,
            _AVATAR_24,
            _AVATAR_32,
            _AVATAR_48,
            _CREDENTIALS,
            _LAST_PROJECT_KEY,
            _LAST_ASSIGNEE_NAME,
            _LAST_COMPONENTS_IDS,
            _LAST_SPREADSHEET_URL
    };

    static {
        sColumnsMap = new MultiValueMap<>();
        sColumnsMap.put(BaseColumns.TYPE_TEXT, new ArrayList<String>() {{
            add(_USER_NAME);
            add(_DISPLAY_NAME);
            add(_TIME_ZONE);
            add(_LOCALE);
            add(_KEY);
            add(_URL);
            add(_EMAIL);
            add(_AVATAR_16);
            add(_AVATAR_24);
            add(_AVATAR_32);
            add(_AVATAR_48);
            add(_CREDENTIALS);
            add(_LAST_PROJECT_KEY);
            add(_LAST_ASSIGNEE_NAME);
            add(_LAST_COMPONENTS_IDS);
            add(_LAST_SPREADSHEET_URL);
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
