package amtt.epam.com.amtt.util;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.util.List;

import amtt.epam.com.amtt.http.MimeType;
/**
 * @author Ivan_Bakach
 * @version on 10.06.2015
 */

public class FileUtil {

    private static final String AMTT_CACHE_DIRECTORY = "amtt_cache";
    private static String sCacheAmttDir = Environment.getExternalStorageDirectory().toString() + File.separatorChar + AMTT_CACHE_DIRECTORY + File.separatorChar;

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
        return filePath.endsWith(MimeType.IMAGE_PNG.getFileExtension()) ||
                filePath.endsWith(MimeType.IMAGE_JPG.getFileExtension()) ||
                filePath.endsWith(MimeType.IMAGE_JPEG.getFileExtension()) ||
                filePath.endsWith(MimeType.IMAGE_GIF.getFileExtension());
    }

    public static boolean isText(String filePath) {
        return filePath.endsWith(MimeType.TEXT_PLAIN.getFileExtension())||filePath.endsWith(MimeType.TEXT_HTML.getFileExtension());
    }

    public static boolean delete(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    public static boolean deleteListFile(List<String> listFile){
        boolean resultDelete = false;

        for (String file:listFile) {
            resultDelete = delete(file);
        }
        return resultDelete;
    }

    public static String getCacheAmttDir() {
        File amttCache = new File(sCacheAmttDir);
        if (!amttCache.canWrite()) {
            if (amttCache.mkdir()) {
                Log.d(FileUtil.class.getSimpleName(),"Was created "+amttCache.getName() +" folder" );
            }else{
                Log.d(FileUtil.class.getSimpleName(),"Failed created "+amttCache.getName() +" folder" );
            };
        }
        return sCacheAmttDir;
    }
}
