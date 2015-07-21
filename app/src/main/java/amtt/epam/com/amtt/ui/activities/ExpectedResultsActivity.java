package amtt.epam.com.amtt.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.ExpectedResultsAdapter;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.googleapi.api.loadcontent.GSpreadsheetContent;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.googleapi.bo.GWorksheet;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;

/**
 * @author Iryna Monchanka
 * @version on 03.06.2015
 */

public class ExpectedResultsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, ExpectedResultsAdapter.ViewHolder.ClickListener {

    private static final int MESSAGE_REFRESH = 100;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ExpectedResultsHandler mHandler;
    private ExpectedResultsAdapter mResultsAdapter;
    private RecyclerView mRecyclerView;
    private Boolean mIsShowDetail = false;

    @Override
    public void onShowCard(int position) {
        Intent detail = new Intent(ExpectedResultsActivity.this, DetailActivity.class);
        GSpreadsheetContent.getInstance().setLastTestcaseId(mResultsAdapter.getIdTestcaseList().get(position));
        mIsShowDetail = true;
        startActivity(detail);
        finish();
    }

    @Override
    public void onShowCreationTicket(int position) {
        Intent creationTicket = new Intent(ExpectedResultsActivity.this, CreateIssueActivity.class);
        GSpreadsheetContent.getInstance().setLastTestcaseId(mResultsAdapter.getIdTestcaseList().get(position));
        startActivity(creationTicket);
        finish();
    }

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
        TopButtonService.sendActionChangeTopButtonVisibility(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        initViews();
        mHandler = new ExpectedResultsHandler(ExpectedResultsActivity.this);
        mSwipeRefreshLayout.setOnRefreshListener(ExpectedResultsActivity.this);
        refreshSteps();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mIsShowDetail) {
            TopButtonService.sendActionChangeTopButtonVisibility(true);
        }
    }

    private void initViews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExpectedResultsActivity.this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onRefresh() {
        mHandler.removeMessages(MESSAGE_REFRESH);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_REFRESH), 750);
    }

    private void refreshSteps() {
        GSpreadsheetContent.getInstance().getWorksheet(new GetContentCallback<GWorksheet>() {
            @Override
            public void resultOfDataLoading(GWorksheet result) {
                if (result != null) {
                    List<GEntryWorksheet> entryWorksheetList = result.getEntry();
                    if (entryWorksheetList != null && !entryWorksheetList.isEmpty()) {
                        mResultsAdapter = new ExpectedResultsAdapter(entryWorksheetList, R.layout.adapter_expected_results, ExpectedResultsActivity.this);
                        mRecyclerView.setAdapter(mResultsAdapter);
                    }
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
