package amtt.epam.com.amtt.database.object;

import android.content.ContentValues;
import android.net.Uri;

/**
 * Created by Ivan_Bakach on 25.05.2015.
 */
public interface IDatabaseEntityMethod {
    ContentValues getContentValues();

    Uri getUri();

    int getId();
}
