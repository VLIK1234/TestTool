package amtt.epam.com.amtt.util;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import amtt.epam.com.amtt.CoreApplication;
import amtt.epam.com.amtt.R;
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
        FileUtil.taskName = taskName.replaceAll("[ ]+","_")+ getCurrentTimeInFormat();
    }

    public static String getCurrentTimeInFormat(){
        long currentTime = System.currentTimeMillis();
        return "_" + new SimpleDateFormat(TASK_NAME_DATETIME_FORMAT).format(new Date(currentTime));
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
        return filePath.endsWith(MimeType.TEXT_PLAIN.getFileExtension())||filePath.endsWith(MimeType.TEXT_HTML.getFileExtension());
    }

    public static boolean deleteListFile(List<String> listFile){
        boolean resultDelete = false;
        for (String file:listFile) {
            resultDelete = deleteRecursive(file);
        }
        return resultDelete;
    }

    public static boolean deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        return fileOrDirectory.delete();
    }

    public static boolean deleteRecursive(String fileOrFolderPath) {
        File fileOrDirectory = new File(fileOrFolderPath);
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        return fileOrDirectory.delete();
    }

    public static void copyFile(String inputPath, String inputFileName, String outputPath) {
        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + "/" + inputFileName);
            out = new FileOutputStream(outputPath +"/" + inputFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException e) {
            Log.e("FileUtil.copyFile", e.getMessage());
        }
        catch (Exception e) {
            Log.e("FileUtil.copyFile", e.getMessage());
        }

    }

    public static String getUsersCacheDir(){
        String amttCacheDir = Environment.getExternalStorageDirectory().toString() + slash
                + LOCAL_CACHE_DIRECTORY + slash;
        createFolder(amttCacheDir);

        String userName = ActiveUser.getInstance().getUserName() != null
                ? ActiveUser.getInstance().getUserName() : "non_auth";
        String userCacheDir = amttCacheDir + userName + slash;
        createFolder(userCacheDir);
        return userCacheDir;
    }

    public static String getCacheLocalDir() {
        String projectCacheDir = FileUtil.getUsersCacheDir() + PreferenceUtil.getString(CoreApplication.getContext().getString(R.string.key_test_project_entry))+slash;
        createFolder(projectCacheDir);

        String taskDir = projectCacheDir + taskName + slash;
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

    public static File[] sortArray(final File[] inputFiles) {
        if (inputFiles != null) {
            ArrayList<File> sortFilesAndDirs = new ArrayList<>();
            ArrayList<File> dirsArrayList = new ArrayList<>();
            ArrayList<File> filesArrayList = new ArrayList<>();

            for (File file : inputFiles) {
                if (file.isDirectory()) {
                    dirsArrayList.add(file);
                } else {
                    filesArrayList.add(file);
                }
            }
            Collections.sort(filesArrayList);
            Collections.sort(dirsArrayList);

            sortFilesAndDirs.addAll(dirsArrayList);
            sortFilesAndDirs.addAll(filesArrayList);

            File[] outSortFileArray = new File[sortFilesAndDirs.size()];
            for (int i = 0; i < outSortFileArray.length; i++) {
                outSortFileArray[i] = sortFilesAndDirs.get(i);
            }
            return outSortFileArray;
        } else {
            return null;
        }

    }

    public static ArrayList<File> getListWithDirFiles(File parentDir) {
        ArrayList<File> outputFilesList = new ArrayList<>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                outputFilesList.add(file);
                outputFilesList.addAll(getListWithDirFiles(file));
            } else {
                outputFilesList.add(file);
            }
        }
        return outputFilesList;
    }

    public static ArrayList<File> getListFilePaths(File parentDir) {
        ArrayList<File> outputFilesList = new ArrayList<>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                outputFilesList.addAll(getListWithDirFiles(file));
            } else {
                outputFilesList.add(file);
            }
        }
        return outputFilesList;
    }

    public static void cleanAllUsersArtifacts() {
        ArrayList<File> listFiles = getListWithDirFiles(new File(getUsersCacheDir()));
        for (File filePath : listFiles) {
            deleteRecursive(filePath);
        }
    }

    public static void writeFile(String filePath, String str) {
        final File stepsDescriptionFile = new File(filePath);
        FileWriter f;
        try {
            f = new FileWriter(stepsDescriptionFile);
            f.write(str);
            f.flush();
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
