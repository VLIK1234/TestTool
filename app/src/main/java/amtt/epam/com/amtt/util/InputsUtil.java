package amtt.epam.com.amtt.util;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import com.android.internal.util.Predicate;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.view.AutocompleteProgressView;

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

    private static final Predicate<EditText> sPredicateIsEmpty;
    private static final Predicate<EditText> sPredicateHasWhitespaces;
    private static final Predicate<EditText> sPredicateHasWhitespaceMargins;
    private static final Predicate<EditText> sPredicateNoEmail;
    private static final Predicate<EditText> sPredicateIsCorrectUrl;
    private static final Predicate<EditText> sPredicateIsEpamUrl;

    private static final Map<Predicate<EditText>, String> sErrorMessagesMap;
    
    static {
        sPredicateIsEmpty = new Predicate<EditText>() {
            @Override
            public boolean apply(EditText editText) {
                return TextUtils.isEmpty(editText.getText().toString());
            }
        };
        sPredicateHasWhitespaces = new Predicate<EditText>() {
            @Override
            public boolean apply(EditText editText) {
                return InputsUtil.hasWhitespaces(editText);
            }
        };
        sPredicateHasWhitespaceMargins = new Predicate<EditText>() {
            @Override
            public boolean apply(EditText editText) {
                return InputsUtil.hasWhitespaceMargins(editText);
            }
        };
        sPredicateNoEmail = new Predicate<EditText>() {
            @Override
            public boolean apply(EditText editText) {
                return InputsUtil.isEmail(editText);
            }
        };
        sPredicateIsCorrectUrl = new Predicate<EditText>() {
            @Override
            public boolean apply(EditText editText) {
                return InputsUtil.checkUrl(editText);
            }
        };
        sPredicateIsEpamUrl = new Predicate<EditText>() {
            @Override
            public boolean apply(EditText editText) {
                return ContextHolder.getContext().getString(R.string.epam_url).equals(editText.getText().toString());
            }
        };

        sErrorMessagesMap = new HashMap<>();
        sErrorMessagesMap.put(sPredicateIsEmpty, ContextHolder.getContext().getString(R.string.enter_prefix) + " ");
        sErrorMessagesMap.put(sPredicateIsEpamUrl, ContextHolder.getContext().getString(R.string.enter_prefix) + ContextHolder.getContext().getString(R.string.enter_postfix_jira));
        sErrorMessagesMap.put(sPredicateIsCorrectUrl, ContextHolder.getContext().getString(R.string.enter_prefix) + ContextHolder.getContext().getString(R.string.enter_correct_url));
        sErrorMessagesMap.put(sPredicateHasWhitespaces, ContextHolder.getContext().getString(R.string.label_no_whitespaces));
        sErrorMessagesMap.put(sPredicateHasWhitespaceMargins, ContextHolder.getContext().getString(R.string.label_no_whitespace_margins));
        sErrorMessagesMap.put(sPredicateNoEmail, ContextHolder.getContext().getString(R.string.label_no_email));
    }

    public static Predicate<EditText> getPredicateHasWhitespaces() {
        return sPredicateHasWhitespaces;
    }

    public static Predicate<EditText> getPredicateHasWhitespaceMargins() {
        return sPredicateHasWhitespaceMargins;
    }

    public static Predicate<EditText> getPredicateIsEmail() {
        return sPredicateNoEmail;
    }

    public static Predicate<EditText> getPredicateIsCorrectUrl() {
        return sPredicateIsCorrectUrl;
    }

    public static Predicate<EditText> getPredicateIsEpamUrl() {
        return sPredicateIsEpamUrl;
    }

    private static String getErrorMessage(Predicate<EditText> predicate) {
        return sErrorMessagesMap.get(predicate);
    }

    public static Boolean checkUrl(EditText editText) {
        String url = editText.getText().toString();
        mPattern = Patterns.WEB_URL;
        mMatcher = mPattern.matcher(url.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? url + ": passed." : url + ": not passed.");
        return !mMatcher.matches();
    }

    public static Boolean hasWhitespaceMargins(EditText editText) {
        //check To Whitespace After And Before
        String string = editText.getText().toString();
        mPattern = Pattern.compile(mNoWhitespaceAfterAndBefore);
        mMatcher = mPattern.matcher(string.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? string + ": passed." : string + ": not passed.");
        return !mMatcher.matches();
    }

    public static Boolean isEmail(EditText editText) {
        String string = editText.getText().toString();
        mPattern = Pattern.compile(mHasAtSymbol);
        mMatcher = mPattern.matcher(string.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? string + ": passed." : string + ": not passed.");
        return mMatcher.matches();
    }

    public static Boolean hasWhitespaces(EditText editText) {
        String string = editText.getText().toString();
        mPattern = Pattern.compile(mHaveWhitespaces);
        mMatcher = mPattern.matcher(string.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? string + ": passed." : string + ": not passed.");
        return mMatcher.matches();
    }

    public static Boolean hasWhitespaces(AutocompleteProgressView autocompleteProgressView) {
        String string = autocompleteProgressView.getText().toString();
        mPattern = Pattern.compile(mHaveWhitespaces);
        mMatcher = mPattern.matcher(string.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? string + ": passed." : string + ": not passed.");
        return mMatcher.matches();
    }

}
