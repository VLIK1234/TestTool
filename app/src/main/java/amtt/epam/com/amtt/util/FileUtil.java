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

}
