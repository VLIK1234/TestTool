package amtt.epam.com.amtt.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import amtt.epam.com.amtt.util.MultiValueMap;

/**
 * Created by Artsiom_Kaliaha on 18.03.2015.
 */
public final class ActivityInfoTable implements BaseColumns {

    public static final String TABLE_NAME = "activity_info";

    public static final String _ACTIVITY_NAME = "_activity_name";
    public static final String _CONFIG_CHANGES = "_config_changes";
    public static final String _FLAGS = "_flags";
    public static final String _LAUNCH_MODE = "_launch_mode";
    public static final String _MAX_RECENTS = "_max_recents";
    public static final String _PARENT_ACTIVITY_NAME = "_parent_activity_name";
    public static final String _PERMISSION = "_permission";
    public static final String _PERSISTABLE_MODE = "_persistable_mode";
    public static final String _SCREEN_ORIENTATION = "_screen_orientation";
    public static final String _SOFT_INPUT_MODE = "_soft_input_mode";
    public static final String _TARGET_ACTIVITY_NAME = "_target_activity_name";
    public static final String _TASK_AFFINITY = "_task_affinity";
    public static final String _THEME_RESOURCE_ID = "_theme_resource_id";
    public static final String _UI_OPTIONS = "_ui_options";
    public static final String _PROCESS_NAME = "_process_name";
    public static final String _META_NAME = "_meta_name";
    public static final String _META_RESOURCE = "_meta_resource";
    public static final String _META_VALUE = "_meta_value";
    public static final String _PACKAGE_NAME = "_package_name";

    private static MultiValueMap<String, String> sColoumnsMap;

    static {
        sColoumnsMap.put(TYPE_TEXT, new ArrayList<String>() {{
            add(_ACTIVITY_NAME);
            add(_CONFIG_CHANGES);
            add(_FLAGS);
            add(_LAUNCH_MODE);
            add(_PARENT_ACTIVITY_NAME);
            add(_PERMISSION);
            add(_PERSISTABLE_MODE);
            add(_SCREEN_ORIENTATION);
            add(_SOFT_INPUT_MODE);
            add(_TARGET_ACTIVITY_NAME);
            add(_TASK_AFFINITY);
            add(_UI_OPTIONS);
            add(_PROCESS_NAME);
            add(_META_NAME);
            add(_META_VALUE);
            add(_PACKAGE_NAME);
        }});

        sColoumnsMap.put(TYPE_INTEGER, new ArrayList<String>() {{
            add(_MAX_RECENTS);
            add(_THEME_RESOURCE_ID);
            add(_META_RESOURCE);
        }});
    }

    public String getCreateString() {
        StringBuilder createQuery = new StringBuilder();
        createQuery.append(BaseColumns.CREATE).append(TABLE_NAME).append(" ( ");

        Set<Map.Entry<String, List<String>>> keyValuePairs = sColoumnsMap.entrySet();

        for (Map.Entry<String, List<String>> pair : keyValuePairs) {
            List<String> columns = pair.getValue();
            for (String column : columns) {
                createQuery.append(column).append(" ").append(pair.getKey()).append(DIVIDER);
            }
        }
        createQuery.deleteCharAt(createQuery.length() - 2).append(" )");
        return createQuery.toString();
    }

}
