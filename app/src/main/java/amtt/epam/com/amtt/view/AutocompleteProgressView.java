package amtt.epam.com.amtt.view;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import amtt.epam.com.amtt.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Iryna_Monchanka on 5/20/2015.
 */
public class AutocompleteProgressView extends RelativeLayout {

    private AutoCompleteTextView mACTextView;
    private ProgressBar mProgress;
    private ArrayAdapter<String> mEmptyAdapter;

    public AutocompleteProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.autocomplete_progress_layout, this, true);
        String[] strings = new String[]{};
        List<String> items = new ArrayList<>(Arrays.asList(strings));
        mEmptyAdapter = new ArrayAdapter<>(context, R.layout.spinner_layout, items);
        mEmptyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        initViews();
    }

    private void initViews() {
        mACTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_text_view);
        mACTextView.setAdapter(mEmptyAdapter);
        mEmptyAdapter.notifyDataSetChanged();
        mProgress = (ProgressBar) findViewById(R.id.progress);
    }

    public void showProgress(boolean enabled) {
        if (enabled) {
            mProgress.setVisibility(View.VISIBLE);
            mACTextView.setAdapter(mEmptyAdapter);
        } else {
            mProgress.setVisibility(View.GONE);
        }
    }

    public void setAdapter(ArrayAdapter<String> adapter) {
        mACTextView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setThreshold(int threshold) {
        mACTextView.setThreshold(threshold);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        mACTextView.addTextChangedListener(watcher);
    }
}
