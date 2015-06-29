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
            return "";
        }
        int slashIndex = filePath.lastIndexOf(File.separatorChar);
        if (slashIndex == -1) {
            return "";
        }
        if (slashIndex + 1 < filePath.length()) {
            return filePath.substring(slashIndex + 1);
        } else {
            return "";
        }
    }

    public static String getExtension(String path) {
        int extensionStartIndex = path.lastIndexOf('.');
        return path.substring(extensionStartIndex + 1, path.length());
    }

}
