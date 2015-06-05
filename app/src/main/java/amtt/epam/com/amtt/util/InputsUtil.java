package amtt.epam.com.amtt.util;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final String mHasAtSymbol = ".+@.+";

    public static Boolean checkUrl(String url) {
        mPattern = Patterns.WEB_URL;
        mMatcher = mPattern.matcher(url.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? url + ": passed." : url + ": not passed.");
        return !mMatcher.matches();
    }

    public static Boolean hasWhitespaceMargins(String string) {
        //check To Whitespace After And Before
        mPattern = Pattern.compile(mNoWhitespaceAfterAndBefore);
        mMatcher = mPattern.matcher(string.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? string + ": passed." : string + ": not passed.");
        return !mMatcher.matches();
    }

    public static Boolean hasAtSymbol(String string) {
        mPattern = Pattern.compile(mHasAtSymbol);
        mMatcher = mPattern.matcher(string.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? string + ": passed." : string + ": not passed.");
        return mMatcher.matches();
    }

    public static Boolean haveWhitespaces(String string) {
        mPattern = Pattern.compile(mHaveWhitespaces);
        mMatcher = mPattern.matcher(string.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? string + ": passed." : string + ": not passed.");
        return mMatcher.matches();
    }

}
