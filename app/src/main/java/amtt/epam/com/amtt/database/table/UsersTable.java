package amtt.epam.com.amtt.database.table;

import java.util.ArrayList;

import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.util.MultiValueMap;

/**
 * Created by Artsiom_Kaliaha on 29.04.2015.
 */
public class UsersTable extends Table implements BaseColumns {

    public static final String TABLE_NAME = "users";

    public static final String _USER_NAME = "_user_name";
    public static final String _PASSWORD = "_password";
    public static final String _KEY = "_key";
    public static final String _EMAIL = "_email";
    public static final String _AVATAR_SMALL = "_avatar_small";
    public static final String _AVATAR_X_SMALL = "_avatar_x_small";
    public static final String _AVATAR_MEDIUM = "_avatar_medium";

    private static MultiValueMap<String, String> sColumnsMap;

    public static final String[] PROJECTION = {
            _ID,
            _USER_NAME,
            _PASSWORD,
            _KEY,
            _EMAIL,
            _AVATAR_SMALL,
            _AVATAR_X_SMALL,
            _AVATAR_MEDIUM
    };

    static {
        sColumnsMap = new MultiValueMap<>();
        sColumnsMap.put(TYPE_TEXT, new ArrayList<String>() {{
            add(_USER_NAME);
            add(_PASSWORD);
            add(_KEY);
            add(_EMAIL);
            add(_AVATAR_SMALL);
            add(_AVATAR_X_SMALL);
            add(_AVATAR_MEDIUM);
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
