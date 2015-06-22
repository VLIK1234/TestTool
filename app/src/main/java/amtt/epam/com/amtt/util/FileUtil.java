package amtt.epam.com.amtt.util;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by Ivan_Bakach on 10.06.2015.
 */
public class FileUtil {

    public static String getFileName(@NonNull String filePath){
        int slash = filePath.lastIndexOf(File.pathSeparatorChar);
        return filePath.substring(slash);
    }
}
