package amtt.epam.com.amtt.util;

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

    public static Boolean checkUrl(String url) {
        String sPreDomen = "[a-z][[a-z|0-9]\u005F\u002D]*[a-z|0-9]";
        String sPostDomen = "([a-z]){2,4}";
        mPattern = Pattern.compile("https\u003A\u002F\u002F" + sPreDomen + "\u002E" + sPreDomen + "\u002E" + sPostDomen);
        mMatcher = mPattern.matcher(url.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? url + ": passed." : url + ": not passed.");
        return !mMatcher.matches();
    }

    public static Boolean checkToWhitespaceEnds(String string) {
        //check To Whitespace After And Before
        mPattern = Pattern.compile("(\\S)+.+(\\S)");
        mMatcher = mPattern.matcher(string.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? string + ": passed." : string + ": not passed.");
        return !mMatcher.matches();
    }

    public static Boolean checkToAt(String string) {
        //check To Whitespace After And Before
        mPattern = Pattern.compile(".+"+"@"+".+");
        mMatcher = mPattern.matcher(string.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? string + ": passed." : string + ": not passed.");
        return mMatcher.matches();
    }

    public static Boolean haveWhitespaces(String string) {
        //check To Whitespace After And Before
        mPattern = Pattern.compile(".*(\\s)+.*");
        mMatcher = mPattern.matcher(string.toLowerCase());
        Logger.d(TAG, mMatcher.matches() ? string + ": passed." : string + ": not passed.");
        return mMatcher.matches();
    }

}
