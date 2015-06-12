package amtt.epam.com.amtt.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.Constants;

/**
 * Created by Artsiom_Kaliaha on 12.06.2015.
 */
public class TextInput extends FrameLayout {

    private EditText mEditText;
    private TextInputLayout mTextInputLayout;

    private int mInputType;
    private int mInitialTextLength;
    private String mDefaultErrorText;
    private boolean isErrorTookPlace;

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

        mEditText = new EditText(context);
        try {
            String initialText = typedArray.getString(R.styleable.TextInput_text);
            mInitialTextLength = initialText.length();
            mDefaultErrorText = typedArray.getString(R.styleable.TextInput_defaultErrorText);
            mEditText.setText(initialText);
            mEditText.setHint(typedArray.getString(R.styleable.TextInput_hint));
            mEditText.setInputType(mInputType = typedArray.getInt(R.styleable.TextInput_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL));
        } finally {
            typedArray.recycle();
        }

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (isErrorTookPlace && !hasFocus && isEditTextEmpty()) {
                    mTextInputLayout.setError(mDefaultErrorText);
                } else if (isErrorTookPlace) {
                    mTextInputLayout.setError(Constants.Symbols.EMPTY);
                }
            }
        });
        mTextInputLayout = new TextInputLayout(context);
        mTextInputLayout.setErrorEnabled(true);
        addView(mEditText, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mTextInputLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setError(CharSequence error) {
        switch (mInputType) {
            case EditorInfo.TYPE_TEXT_VARIATION_NORMAL:

                break;
            case EditorInfo.TYPE_TEXT_VARIATION_URI:

                break;
            case EditorInfo.TYPE_TEXT_VARIATION_PASSWORD:

                break;
        }
        mTextInputLayout.setError(error);
    }

    private boolean isEditTextEmpty() {
        if (mInitialTextLength != 0) {
            return mEditText.getText().length() <= mInitialTextLength;
        } else {
            return TextUtils.isEmpty(mEditText.getText());
        }
    }

}
