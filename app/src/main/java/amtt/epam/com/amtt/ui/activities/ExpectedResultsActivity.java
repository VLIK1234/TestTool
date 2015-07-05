package amtt.epam.com.amtt.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.ExpectedResultAdapter;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;

/**
 * @author Iryna Monchanka
 * @version on 03.06.2015
 */

public class ExpectedResultsActivity  extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int MESSAGE_REFRESH = 100;
    private ListView mExpectedResultsListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ExpectedResultsHandler mHandler;
    private ArrayList<ExpectedResultAdapter.ExpectedResult> mExpectedResultsList;
    private ExpectedResultAdapter mResultsAdapter;

    public static class ExpectedResultsHandler extends Handler {

        private final WeakReference<ExpectedResultsActivity> mActivity;

        ExpectedResultsHandler(ExpectedResultsActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ExpectedResultsActivity service = mActivity.get();
            service.refreshSteps();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expected_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        initViews();
        mHandler = new ExpectedResultsHandler(ExpectedResultsActivity.this);
        mSwipeRefreshLayout.setOnRefreshListener(ExpectedResultsActivity.this);
        refreshSteps();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TopButtonService.sendActionChangeVisibilityTopbutton(true);
    }

    private void initViews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mExpectedResultsList = new ArrayList<>();
        mResultsAdapter = new ExpectedResultAdapter(ExpectedResultsActivity.this, mExpectedResultsList);
        mExpectedResultsListView = (ListView) findViewById(android.R.id.list);

    }

    @Override
    public void onRefresh() {
        mHandler.removeMessages(MESSAGE_REFRESH);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_REFRESH), 750);
    }

    private void refreshSteps() {
        ExpectedResultAdapter.ExpectedResult result1 = new ExpectedResultAdapter.ExpectedResult("Top button", "assets://image3.png", "1) Center in parent");
        mResultsAdapter.add(result1);
        ExpectedResultAdapter.ExpectedResult result2 = new ExpectedResultAdapter.ExpectedResult("Top button", "assets://image2.png", "1) Center in parent\n2) Show Toast 'Start Record'");
        mResultsAdapter.add(result2);
        ExpectedResultAdapter.ExpectedResult result3 = new ExpectedResultAdapter.ExpectedResult("Top button", "assets://image4.png", "1) Align end\n2) Show child's buttons\n3) Rotate image main button");
        mResultsAdapter.add(result3);
        mExpectedResultsListView.setAdapter(mResultsAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
