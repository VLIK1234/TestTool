package amtt.epam.com.amtt.util;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import amtt.epam.com.amtt.http.MimeType;
/**
 * @author Ivan_Bakach
 * @version on 10.06.2015
 */

public class FileUtil {

    private static final String LOCAL_CACHE_DIRECTORY = "amtt_cache";
    private static char slash = File.separatorChar;
    private static String taskName = "default";
    private static final String TASK_NAME_DATETIME_FORMAT = "dd-MM-HH-mm";

    public static void setTaskName(String taskName) {
        long imageTime = System.currentTimeMillis();
        String taskNameDate = "_" + new SimpleDateFormat(TASK_NAME_DATETIME_FORMAT).format(new Date(imageTime));
        FileUtil.taskName = taskName.replaceAll("[ ]+","_")+ taskNameDate;
    }

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
        return filePath.endsWith(MimeType.TEXT_PLAIN.getFileExtension());
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

    public static String getCacheLocalDir() {
        String amttCacheDir = Environment.getExternalStorageDirectory().toString() + slash
                + LOCAL_CACHE_DIRECTORY + slash;
        createFolder(amttCacheDir);

        String userCacheDir = Environment.getExternalStorageDirectory().toString() + slash
                + LOCAL_CACHE_DIRECTORY + slash + ActiveUser.getInstance().getUserName()+ slash;
        createFolder(userCacheDir);

        String taskDir = Environment.getExternalStorageDirectory().toString() + slash
                + LOCAL_CACHE_DIRECTORY + slash + ActiveUser.getInstance().getUserName()+ slash + taskName + slash;
        createFolder(taskDir);
        return taskDir;
    }

    public static void createFolder(String folderPath){
        File folder = new File(folderPath);
        if (!folder.canWrite()) {
            if (folder.mkdir()) {
                Log.d(FileUtil.class.getSimpleName(), "Was created " + folder.getName() + " folder");
            } else {
                Log.d(FileUtil.class.getSimpleName(), "Failed created " + folder.getName() + " folder");
            }
        }
    }
}
