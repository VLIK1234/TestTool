package amtt.epam.com.amtt.database;

/**
 * Created by Artsiom_Kaliaha on 19.03.2015.
 */
public interface BaseColumns extends android.provider.BaseColumns {

    String DIVIDER = ", ";

    String TYPE_TEXT = "TEXT";
    String TYPE_INTEGER = "INTEGER";
    String TYPE_REAL = "REAL";
    String TYPE_BLOB = "BLOB";

    String CREATE = "CREATE TABLE IF NOT EXISTS ";
    String DROP = "DROP TABLE IF EXISTS ";
    String AUTO_INCREMETN = " PRIMARY KEY AUTOINCREMENT";

}
