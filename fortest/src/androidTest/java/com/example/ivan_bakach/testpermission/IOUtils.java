package com.example.ivan_bakach.testpermission;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOUtils {

    public static FileOutputStream openFileOutput(String path) throws FileNotFoundException {
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(path, new File(path).exists());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            outputStream = new FileOutputStream(path, false);
        }
        return outputStream;
    }

    public static void deleteFileIfExist(String filePath) {
        File myFile = new File(filePath);
        if (myFile.exists()) {
            myFile.delete();
        }
    }

    public static byte[] toByteArray(File file) throws IOException {
        FileInputStream fileInputStream;
        byte[] byteArray = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(byteArray);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
