package amtt.epam.com.amtt.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.internal.util.Predicate;

import java.util.Map;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.Constants;

/**
 * Created by Artsiom_Kaliaha on 12.06.2015.
 */
public class TextInput extends TextInputLayout {

    private EditText mText;
    private CharSequence mLastErrorText;
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

        mText = new EditText(context, attrs);
        mText.setHint(null);
        mText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (count == 1 && after == 0) {
                    setError(mLastErrorText);
                } else {
                    setError(Constants.Symbols.EMPTY);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addView(mText, new ViewGroup.LayoutParams(context, attrs));
    }

    public void setValidationMap(Map<Predicate<EditText>, CharSequence> validationMap) {
        mValidationMap = validationMap;
    }

    /*
    * Returns false if validation is not passed, otherwise returns true
    * */
    public boolean validate() {
        for (Map.Entry<Predicate<EditText>, CharSequence> pair : mValidationMap.entrySet()) {
            if (pair.getKey().apply(mText)) {
                if (mLastErrorText != pair.getValue()) {
                    setError(mLastErrorText = pair.getValue());
                }
                return false;
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
