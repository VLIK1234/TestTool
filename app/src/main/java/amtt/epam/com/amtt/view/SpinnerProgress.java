package amtt.epam.com.amtt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import amtt.epam.com.amtt.R;

/**
 * Created by Iryna_Monchanka on 5/20/2015.
 */

public class SpinnerProgress extends RelativeLayout {
    private Spinner mSpinner;
    private ProgressBar mProgress;
    private ArrayAdapter<String> mEmptyAdapter;
    private  Context mContext;

    public SpinnerProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.spinner_progress_layout, this, true);
        String[] strings = new String[]{};
        List<String> items = new ArrayList<>(Arrays.asList(strings));
        mEmptyAdapter = new ArrayAdapter<>(context, R.layout.spinner_layout, items);
        initViews();
    }

    private void initViews() {
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mProgress = (ProgressBar) findViewById(R.id.progress);
    }

    public void showProgress(boolean enabled) {
        if (enabled)
            mSpinner.setAdapter(mEmptyAdapter);
        mProgress.setVisibility(enabled ? View.VISIBLE : View.GONE);

    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mSpinner.setTag(getId());
        mSpinner.setOnItemSelectedListener(listener);
    }

    public void setAdapter(SpinnerAdapter adapter) {
        mSpinner.setAdapter(adapter);
    }

    public int getSelectedItemPosition() {
        return this.mSpinner.getSelectedItemPosition();
    }

    public SpinnerAdapter getAdapter() {
        return this.mSpinner.getAdapter();
    }

    public void setSelection(int position) {
        mSpinner.setSelection(position);
    }
}