package amtt.epam.com.amtt.util;

import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static Boolean hasAtSymbol(EditText editText) {
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
