package amtt.epam.com.amtt.app;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.StepsAdapter;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.table.StepsWithMetaTable;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StepsActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER = 0;

    private ListView mListView;
    private StepsAdapter mAdapter;
    private ProgressBar mProgressBar;
    private TextView mEmptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        mListView = (ListView) findViewById(android.R.id.list);
        mProgressBar = (ProgressBar) findViewById(android.R.id.progress);
        mEmptyText = (TextView) findViewById(android.R.id.empty);
        getLoaderManager().initLoader(CURSOR_LOADER, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mProgressBar.setVisibility(View.VISIBLE);
        return new CursorLoader(this, AmttContentProvider.STEP_WITH_META_CONTENT_URI, StepsWithMetaTable.PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() != 0) {
            if (mAdapter == null) {
                //TODO why do you init adapter with null and then call swapCursor?
                mAdapter = new StepsAdapter(StepsActivity.this, null, 0);
                mListView.setAdapter(mAdapter);
            }
            mAdapter.swapCursor(data);
            //TODO progress is hidden in both cases
            mProgressBar.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
