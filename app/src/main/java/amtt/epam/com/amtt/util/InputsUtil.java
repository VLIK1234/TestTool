package amtt.epam.com.amtt.util;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.R;

/**
 * @author Iryna Monchanka
 * @version on 6/5/2015
 */

public class InputsUtil {

    private static final String TAG = InputsUtil.class.getSimpleName();
    private static Pattern mPattern;
    private static Matcher mMatcher;
    private static final String mNoWhitespaceAfterAndBefore = "(\\S)+.+(\\S)";
    private static final String mHaveWhitespaces = ".*(\\s)+.*";
    private static final String mHasAtSymbol = ".*@.*";

    private static final Validator sEmptyValidator;
    private static final Validator sWhitespacesValidator;
    private static final Validator sEndStartWhitespacesValidator;
    private static final Validator sNoEmailValidator;
    private static final Validator sCorrectUrlValidator;
    private static final Validator sEpamUrlValidator;

    static {
        sEmptyValidator = new Validator() {
            @Override
            public String getMessage(CharSequence viewHint) {
                return AmttApplication.getContext().getString(R.string.enter_prefix, viewHint.toString().toLowerCase());
            }

            @Override
            public boolean validate(TextEditable editable) {
                Editable editableText = editable.getText();
                if (editableText == null) {
                    return true;
                }
                String text = editableText.toString();
                String urlPrefix = AmttApplication.getContext().getString(R.string.url_prefix);
                if (text.equals(urlPrefix)) {
                    return text.length() == urlPrefix.length();
                }
                return TextUtils.isEmpty(text);
            }
        };
        sWhitespacesValidator = new Validator() {
            @Override
            public String getMessage(CharSequence viewHint) {
                return viewHint.toString() + AmttApplication.getContext().getString(R.string.label_no_whitespaces);
            }

            @Override
            public boolean validate(TextEditable editable) {
                return hasWhitespaces(editable);
            }
        };
        sEndStartWhitespacesValidator = new Validator() {
            @Override
            public String getMessage(CharSequence viewHint) {
                return viewHint.toString() + AmttApplication.getContext().getString(R.string.label_no_whitespace_margins);
            }

            @Override
            public boolean validate(TextEditable editable) {
                return hasEndStartWhitespaces(editable);
            }
        };
        sNoEmailValidator = new Validator() {
            @Override
            public String getMessage(CharSequence viewHint) {
                return AmttApplication.getContext().getString(R.string.enter_prefix, AmttApplication.getContext().getString(R.string.label_no_email));
            }

            @Override
            public boolean validate(TextEditable editable) {
                return isEmail(editable);
            }
        };
        sCorrectUrlValidator = new Validator() {
            @Override
            public String getMessage(CharSequence viewHint) {
                return AmttApplication.getContext().getString(R.string.enter_prefix, viewHint.toString().toLowerCase()) + AmttApplication.getContext().getString(R.string.label_no_email);
            }

            @Override
            public boolean validate(TextEditable editable) {
                return checkUrl(editable);
            }
        };
        sEpamUrlValidator = new Validator() {
            @Override
            public String getMessage(CharSequence viewHint) {
                return AmttApplication.getContext().getString(R.string.enter_prefix, AmttApplication.getContext().getString(R.string.enter_postfix_jira));
            }

            @Override
            public boolean validate(TextEditable editable) {
                return AmttApplication.getContext().getString(R.string.epam_url).equals(editable.getText().toString());
            }
        };
    }

    public static Validator getEmptyValidator() {
        return sEmptyValidator;
    }

    public static Validator getWhitespacesValidator() {
        return sWhitespacesValidator;
    }

    public static Validator getEndStartWhitespacesValidator() {
        return sEndStartWhitespacesValidator;
    }

    public static Validator getNoEmailValidator() {
        return sNoEmailValidator;
    }

    public static Validator getCorrectUrlValidator() {
        return sCorrectUrlValidator;
    }

    public static Validator getEpamUrlValidator() {
        return sEpamUrlValidator;
    }

    private static Boolean checkUrl(TextEditable editable) {
        Editable editableText = editable.getText();
        if (editableText == null) {
            return false;
        }
        String url = editable.getText().toString();
        mPattern = Patterns.WEB_URL;
        mMatcher = mPattern.matcher(url.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? url + ": passed." : url + ": not passed.");
        return !mMatcher.matches();
    }

    private static Boolean hasEndStartWhitespaces(TextEditable editable) {
        Editable editableText = editable.getText();
        if (editableText == null) {
            return false;
        }
        String string = editableText.toString();
        mPattern = Pattern.compile(mNoWhitespaceAfterAndBefore);
        mMatcher = mPattern.matcher(string.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? string + ": passed." : string + ": not passed.");
        return !mMatcher.matches();
    }

    private static Boolean isEmail(TextEditable editable) {
        Editable editableText = editable.getText();
        if (editableText == null) {
            return false;
        }
        String string = editableText.toString();
        mPattern = Pattern.compile(mHasAtSymbol);
        mMatcher = mPattern.matcher(string.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? string + ": passed." : string + ": not passed.");
        return mMatcher.matches();
    }

    private static Boolean hasWhitespaces(TextEditable editable) {
        Editable editableText = editable.getText();
        if (editableText == null) {
            return false;
        }
        String string = editableText.toString();
        mPattern = Pattern.compile(mHaveWhitespaces);
        mMatcher = mPattern.matcher(string.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? string + ": passed." : string + ": not passed.");
        return mMatcher.matches();
    }

}
