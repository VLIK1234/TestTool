package com.epam.amtt.test;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOUtils {

    private static final String TAG = IOUtils.class.getSimpleName();

    public static FileOutputStream openFileOutput(String path) throws FileNotFoundException {
        if (path != null) {
            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream(path, new File(path).exists());
            } catch (FileNotFoundException e) {
                Log.e(TAG, e.getMessage(), e);
                outputStream = new FileOutputStream(path, false);
            }
            return outputStream;
        } else {
            return null;
        }
    }

    public static void deleteFileIfExist(String filePath) {
        File myFile = new File(filePath);
        if (myFile.exists()) {
            myFile.delete();
        }
    }

    public static byte[] toByteArray(File file) throws IOException {
        FileInputStream fileInputStream = null;
        byte[] byteArray = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(byteArray);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
        return byteArray;
    }

    public static File[] getAllFileInFolder(File folder){
        return folder.listFiles();
    }

    public static boolean deleteFiles(File... listFile){
        boolean delete = false;
        File[] allAttachFile = listFile;
        if (allAttachFile!=null) {
            for (File file : allAttachFile) {
                if (file.exists()) {
                    delete = file.delete();
                }
            }
        }
        return delete;
    }
}
