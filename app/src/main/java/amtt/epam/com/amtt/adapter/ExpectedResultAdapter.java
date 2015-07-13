package amtt.epam.com.amtt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        private String mId;

        public ExpectedResult(String label, String testcaseName, String priority, String steps, String id) {
            this.mLabel = label;
            this.mTestcaseName = testcaseName;
            this.mPriority = priority;
            this.mSteps = steps;
            this.mId = id;
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
        label.setText(expectedResult.mLabel+" [ " + expectedResult.mId + " ]");
        testcaseName.setText(expectedResult.mTestcaseName);
        priority.setText(expectedResult.mPriority);
        steps.setText(expectedResult.mSteps);
        return convertView;
    }

}
