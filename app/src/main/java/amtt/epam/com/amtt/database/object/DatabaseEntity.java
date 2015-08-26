package amtt.epam.com.amtt.database.object;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 @author Artsiom_Kaliaha
 @version on 15.05.2015
 */

public abstract class DatabaseEntity<B> implements IDatabaseEntityMethod<B>{

    protected DatabaseEntity() {
    }

    protected DatabaseEntity(Cursor cursor) {
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
