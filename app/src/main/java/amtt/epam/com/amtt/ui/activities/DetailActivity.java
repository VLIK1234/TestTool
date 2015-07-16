package amtt.epam.com.amtt.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.excel.api.loadcontent.XMLContent;
import amtt.epam.com.amtt.excel.bo.GoogleEntryWorksheet;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.Constants;

/**
 * @author Iryna Monchanka
 * @version on 7/13/2015
 */

public class DetailActivity extends BaseActivity {

    private TextView mNameTextView;
    private TextView mLabelTextView;
    private TextView mPriorityTextView;
    private TextView mStepsTextView;
    private TextView mDescriptionTextView;
    private TextView mExpectedResultsTextView;
    private GoogleEntryWorksheet mTestcase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        TopButtonService.sendActionChangeTopButtonVisibility(false);
        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TopButtonService.sendActionChangeTopButtonVisibility(true);
    }

    private void setTestcaseData() {
        String testCaseId = XMLContent.getInstance().getLastTestcaseId();
        if (testCaseId != null && !testCaseId.equals(Constants.Symbols.EMPTY)) {
            mTestcase = XMLContent.getInstance().getTestcaseByIdGSX(testCaseId);
            if (mTestcase != null) {
                if (mTestcase.getTestCaseNameGSX() != null) {
                    mNameTextView.setText(mTestcase.getTestcaseNameAndId());
                }
                mLabelTextView.setText(mTestcase.getLabelGSX());
                mPriorityTextView.setText(mTestcase.getPriorityGSX());
                mStepsTextView.setText(mTestcase.getTestStepsGSX());
                mDescriptionTextView.setText(mTestcase.getTestCaseDescriptionGSX());
                mExpectedResultsTextView.setText(mTestcase.getExpectedResultGSX());
            }
        }
    }

    private void initViews() {
        mNameTextView = (TextView) findViewById(R.id.tv_testcase_name);
        mLabelTextView = (TextView) findViewById(R.id.tv_label);
        mPriorityTextView = (TextView) findViewById(R.id.tv_priority);
        mStepsTextView = (TextView) findViewById(R.id.tv_steps);
        mDescriptionTextView = (TextView) findViewById(R.id.tv_description);
        mExpectedResultsTextView = (TextView) findViewById(R.id.tv_expected_results);
        initBugButton();
        setTestcaseData();
    }

    private void initBugButton() {
        FloatingActionButton mBugButton = (FloatingActionButton) findViewById(R.id.btn_bug);
        mBugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTestcase != null) {
                    XMLContent.getInstance().setLastTestCase(mTestcase);
                    Intent loginIntent = new Intent(DetailActivity.this, CreateIssueActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
        });
    }

}
