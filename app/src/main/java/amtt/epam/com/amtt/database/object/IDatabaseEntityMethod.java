package amtt.epam.com.amtt.database.object;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Ivan_Bakach on 25.05.2015.
 */
public interface IDatabaseEntityMethod<B> {

    B parse(Cursor cursor);

    ContentValues getContentValues();

    Uri getUri();

    int getId();
}
