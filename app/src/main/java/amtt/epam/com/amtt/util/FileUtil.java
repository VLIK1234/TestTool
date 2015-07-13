package amtt.epam.com.amtt.util;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Ivan_Bakach
 * @version on 10.06.2015
 */

public  class FileUtil {

    public static String getFileName(@Nullable String filePath) {
        if (filePath == null) {
            return Constants.Symbols.EMPTY;
        }
        int slashIndex = filePath.lastIndexOf(File.separatorChar);
        if (slashIndex == -1) {
            return Constants.Symbols.EMPTY;
        }
        if (slashIndex + 1 < filePath.length()) {
            return filePath.substring(slashIndex + 1);
        } else {
            return Constants.Symbols.EMPTY;
        }
    }

    public static String getExtension(String path) {
        int extensionStartIndex = path.lastIndexOf('.');
        return path.substring(extensionStartIndex, path.length());
    }

    public static boolean isPicture(String filePath) {
        return filePath.endsWith(".png") || filePath.endsWith(".jpg") || filePath.endsWith(".jpeg");
    }

    public static boolean isText(String filePath) {
        return filePath.endsWith(".txt");
    }
}
