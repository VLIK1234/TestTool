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

/**
 * Created by Iryna_Monchanka on 5/20/2015.
 */
public class ACTextViewWithProgress extends RelativeLayout {

    private AutoCompleteTextView mACTextView;
    private ProgressBar mProgress;

    public ACTextViewWithProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.autocomplete_text_view_layout, this, true);
        initViews();
    }

    private void initViews() {
        mACTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_text_view);
        mProgress = (ProgressBar) findViewById(R.id.progress);
    }

    public void showProgress(boolean flag) {
        if (flag) {
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
}
