package amtt.epam.com.amtt.database;

import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Artsiom_Kaliaha on 27.03.2015.
 */
public class DbClearTask extends AsyncTask<Void, Void, Void> {

    private final Context mContext;

    public DbClearTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        mContext.getContentResolver().delete(AmttContentProvider.ACTIVITY_META_CONTENT_URI, null, null);
        mContext.getContentResolver().delete(AmttContentProvider.STEP_CONTENT_URI, null, null);
        return null;
    }
}
