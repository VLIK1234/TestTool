package amtt.epam.com.amtt.database.table;

import java.util.ArrayList;

import amtt.epam.com.amtt.util.MultiValueMap;

/**
 * Created by Artsiom_Kaliaha on 29.04.2015.
 */
public class UsersTable extends Table {

    public static final String TABLE_NAME = "users_table";

    public static final String _USER_NAME = "_user_name";
    public static final String _PASSWORD = "_password";

    private static MultiValueMap<String, String> sColumnsMap;

    public static final String[] PROJECTION = {
            _USER_NAME,
            _PASSWORD
    };

    static {
        sColumnsMap = new MultiValueMap<>();
        sColumnsMap.put(TYPE_TEXT, new ArrayList<String>() {{
            add(_USER_NAME);
            add(_PASSWORD);
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
