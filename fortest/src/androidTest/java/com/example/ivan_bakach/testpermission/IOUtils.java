package com.example.ivan_bakach.testpermission;

import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOUtils {

    private static final String TAG = IOUtils.class.getSimpleName();

    public static void close(Closeable... closeablesArray) {
        for (Closeable closeable : closeablesArray) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    public static FileOutputStream openFileOutput(String path, boolean isExists) throws FileNotFoundException {
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(path, isExists);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            outputStream = new FileOutputStream(path, false);
        }
        return outputStream;
    }
}
