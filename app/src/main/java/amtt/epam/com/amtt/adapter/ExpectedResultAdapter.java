package amtt.epam.com.amtt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import amtt.epam.com.amtt.R;

/**
 * @author Iryna Monchanka
 * @version on 6/9/2015
 */

public class ExpectedResultAdapter extends ArrayAdapter<ExpectedResultAdapter.ExpectedResult> {

    public static class ExpectedResult {

        private String mLabel;
        private String mTestcaseName;
        private String mPriority;
        private String mSteps;
        private String mExpectedResults;
        private String mScreenshotPath;

        public ExpectedResult(String mLabel, String mTestcaseName, String mPriority, String mSteps, String mExpectedResults, String mScreenshotPath) {
            this.mLabel = mLabel;
            this.mTestcaseName = mTestcaseName;
            this.mPriority = mPriority;
            this.mSteps = mSteps;
            this.mExpectedResults = mExpectedResults;
            this.mScreenshotPath = mScreenshotPath;
        }
    }

    public ExpectedResultAdapter(Context context, ArrayList<ExpectedResult> expectedResults) {
        super(context, 0, expectedResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExpectedResult expectedResult = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_expected_results, parent, false);
        }
        TextView label = (TextView) convertView.findViewById(R.id.tv_label);
        TextView testcaseName = (TextView) convertView.findViewById(R.id.tv_testcase_name);
        TextView priority = (TextView) convertView.findViewById(R.id.tv_priority);
        TextView steps = (TextView) convertView.findViewById(R.id.tv_steps);
        TextView expectedResults = (TextView) convertView.findViewById(R.id.tv_expected_results);
        ImageView screenshot = (ImageView) convertView.findViewById(R.id.iv_screenshot);
        label.setText(expectedResult.mLabel);
        testcaseName.setText("Testcase name : " + expectedResult.mTestcaseName);
        priority.setText("Priority : " + expectedResult.mPriority);
        steps.setText("Steps : " + expectedResult.mSteps);
        expectedResults.setText("Expected results : " + expectedResult.mExpectedResults);
        ImageLoader.getInstance().displayImage(expectedResult.mScreenshotPath, screenshot);
        return convertView;
    }

}
