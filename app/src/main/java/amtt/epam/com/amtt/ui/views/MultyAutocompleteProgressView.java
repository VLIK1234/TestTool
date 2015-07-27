package amtt.epam.com.amtt.ui.views;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import amtt.epam.com.amtt.R;

/**
 * @author Iryna Monchanka
 * @version on 7/27/2015
 */
public class MultyAutocompleteProgressView extends RelativeLayout {

    private MultiAutoCompleteTextView mMACTextView;
    private ProgressBar mProgress;

    public MultyAutocompleteProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.multyautocomplete_progress_layout, this, true);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        mMACTextView = (MultiAutoCompleteTextView) findViewById(R.id.multyautocomplete_text_view);
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
        mMACTextView.setAdapter(adapter);
    }

    public void setThreshold(int threshold) {
        mMACTextView.setThreshold(threshold);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        mMACTextView.addTextChangedListener(watcher);
    }

    public void showDropDown() {
        mMACTextView.showDropDown();
    }

    public void setText(CharSequence text) {
        mMACTextView.setText(text, TextView.BufferType.NORMAL);
    }

    public void setTokenizer(MultiAutoCompleteTextView.Tokenizer t) {
        mMACTextView.setTokenizer(t);
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener l) {
        mMACTextView.setOnItemSelectedListener(l);
    }
}
