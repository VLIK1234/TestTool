package amtt.epam.com.amtt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
        db.execSQL(new ActivityInfoTable().getCreateQuery());
        db.execSQL(new StepsTable().getCreateQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor query(String tableName, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        SQLiteDatabase database = getReadableDatabase();

        try {
            database.beginTransaction();
            cursor = getReadableDatabase().query(tableName, projection, selection + "=?", selectionArgs, null, null, sortOrder);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

        return cursor;
    }

    public long insert(String tableName, ContentValues values) {
        long id;

        SQLiteDatabase database = getWritableDatabase();

        try {
            database.beginTransaction();
            id = database.insert(tableName, null, values);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }

        return id;
    }

    public int delete(String tableName, String selection, String[] selectionArgs) {
        int deletedRows;
        SQLiteDatabase database = getWritableDatabase();

        try {
            database.beginTransaction();
            deletedRows = database.delete(tableName, selection, selectionArgs);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }

        return deletedRows;
    }

    public int update(String tableName, ContentValues values, String selection, String[] selectionArgs) {
        int updatedRows;

        SQLiteDatabase database = getWritableDatabase();
        try {
            database.beginTransaction();
            updatedRows = database.update(tableName, values, selection, selectionArgs);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
            database.close();
        }
        return updatedRows;
    }

}
