package amtt.epam.com.amtt.database.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.database.constant.BaseColumns;
import amtt.epam.com.amtt.database.constant.SqlQueryConstants;
import amtt.epam.com.amtt.database.table.ActivityInfoTable;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.database.table.Table;

/**
 * Created by Artsiom_Kaliaha on 18.03.2015.
 */
public class DataBaseManager extends SQLiteOpenHelper implements SqlQueryConstants, BaseColumns {

    private static final Integer DATA_BASE_VERSION = 3;
    private static final String DATA_BASE_NAME = "amtt.db";
    private static final List<Class> sTables;

    static {
        sTables = new ArrayList<>();
        sTables.add(ActivityInfoTable.class);
        sTables.add(StepsTable.class);
    }

    public DataBaseManager(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        try {
            for (Class table : sTables) {
                db.execSQL(((Table) table.newInstance()).getCreateQuery());
            }
        } catch (IllegalAccessException e) {
            //ignored
        } catch (InstantiationException e) {
            //ignored
        }
    }

    private void dropTables(SQLiteDatabase db) {
        try {
            for (Class table : sTables) {
                db.execSQL(DROP + ((Table)table.newInstance()).getTableName());
            }
        } catch (IllegalAccessException e) {
            //ignored
        } catch (InstantiationException e) {
            //ignored
        }
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

    public Cursor joinQuery(String[] tablesName, String[] projection, String[] connectionColumns) {
        StringBuilder rawQueryBuilder = new StringBuilder();
        rawQueryBuilder.append(SELECT);

        for (int i = 0; i < projection.length; i++) {
            rawQueryBuilder.append(projection[i]);
            if (i != projection.length - 1) {
                rawQueryBuilder.append(COMMA);
            }
        }

        final String firstTable = tablesName[0];
        final String secondTable = tablesName[1];
        rawQueryBuilder.append(FROM).append(firstTable)
                .append(JOIN).append(secondTable)
                .append(ON).append(firstTable).append(DOT).append(connectionColumns[0])
                .append(EQUALS)
                .append(secondTable).append(DOT).append(connectionColumns[1]);

        Cursor cursor;
        SQLiteDatabase database = getReadableDatabase();

        try {
            database.beginTransaction();
            cursor = getReadableDatabase().rawQuery(rawQueryBuilder.toString(), null);
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
