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

        private String mActivityName;
        private String mScreenshotPath;
        private String mDescriptionResult;

        public ExpectedResult(String activityName, String screenshotPath, String descriptionResult) {
            this.mActivityName = activityName;
            this.mScreenshotPath = screenshotPath;
            this.mDescriptionResult = descriptionResult;
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
        TextView activityName = (TextView) convertView.findViewById(R.id.tv_name_activity);
        TextView descriptionResult = (TextView) convertView.findViewById(R.id.tv_description_result);
        ImageView screenshot = (ImageView) convertView.findViewById(R.id.iv_screenshot);
        activityName.setText(expectedResult.mActivityName);
        descriptionResult.setText(expectedResult.mDescriptionResult);
        ImageLoader.getInstance().displayImage(expectedResult.mScreenshotPath, screenshot);
        return convertView;
    }

}
