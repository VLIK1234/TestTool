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
    public static final String _URL = "_url";
    public static final String _KEY = "_key";
    public static final String _EMAIL = "_email";
    public static final String _ACTIVE = "_active";
    public static final String _AVATAR_16 = "_avatar_16";
    public static final String _AVATAR_24 = "_avatar_24";
    public static final String _AVATAR_32= "_avatar_32";
    public static final String _AVATAR_48 = "_avatar_48";

    public static final String ACTIVE_USER_MARKER = "1";

    private static MultiValueMap<String, String> sColumnsMap;

    public static final String[] PROJECTION = {
            _ID,
            _USER_NAME,
            _URL,
            _KEY,
            _EMAIL,
            _ACTIVE,
            _AVATAR_16,
            _AVATAR_24,
            _AVATAR_32,
            _AVATAR_48
    };

    static {
        sColumnsMap = new MultiValueMap<>();
        sColumnsMap.put(TYPE_TEXT, new ArrayList<String>() {{
            add(_USER_NAME);
            add(_KEY);
            add(_URL);
            add(_EMAIL);
            add(_ACTIVE);
            add(_AVATAR_16);
            add(_AVATAR_24);
            add(_AVATAR_32);
            add(_AVATAR_48);
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
