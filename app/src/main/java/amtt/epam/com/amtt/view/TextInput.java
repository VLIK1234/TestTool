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

import com.android.internal.util.Predicate;

import java.util.Map;

import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.ThemeUtil;

/**
 * Created by Artsiom_Kaliaha on 12.06.2015.
 */
public class TextInput extends TextInputLayout {

    private EditText mText;
    private CharSequence mLastErrorText;
    private boolean isErrorShown;
    private Map<Predicate<EditText>, CharSequence> mValidationMap;

    public TextInput(Context context) {
        this(context, null);
    }

    public TextInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContent(context, attrs);
    }

    private void initContent(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, android.support.design.R.styleable.TextInputLayout, 0, android.support.design.R.style.Widget_Design_TextInputLayout);
        CharSequence hint = a.getText(android.support.design.R.styleable.TextInputLayout_android_hint);
        a.recycle();

        setHint(hint);
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

    public void setValidationMap(Map<Predicate<EditText>, CharSequence> validationMap) {
        mValidationMap = validationMap;
    }

    /*
    * Returns false if validation is not passed, otherwise returns true
    * */
    public boolean validate() {
        if (mValidationMap != null) {
            for (Map.Entry<Predicate<EditText>, CharSequence> pair : mValidationMap.entrySet()) {
                if (pair.getKey().apply(mText)) {
                    if (!isErrorShown) {
                        setError(mLastErrorText = pair.getValue());
                        isErrorShown = true;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public Editable getText() {
        return mText.getText();
    }

    public void setText(CharSequence text) {
        mText.setText(text);
    }

}
