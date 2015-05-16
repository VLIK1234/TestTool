package amtt.epam.com.amtt.database.constant;

/**
 * Created by Artsiom_Kaliaha on 19.03.2015.
 */
public final class BaseColumns implements android.provider.BaseColumns {

    public static final String DIVIDER = ", ";

    public static final String TYPE_TEXT = "TEXT";
    public static final String TYPE_INTEGER = "INTEGER";
    public static final String TYPE_REAL = "REAL";
    public static final String TYPE_BLOB = "BLOB";

    public static final String CREATE = "CREATE TABLE IF NOT EXISTS ";
    public static final String DROP = "DROP TABLE IF EXISTS ";

    public static final String PRIMARY_KEY = " PRIMARY KEY";
    public static final String UNIQUE = " UNIQUE ";
    public static final String ON_CONFLICT_REPLACE = "ON CONFLICT REPLACE";

}
