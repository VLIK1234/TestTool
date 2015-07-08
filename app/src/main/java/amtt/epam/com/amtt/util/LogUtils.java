package amtt.epam.com.amtt.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;

import amtt.epam.com.amtt.AmttApplication;

/**
 * Created by Ivan_Bakach on 08.07.2015.
 */
public class LogUtils {
    public static Spannable getFormatLog(ArrayList<String> listLines) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (String line : listLines) {
            if (line.contains("E/")) {
                SpannableUtil.appendCompact(builder, line, new ForegroundColorSpan(AmttApplication.getContext().getResources().getColor(android.R.color.holo_red_light)), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append("\n");
            } else if (line.contains("F/")) {
                SpannableUtil.appendCompact(builder, line, new ForegroundColorSpan(AmttApplication.getContext().getResources().getColor(android.R.color.holo_red_dark)), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append("\n");
            }  else if (line.contains("W/")) {
                SpannableUtil.appendCompact(builder, line, new ForegroundColorSpan(AmttApplication.getContext().getResources().getColor(android.R.color.holo_orange_light)), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append("\n");
            } else if (line.contains("I/")) {
                SpannableUtil.appendCompact(builder, line, new ForegroundColorSpan(Color.GREEN), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append("\n");
            } else if (line.contains("D/")) {
                SpannableUtil.appendCompact(builder, line, new ForegroundColorSpan(Color.BLACK), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append("\n");
            } else {
                builder.append(line);
                builder.append("\n");
            }
        }
        return builder;
    }
}
