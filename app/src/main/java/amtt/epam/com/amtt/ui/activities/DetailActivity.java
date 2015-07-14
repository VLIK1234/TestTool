package amtt.epam.com.amtt.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.excel.api.loadcontent.XMLContent;
import amtt.epam.com.amtt.excel.bo.GoogleEntryWorksheet;

/**
 * @author Iryna Monchanka
 * @version on 7/13/2015
 */

public class DetailActivity extends BaseActivity{

    public static final String TESTCASE_ID = "testcase_key";
    private TextView mNameTextView;
    private TextView mLabelTextView;
    private TextView mPriorityTextView;
    private TextView mStepsTextView;
    private TextView mDescriptionTextView;
    private TextView mExpectedResultsTextView;
    private ImageButton mBugButton;
    private GoogleEntryWorksheet testcase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mNameTextView = (TextView) findViewById(R.id.tv_testcase_name);
        mLabelTextView = (TextView) findViewById(R.id.tv_label);
        mPriorityTextView = (TextView) findViewById(R.id.tv_priority);
        mStepsTextView = (TextView) findViewById(R.id.tv_steps);
        mDescriptionTextView = (TextView) findViewById(R.id.tv_description);
        mExpectedResultsTextView = (TextView) findViewById(R.id.tv_expected_results);
        mBugButton = (ImageButton) findViewById(R.id.btn_bug);
        mBugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(testcase!=null){
                    XMLContent.getInstance().setLastTestCase(testcase);
                    Intent loginIntent = new Intent(DetailActivity.this, CreateIssueActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
        });
        Bundle extra = getIntent().getExtras();
        String testCaseId = null;

        if (extra != null) {
            testCaseId = extra.getString(TESTCASE_ID);
        }
        if (testCaseId != null && !testCaseId.equals("")) {
            testcase = XMLContent.getInstance().getTestcaseByIdGSX(testCaseId);
            if (testcase != null) {
                if (testcase.getTestCaseNameGSX() != null) {
                    mNameTextView.setText(testcase.getTestCaseNameGSX());
                }
                mLabelTextView.setText(testcase.getLabelGSX());
                mPriorityTextView.setText(testcase.getPriorityGSX());
                mStepsTextView.setText(testcase.getTestStepsGSX());
                mDescriptionTextView.setText(testcase.getTestCaseDescriptionGSX());
                mExpectedResultsTextView.setText(testcase.getExpectedResultGSX());
            }
        }
    }
}
