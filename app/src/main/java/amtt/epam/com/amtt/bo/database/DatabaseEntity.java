package amtt.epam.com.amtt.bo.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Artsiom_Kaliaha on 15.05.2015.
 */
public abstract class DatabaseEntity {

    public DatabaseEntity() {
    }

    public DatabaseEntity(Cursor cursor) {
    }

    public abstract ContentValues getContentValues();

    public abstract Uri getUri();

    public abstract int getId();

}
