package amtt.epam.com.amtt.util;

import android.text.SpannableStringBuilder;

/**
 * Created by Ivan_Bakach on 08.07.2015.
 */
public class SpannableUtil {
    public static SpannableStringBuilder appendCompact(SpannableStringBuilder spannableString, CharSequence text, Object what, int flags) {
        int start = spannableString.length();
        spannableString.append(text);
        spannableString.setSpan(what, start, spannableString.length(), flags);
        return spannableString;
    }
}
