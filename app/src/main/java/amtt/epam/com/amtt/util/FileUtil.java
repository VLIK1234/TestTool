package amtt.epam.com.amtt.util;

import android.support.annotation.Nullable;

import java.io.File;

/**
 * Created by Ivan_Bakach on 10.06.2015.
 */
public class FileUtil {

    public static String getFileName(@Nullable String filePath) {
        if (filePath==null) {
            return "not attached";
        }
        int slash = filePath.lastIndexOf(File.pathSeparatorChar);
        return filePath.substring(slash);
    }
}
