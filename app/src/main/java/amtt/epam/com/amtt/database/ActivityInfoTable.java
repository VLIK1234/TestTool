package amtt.epam.com.amtt.database;

import java.util.ArrayList;

import amtt.epam.com.amtt.util.MultiValueMap;

/**
 * Created by Artsiom_Kaliaha on 18.03.2015.
 */
public final class ActivityInfoTable extends Table {

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
    public static final String _THEME = "_theme";
    public static final String _UI_OPTIONS = "_ui_options";
    public static final String _PROCESS_NAME = "_process_name";
    public static final String _PACKAGE_NAME = "_package_name";

    private static MultiValueMap<String, String> sColumnsMap;

    public static final String[] PROJECTION = {
            _ACTIVITY_NAME,
            _CONFIG_CHANGES,
            _FLAGS,
            _LAUNCH_MODE,
            _MAX_RECENTS,
            _PARENT_ACTIVITY_NAME,
            _PERMISSION,
            _PERSISTABLE_MODE,
            _SCREEN_ORIENTATION,
            _SOFT_INPUT_MODE,
            _TARGET_ACTIVITY_NAME,
            _TASK_AFFINITY,
            _THEME,
            _UI_OPTIONS,
            _PROCESS_NAME,
            _PACKAGE_NAME
    };

    static {
        sColumnsMap = new MultiValueMap<>();
        sColumnsMap.put(TYPE_TEXT, new ArrayList<String>() {{
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
            add(_THEME);
            add(_UI_OPTIONS);
            add(_PROCESS_NAME);
            add(_PACKAGE_NAME);
            add(_MAX_RECENTS);
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
