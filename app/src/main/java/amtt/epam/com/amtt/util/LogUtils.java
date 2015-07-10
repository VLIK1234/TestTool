package amtt.epam.com.amtt.util;

import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.QuoteSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import java.util.ArrayList;

import amtt.epam.com.amtt.AmttApplication;

/**
 * Created by Ivan_Bakach on 08.07.2015.
 */
public class LogUtils {

    public static final int SPAN_EXCLUSIVE_EXCLUSIVE = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

    public static Spannable getFormatLog(ArrayList<String> listLines) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        ForegroundColorSpan errorColor = new ForegroundColorSpan(AmttApplication.getContext().getResources().getColor(android.R.color.holo_red_light));
        ForegroundColorSpan warningColor = new ForegroundColorSpan(AmttApplication.getContext().getResources().getColor(android.R.color.holo_orange_light));
        ForegroundColorSpan infoColor = new ForegroundColorSpan(Color.GREEN);
        for (String line : listLines) {
            if (line.contains("E/")) {
                SpannableUtil.appendCompact(builder, line, new ForegroundColorSpan(AmttApplication.getContext().getResources().getColor(android.R.color.holo_red_light)), SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append("\n");
            } else if (line.contains("F/")) {
                final int lengthBefore = builder.length();
                SpannableUtil.appendCompact(builder, line, new ForegroundColorSpan(AmttApplication.getContext().getResources().getColor(android.R.color.holo_red_light)), SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new QuoteSpan(), lengthBefore, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append("\n");
            }  else if (line.contains("W/")) {
                SpannableUtil.appendCompact(builder, line, new ForegroundColorSpan(AmttApplication.getContext().getResources().getColor(android.R.color.holo_orange_light)), SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append("\n");
            } else if (line.contains("I/")) {
                SpannableUtil.appendCompact(builder, line, infoColor, SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append("\n");
            }  else {
                builder.append(line);
                builder.append("\n");
            }
        }
        return builder;
    }
    public static String getTextLog(ArrayList<String> listLines){
        StringBuilder builder = new StringBuilder();
        for (String line : listLines) {
            builder.append(line).append("\n");
        }
        return builder.toString();
    }
}
