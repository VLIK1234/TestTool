package amtt.epam.com.amtt.view;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.internal.util.Predicate;

import java.util.Map;

/**
 * Created by Artsiom_Kaliaha on 12.06.2015.
 */
public class TextInput extends TextInputLayout {

    private EditText mET;

    private int mInitialTextLength;
    private String mDefaultErrorText;
    private boolean isErrorTookPlace;

    private Map<Predicate<EditText>, CharSequence> mValidationMap;

    public TextInput(Context context) {
        this(context, null);
    }

    public TextInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContent(context, attrs);
    }

    private void initContent(Context context, AttributeSet attrs) {
        mET = new EditText(context, attrs);
        addView(mET, new ViewGroup.LayoutParams(context, attrs));
    }

    public void setValidationMap(Map<Predicate<EditText>, CharSequence> validationMap) {
        mValidationMap = validationMap;
    }

    public void validate() {
        isErrorTookPlace = false;
        for (Map.Entry<Predicate<EditText>, CharSequence> pair : mValidationMap.entrySet()) {
            if (pair.getKey().apply(mET)) {
                //mTextInputLayout.setError(pair.getValue());
                isErrorTookPlace = true;
                break;
            }
        }
    }

    public Editable getText() {
        return mET.getText();
    }

    public void setText(CharSequence text) {
        mET.setText(text);
    }

    private boolean isEditTextEmpty() {
        if (mInitialTextLength != 0) {
            return mET.getText().length() <= mInitialTextLength;
        } else {
            return TextUtils.isEmpty(mET.getText());
        }
    }

}
