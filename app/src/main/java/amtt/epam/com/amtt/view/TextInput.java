package amtt.epam.com.amtt.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.android.internal.util.Predicate;

import java.util.Map;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.InputsUtil;

/**
 * Created by Artsiom_Kaliaha on 12.06.2015.
 */
public class TextInput extends FrameLayout {

    private EditText mEditText;
    private TextInputLayout mTextInputLayout;

    private int mInitialTextLength;
    private String mDefaultErrorText;
    private boolean isErrorTookPlace;

    private Map<Predicate<EditText>,CharSequence> mValidationMap;

    public TextInput(Context context) {
        super(context);
        initContent(context, null, 0, 0);
    }

    public TextInput(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextInput(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TextInput(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        initContent(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initContent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TextInput, defStyleAttr, defStyleRes);

        mEditText = new EditText(context, attrs);
        try {
//            String initialText = typedArray.getString(android.R.attr.text);
//            mInitialTextLength = initialText.length();
            mDefaultErrorText = getContext().getString(R.string.enter_prefix) + typedArray.getString(R.styleable.TextInput_defaultErrorText);
//            mEditText.setText(initialText);
        } finally {
            typedArray.recycle();
        }
        this.setFocusableInTouchMode(true);
        mEditText.setFocusableInTouchMode(true);
//        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (isErrorTookPlace && !hasFocus && isEditTextEmpty()) {
//                    mTextInputLayout.setError(mDefaultErrorText);
//                } else if (isErrorTookPlace) {
//                    mTextInputLayout.setError(Constants.Symbols.EMPTY);
//                }
//            }
//        });
        mTextInputLayout = new TextInputLayout(context);
        mTextInputLayout.addView(mEditText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTextInputLayout.setErrorEnabled(true);
        addView(mTextInputLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextInput.this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEditText.requestFocus();
            }
        });
    }

    public void setValidationMap(Map<Predicate<EditText>,CharSequence> validationMap) {
        mValidationMap = validationMap;
    }

    public void validate() {
        isErrorTookPlace = false;
        for (Map.Entry<Predicate<EditText>,CharSequence> pair : mValidationMap.entrySet()) {
            if (pair.getKey().apply(mEditText)) {
                mTextInputLayout.setError(pair.getValue());
                isErrorTookPlace = true;
                break;
            }
        }
    }

    public Editable getText() {
        return mEditText.getText();
    }

    public void setText(CharSequence text) {
        mEditText.setText(text);
    }

    private boolean isEditTextEmpty() {
        if (mInitialTextLength != 0) {
            return mEditText.getText().length() <= mInitialTextLength;
        } else {
            return TextUtils.isEmpty(mEditText.getText());
        }
    }

}
