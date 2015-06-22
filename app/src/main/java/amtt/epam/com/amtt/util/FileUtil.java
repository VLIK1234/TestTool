package amtt.epam.com.amtt.util;

import android.support.annotation.Nullable;

import java.io.File;

/**
 @author Ivan_Bakach
 @version on 10.06.2015
 */

public class FileUtil {

    public static String getFileName(@Nullable String filePath) {
        if (filePath == null) {
            return "not attached";
        }
        int slash = filePath.lastIndexOf(File.separatorChar);
        if (slash + 1 < filePath.length() - 1) {
            return filePath.substring(slash + 1);
        } else {
            return filePath.substring(slash);
        }
    }
}
