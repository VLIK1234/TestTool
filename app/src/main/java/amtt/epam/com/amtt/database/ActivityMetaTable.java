package amtt.epam.com.amtt.database;

/**
 * Created by Artsiom_Kaliaha on 18.03.2015.
 */
public final class ActivityMetaTable implements BaseColumns {

    private static final String TABLE_NAME = "activity_meta_table";
    private static final String _NAME = "_name";
    private static final String _STACK_POSITION = "_stack_position";

    static final String CREATE_TABLE = CREATE + TABLE_NAME + " ( " +
            _ID + " " + TYPE_INTEGER + DIVIDER +
            _NAME + " " + TYPE_TEXT + DIVIDER +
            _STACK_POSITION + " " + TYPE_INTEGER + ")";

}
