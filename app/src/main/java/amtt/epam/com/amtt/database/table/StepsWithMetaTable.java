package amtt.epam.com.amtt.database.table;

import amtt.epam.com.amtt.util.MultiValueMap;

/**
 * Created by Artsiom_Kaliaha on 31.03.2015.
 */
public class StepsWithMetaTable extends Table {

    public static final String TABLE_NAME = "steps_with_meta";

    public static final String[] PROJECTION = {
        _ID,
        StepsTable._SCREEN_PATH,
        StepsTable._ASSOCIATED_ACTIVITY,
        ActivityInfoTable._CONFIG_CHANGES,
        ActivityInfoTable._FLAGS,
        ActivityInfoTable._LAUNCH_MODE,
        ActivityInfoTable._MAX_RECENTS,
        ActivityInfoTable._PARENT_ACTIVITY_NAME,
        ActivityInfoTable._PERMISSION,
        ActivityInfoTable._PERSISTABLE_MODE,
        ActivityInfoTable._SCREEN_ORIENTATION,
        ActivityInfoTable._SOFT_INPUT_MODE,
        ActivityInfoTable._TARGET_ACTIVITY_NAME,
        ActivityInfoTable._TASK_AFFINITY,
        ActivityInfoTable._THEME,
        ActivityInfoTable._UI_OPTIONS,
        ActivityInfoTable._PROCESS_NAME,
        ActivityInfoTable._PACKAGE_NAME
    };

    @Override
    public MultiValueMap<String, String> getColumnsMap() {
        return null;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
