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

    public static CharSequence getFormatLog(ArrayList<CharSequence> listLines) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (CharSequence line : listLines) {
            if (line.toString().contains("E/")) {
                builder.append(Html.fromHtml("<font color='#FF0000'>" + line + "</font>"));
//                SpannableUtil.appendCompact(builder, line, new ForegroundColorSpan(AmttApplication.getContext().getResources().getColor(android.R.color.holo_red_light)), SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append("\n");
            } else if (line.toString().contains("F/")) {
                final int lengthBefore = builder.length();
                builder.append(Html.fromHtml("<font color='#FF0000'>" + line + "</font>"));
//                SpannableUtil.appendCompact(builder, line, new ForegroundColorSpan(AmttApplication.getContext().getResources().getColor(android.R.color.holo_red_light)), SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new QuoteSpan(), lengthBefore, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append("\n");
            }  else if (line.toString().contains("W/")) {
                builder.append(Html.fromHtml("<font color='#FFD700'>" + line + "</font>"));
//                SpannableUtil.appendCompact(builder, line, new ForegroundColorSpan(AmttApplication.getContext().getResources().getColor(android.R.color.holo_orange_light)), SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append("\n");
            } else if (line.toString().contains("I/")) {
                builder.append(Html.fromHtml("<font color='#9ACD32'>" + line + "</font>"));
//                SpannableUtil.appendCompact(builder, line, new ForegroundColorSpan(Color.GREEN), SPAN_EXCLUSIVE_EXCLUSIVE);
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
