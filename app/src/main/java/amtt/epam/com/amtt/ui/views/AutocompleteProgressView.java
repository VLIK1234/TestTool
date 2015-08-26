package amtt.epam.com.amtt.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.TextEditable;
import amtt.epam.com.amtt.util.Validatable;
import amtt.epam.com.amtt.util.Validator;

/**
 * @author Iryna Monchanka
 * @version on 5/20/2015
 */

public class AutocompleteProgressView extends RelativeLayout implements TextEditable, Validatable {

    private AutoCompleteTextView mACTextView;
    private ProgressBar mProgress;
    private List<Validator> mValidators;
    private CharSequence mHint;

    public AutocompleteProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.autocomplete_progress_layout, this, true);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        mACTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_text_view);
        mProgress = (ProgressBar) findViewById(R.id.progress);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, android.support.design.R.styleable.TextInputLayout, 0, android.support.design.R.style.Widget_Design_TextInputLayout);
        mHint = typedArray.getText(android.support.design.R.styleable.TextInputLayout_android_hint);
        typedArray.recycle();
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

    public void dismissDropDown() {
        mACTextView.dismissDropDown();
    }

    public void setText(CharSequence text) {
        mACTextView.setText(text, TextView.BufferType.NORMAL);

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
        mACTextView.setOnItemClickListener(l);
    }

    @Override
    public Editable getText() {
        return mACTextView.getText();
    }

    @Override
    public void setValidators(List<Validator> validators) {
        mValidators = validators;
    }

    @Override
    public boolean validate() {
        if (mValidators != null) {
            for (Validator validator : mValidators) {
                if (validator.validate(this)) {
                    Toast.makeText(getContext(), validator.getMessage(mHint), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

}
