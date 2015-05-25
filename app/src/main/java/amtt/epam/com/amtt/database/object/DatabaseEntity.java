package amtt.epam.com.amtt.database.object;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Artsiom_Kaliaha on 15.05.2015.
 */
public class DatabaseEntity implements IDatabaseEntityMethod{

    public DatabaseEntity() {
    }

    public DatabaseEntity(Cursor cursor) {
    }

    @Override
    public ContentValues getContentValues() {
        return null;
    }

    @Override
    public Uri getUri() {
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }
}
