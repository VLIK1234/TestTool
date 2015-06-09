package amtt.epam.com.amtt.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import amtt.epam.com.amtt.R;

/**
 @author Iryna Monchanka
 @version on 5/20/2015
 */

public class AutocompleteProgressView extends RelativeLayout {

    private AutoCompleteTextView mACTextView;
    private ProgressBar mProgress;

    public AutocompleteProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.autocomplete_progress_layout, this, true);
        initViews();
    }

    private void initViews() {
        mACTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_text_view);
        mProgress = (ProgressBar) findViewById(R.id.progress);
    }

    public void showProgress(boolean enabled) {
        if (enabled) {
            mProgress.setVisibility(View.VISIBLE);
        } else {
            mProgress.setVisibility(View.GONE);
        }
    }

    public void setAdapter(ArrayAdapter<String> adapter) {
        mACTextView.setAdapter(adapter);
    }

    public void setThreshold(int threshold) {
        mACTextView.setThreshold(threshold);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        mACTextView.addTextChangedListener(watcher);
    }

    public void showDropDown() {
        mACTextView.showDropDown();
    }

    public Editable getText(){
        return mACTextView.getText();
    }
}