package amtt.epam.com.amtt.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.TextEditable;
import amtt.epam.com.amtt.util.ThemeUtil;
import amtt.epam.com.amtt.util.Validatable;
import amtt.epam.com.amtt.util.Validator;

/**
 * Created by Artsiom_Kaliaha on 12.06.2015.
 */
public class TextInput extends TextInputLayout implements TextEditable, Validatable {

    private EditText mText;
    private CharSequence mLastErrorText;
    private CharSequence mHint;
    private boolean isErrorShown;
    private List<Validator> mValidators;

    public TextInput(Context context) {
        this(context, null);
    }

    public TextInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContent(context, attrs);
    }

    private void initContent(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, android.support.design.R.styleable.TextInputLayout, 0, android.support.design.R.style.Widget_Design_TextInputLayout);
        mHint = a.getText(android.support.design.R.styleable.TextInputLayout_android_hint);
        a.recycle();

        setHint(mHint);
        setErrorEnabled(true);

        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_focused},
                new int[]{android.R.attr.state_focused, android.R.attr.state_enabled},
        };
        int[] colors = new int[]{
                ThemeUtil.colorControlNormal(context, 0xFF000000),
                ThemeUtil.colorControlActivated(context, 0xFF000000),
        };
        ColorStateList mDividerColors = new ColorStateList(states, colors);

        mText = new EditText(context, attrs);
        mText.setHint(null);
        mText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 1 && after == 0) {
                    setError(mLastErrorText);
                    isErrorShown = true;
                } else {
                    setError(Constants.Symbols.EMPTY);
                    isErrorShown = false;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Divider mDivider = new Divider(2, mText.getTotalPaddingLeft(), mText.getTotalPaddingRight(), mDividerColors, 0);
        mDivider.setInEditMode(isInEditMode());
        mDivider.setAnimEnable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mText.setBackground(mDivider);
        } else {
            mText.setBackgroundDrawable(mDivider);
        }
        addView(mText, new ViewGroup.LayoutParams(context, attrs));
    }

    public void setText(CharSequence text) {
        mText.setText(text);
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
                    if (!isErrorShown) {
                        setError(mLastErrorText = validator.getMessage(mHint));
                        isErrorShown = true;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Editable getText() {
        return mText.getText();
    }
}
