package amtt.epam.com.amtt.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.googleapi.api.GoogleApiConst;
import amtt.epam.com.amtt.googleapi.api.loadcontent.GSpreadsheetContent;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;

/**
 * @author Iryna Monchanka
 * @version on 7/13/2015
 */

public class DetailActivity extends BaseActivity {

    private TextView mNameTextView;
    private TextView mPriorityTextView;
    private TextView mStepsTextView;
    private TextView mExpectedResultsTextView;
    private GEntryWorksheet mTestcase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        TopButtonService.sendActionChangeTopButtonVisibility(false);
        checkIntent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TopButtonService.sendActionChangeTopButtonVisibility(true);
    }

    private void setTestcaseData() {
        if (mTestcase != null) {
            if (mTestcase.getTestCaseNameGSX() != null) {
                mNameTextView.setText(mTestcase.getTestCaseNameGSX());
            }
            mPriorityTextView.setText(mTestcase.getPriorityGSX());
            mStepsTextView.setText(mTestcase.getTestStepsGSX());
            mExpectedResultsTextView.setText(mTestcase.getExpectedResultGSX());
        }
    }

    private void initViews() {
        mNameTextView = (TextView) findViewById(R.id.tv_testcase_name);
        mPriorityTextView = (TextView) findViewById(R.id.tv_priority);
        mStepsTextView = (TextView) findViewById(R.id.tv_steps);
        mExpectedResultsTextView = (TextView) findViewById(R.id.tv_expected_results);
        initBugButton();
        setTestcaseData();

    }

    private void checkIntent() {
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            GSpreadsheetContent.getInstance().getTestcaseByIdLink(extra.getString(GoogleApiConst.LINK_TAG), new GetContentCallback<GEntryWorksheet>() {
                @Override
                public void resultOfDataLoading(GEntryWorksheet result) {
                    if (result != null) {
                        mTestcase = result;
                    }
                    initViews();
                }
            });
        } else {
            initViews();
        }
    }

    private void initBugButton() {
        FloatingActionButton mBugButton = (FloatingActionButton) findViewById(R.id.btn_bug);
        mBugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTestcase != null) {
                    Intent loginIntent = new Intent(DetailActivity.this, CreateIssueActivity.class);
                    loginIntent.putExtra(GoogleApiConst.LINK_TAG, mTestcase.getIdLink());
                    startActivity(loginIntent);
                    finish();
                }
            }
        });
    }

}
