package amtt.epam.com.amtt.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater factory = LayoutInflater.from(getBaseContext());
        mNameTextView = (TextView) findViewById(R.id.tv_testcase_name);
        mLabelTextView = (TextView) findViewById(R.id.tv_label);
        mPriorityTextView = (TextView) findViewById(R.id.tv_priority);
        mStepsTextView = (TextView) findViewById(R.id.tv_steps);
        mDescriptionTextView = (TextView) findViewById(R.id.tv_description);
        mExpectedResultsTextView = (TextView) findViewById(R.id.tv_expected_results);
        Bundle extra = getIntent().getExtras();
        String testCaseId = null;
        GoogleEntryWorksheet testcase;
        if (extra != null) {
            testCaseId = extra.getString(TESTCASE_ID);
        }
        if (testCaseId != null && !testCaseId.equals("")) {
            testcase = XMLContent.getInstance().getTestcaseByIdGSX(testCaseId);
            mNameTextView.setText(testcase.getTestCaseNameGSX());
            mLabelTextView.setText(testcase.getLabelGSX());
            mPriorityTextView.setText(testcase.getPriorityGSX());
            mStepsTextView.setText(testcase.getTestStepsGSX());
            mDescriptionTextView.setText(testcase.getTestCaseDescriptionGSX());
            mExpectedResultsTextView.setText(testcase.getExpectedResultGSX());
        }

    }


}
