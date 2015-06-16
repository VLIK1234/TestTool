package amtt.epam.com.amtt.database;

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
import amtt.epam.com.amtt.database.table.IssuetypeTable;
import amtt.epam.com.amtt.database.table.PriorityTable;
import amtt.epam.com.amtt.database.table.ProjectTable;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.database.table.Table;
import amtt.epam.com.amtt.database.table.UsersTable;

/**
 @author Artsiom_Kaliaha
 @version on 18.03.2015
 */

public class DataBaseManager extends SQLiteOpenHelper {

    private static final Integer DATA_BASE_VERSION = 7;
    private static final String DATA_BASE_NAME = "amtt.db";
    private static final List<Class> sTables;

    static {
        sTables = new ArrayList<>();
        sTables.add(ActivityInfoTable.class);
        sTables.add(StepsTable.class);
        sTables.add(UsersTable.class);
        sTables.add(PriorityTable.class);
        sTables.add(ProjectTable.class);
        sTables.add(IssuetypeTable.class);
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
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void dropTables(SQLiteDatabase db) {
        try {
            for (Class table : sTables) {
                db.execSQL(BaseColumns.DROP + ((Table) table.newInstance()).getTableName());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public Cursor query(String tableName, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        SQLiteDatabase database = getReadableDatabase();

        try {
            database.beginTransaction();
            cursor = getReadableDatabase().query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        return cursor;
    }

    public Cursor joinQuery(String[] tablesName, String[] projection, String[] connectionColumns) {
        StringBuilder rawQueryBuilder = new StringBuilder();
        rawQueryBuilder.append(SqlQueryConstants.SELECT);

        for (int i = 0; i < projection.length; i++) {
            rawQueryBuilder.append(projection[i]);
            if (i != projection.length - 1) {
                rawQueryBuilder.append(SqlQueryConstants.COMMA);
            }
        }

        final String firstTable = tablesName[0];
        final String secondTable = tablesName[1];
        rawQueryBuilder.append(SqlQueryConstants.FROM).append(firstTable)
                .append(SqlQueryConstants.JOIN).append(secondTable)
                .append(SqlQueryConstants.ON).append(firstTable).append(SqlQueryConstants.DOT).append(connectionColumns[0])
                .append(SqlQueryConstants.EQUALS)
                .append(secondTable).append(SqlQueryConstants.DOT).append(connectionColumns[1]);

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
            id = database.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        return id;
    }

    public int bulkInsert(String tableName, ContentValues[] values) {
        SQLiteDatabase database = getWritableDatabase();
        try {
            database.beginTransaction();
            for (ContentValues value : values) {
                database.insert(tableName, null, value);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
        return values.length;
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
        }
        return updatedRows;
    }

}
