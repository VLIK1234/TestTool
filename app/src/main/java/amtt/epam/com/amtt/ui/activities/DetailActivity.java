package amtt.epam.com.amtt.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        TopButtonService.sendActionChangeTopButtonVisibility(false);
        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TopButtonService.sendActionChangeTopButtonVisibility(true);
    }

    private void setTestcaseData() {
        checkIntent();
        if (mTestcase != null) {
            if (mTestcase.getTestCaseNameGSX() != null) {
                mNameTextView.setText(mTestcase.getTestCaseNameGSX());
            }
            if (mTestcase.getPriorityGSX() != null) {
                mPriorityTextView.setText(mTestcase.getPriorityGSX());
            }
            if (mTestcase.getTestStepsGSX() != null) {
                mStepsTextView.setText(mTestcase.getTestStepsGSX());
            }
            if (mTestcase.getExpectedResultGSX() != null) {
                mExpectedResultsTextView.setText(mTestcase.getExpectedResultGSX());
            }
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
            mTestcase = new GEntryWorksheet();
            mTestcase.setTestCaseNameGSX(extra.getString(ExpectedResultsActivity.NAME));
            mTestcase.setExpectedResultGSX(extra.getString(ExpectedResultsActivity.EXPECTED_RESULT));
            mTestcase.setPriorityGSX(extra.getString(ExpectedResultsActivity.PRIORITY));
            mTestcase.setTestStepsGSX(extra.getString(ExpectedResultsActivity.STEPS));
        }
    }

    private void initBugButton() {
        FloatingActionButton mBugButton = (FloatingActionButton) findViewById(R.id.btn_bug);
        mBugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTestcase != null) {
                    Intent loginIntent = new Intent(DetailActivity.this, CreateIssueActivity.class);
                    loginIntent.putExtra(ExpectedResultsActivity.NAME, mTestcase.getTestCaseNameGSX());
                    loginIntent.putExtra(ExpectedResultsActivity.PRIORITY, mTestcase.getPriorityGSX());
                    loginIntent.putExtra(ExpectedResultsActivity.STEPS, mTestcase.getTestStepsGSX());
                    loginIntent.putExtra(ExpectedResultsActivity.EXPECTED_RESULT, mTestcase.getExpectedResultGSX());
                    startActivity(loginIntent);
                    finish();
                }
            }
        });
    }

}
