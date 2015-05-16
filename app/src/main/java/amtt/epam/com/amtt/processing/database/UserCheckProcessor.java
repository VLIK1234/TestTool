package amtt.epam.com.amtt.processing.database;

import android.database.Cursor;

import amtt.epam.com.amtt.processing.Processor;

/**
 * Created by Artyom on 16.05.2015.
 */
public class UserCheckProcessor implements Processor<Boolean, Cursor> {

    @Override
    public Boolean process(Cursor cursor) throws Exception {
        return cursor.getCount() != 0;
    }
}
