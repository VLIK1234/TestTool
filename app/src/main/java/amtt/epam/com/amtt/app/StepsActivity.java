package amtt.epam.com.amtt.app;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.StepsAdapter;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.StepsWithMetaTable;

public class StepsActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER = 0;

    private ListView mListView;
    private StepsAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        mListView = (ListView) findViewById(android.R.id.list);

        getLoaderManager().initLoader(CURSOR_LOADER,null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, AmttContentProvider.STEP_WITH_META_CONTENT_URI, StepsWithMetaTable.PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter == null) {
            mAdapter = new StepsAdapter(StepsActivity.this, null, 0);
            mListView.setAdapter(mAdapter);
        }
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
