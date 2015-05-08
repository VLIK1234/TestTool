package amtt.epam.com.amtt.database.table;

import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.util.MultiValueMap;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 07.05.2015.
 */
public class IssuetypeTable extends Table implements BaseColumns {

    public static final String TABLE_NAME = "issuetype";

    public static final String _ID_PROJECT = "_id_project";
    public static final String _NAME = "_name";

    private static MultiValueMap<String, String> sColumnsMap;

    public static final String[] PROJECTION = {
        _ID,
        ProjectTable._KEY,
        _NAME,
    };

    static {
        sColumnsMap = new MultiValueMap<>();
        sColumnsMap.put(TYPE_TEXT, new ArrayList<String>() {{
            add(ProjectTable._KEY);
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


