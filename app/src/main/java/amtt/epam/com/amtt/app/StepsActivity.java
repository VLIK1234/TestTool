package amtt.epam.com.amtt.app;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.StepAdapter;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;

public class StepsActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    private static final int CURSOR_LOADER = 0;
    private static final int MESSAGE_REFRESH = 100;
    private ListView mListView;
    private StepAdapter mAdapter;
    private TextView mEmptyText;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private StepsHandler mHandler;

    public static class StepsHandler extends Handler {

        private final WeakReference<StepsActivity> mActivity;

        StepsHandler(StepsActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            StepsActivity service = mActivity.get();
            service.refreshSteps();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        TopButtonService.close(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        initViews();
        mHandler = new StepsHandler(StepsActivity.this);
        mSwipeRefreshLayout.setOnRefreshListener(StepsActivity.this);
        getLoaderManager().initLoader(CURSOR_LOADER, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TopButtonService.start(this);
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

    private void initViews() {
        mListView = (ListView) findViewById(android.R.id.list);
        mEmptyText = (TextView) findViewById(android.R.id.empty);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mSwipeRefreshLayout.setRefreshing(true);
        return new CursorLoader(this, AmttUri.STEP_WITH_META.get(), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() != 0) {
            if (mAdapter == null) {
                mAdapter = new StepAdapter(StepsActivity.this, data, 0);
                mListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mEmptyText.setVisibility(View.GONE);
            } else {
                mAdapter.swapCursor(data);
                mAdapter.notifyDataSetChanged();
                mEmptyText.setVisibility(View.GONE);
            }
        } else {
            mEmptyText.setVisibility(View.VISIBLE);
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onRefresh() {
        mHandler.removeMessages(MESSAGE_REFRESH);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_REFRESH), 750);
    }

    private void refreshSteps() {
        getLoaderManager().restartLoader(CURSOR_LOADER, null, this);
    }

}
