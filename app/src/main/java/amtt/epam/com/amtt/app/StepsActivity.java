package amtt.epam.com.amtt.app;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.StepAdapter;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.table.StepsWithMetaTable;

public class StepsActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER = 0;

    private ListView mListView;
    private StepAdapter mAdapter;
    private TextView mEmptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mListView = (ListView) findViewById(android.R.id.list);
        mEmptyText = (TextView) findViewById(android.R.id.empty);
        getLoaderManager().initLoader(CURSOR_LOADER, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        showProgress(true);
        return new CursorLoader(this, AmttUri.STEP_WITH_META.get(), StepsWithMetaTable.PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() != 0) {
            if (mAdapter == null) {
                mAdapter = new StepAdapter(StepsActivity.this, data, 0);
                mListView.setAdapter(mAdapter);
            } else {
                mAdapter.swapCursor(data);
            }
        } else {
            mEmptyText.setVisibility(View.VISIBLE);
        }
        showProgress(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
