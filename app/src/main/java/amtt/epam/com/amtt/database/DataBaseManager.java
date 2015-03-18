package amtt.epam.com.amtt.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Artsiom_Kaliaha on 18.03.2015.
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private static final Integer DATA_BASE_VERSION = 1;
    private static final String DATA_BASE_NAME = "amtt.db";

    public DataBaseManager(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ActivityMetaTable.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
