package amtt.epam.com.amtt.database;

import java.util.ArrayList;

import amtt.epam.com.amtt.util.MultiValueMap;

/**
 * Created by Artsiom_Kaliaha on 26.03.2015.
 */
public class StepsTable extends Table {

    public static final String TABLE_NAME = "steps";

    public static final String _STEP = "_step";
    public static final String _SCREEN_PATH = "_screen_path";
    public static final String _ASSOCIATED_ACTIVITY = "_associated_activity";

    private static MultiValueMap<String, String> sColumnsMap;

    public static final String[] PROJECTION = {
            _STEP,
            _SCREEN_PATH,
            _ASSOCIATED_ACTIVITY
    };

    static {
        sColumnsMap = new MultiValueMap<>();
        sColumnsMap.put(TYPE_TEXT, new ArrayList<String>() {{
            add(_SCREEN_PATH);
            add(_ASSOCIATED_ACTIVITY);
        }});
        sColumnsMap.put(TYPE_INTEGER + AUTO_INCREMETN, new ArrayList<String>() {{
            add(_STEP);
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
