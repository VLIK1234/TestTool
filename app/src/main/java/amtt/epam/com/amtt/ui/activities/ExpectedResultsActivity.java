package amtt.epam.com.amtt.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.ExpectedResultAdapter;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.excel.api.loadcontent.XMLContent;
import amtt.epam.com.amtt.excel.bo.GoogleEntryWorksheet;
import amtt.epam.com.amtt.excel.bo.GoogleWorksheet;
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
        TopButtonService.sendActionChangeTopButtonVisibility(true);
    }

    private void initViews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        ArrayList<ExpectedResultAdapter.ExpectedResult> mExpectedResultsList = new ArrayList<>();
        mResultsAdapter = new ExpectedResultAdapter(ExpectedResultsActivity.this, mExpectedResultsList);
        mExpectedResultsListView = (ListView) findViewById(android.R.id.list);

    }

    @Override
    public void onRefresh() {
        mHandler.removeMessages(MESSAGE_REFRESH);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_REFRESH), 750);
    }

    private void refreshSteps() {
        XMLContent.getInstance().getWorksheet(new GetContentCallback<GoogleWorksheet>() {
            @Override
            public void resultOfDataLoading(GoogleWorksheet result) {
                if(result != null){
                    List<GoogleEntryWorksheet> entryWorksheetList = result.getEntry();
                    if (entryWorksheetList != null) {
                        if (!entryWorksheetList.isEmpty()) {
                            for (int i = 1; i < entryWorksheetList.size(); i++) {
                                if (entryWorksheetList.get(i).getTestCaseNameGSX() != null) {
                                    ExpectedResultAdapter.ExpectedResult expectedResult = new ExpectedResultAdapter.ExpectedResult(entryWorksheetList.get(i).getLabelGSX(),
                                            entryWorksheetList.get(i).getTestCaseNameGSX(),
                                            entryWorksheetList.get(i).getPriorityGSX(),
                                            entryWorksheetList.get(i).getTestStepsGSX(),
                                            entryWorksheetList.get(i).getIdGSX());
                                    mResultsAdapter.add(expectedResult);
                                }
                            }
                            mExpectedResultsListView.setAdapter(mResultsAdapter);
                        }
                    }
                }
            }
        });


        mSwipeRefreshLayout.setRefreshing(false);
    }

}
