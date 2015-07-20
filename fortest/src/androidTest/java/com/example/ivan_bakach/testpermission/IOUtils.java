package com.example.ivan_bakach.testpermission;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Artsiom_Kaliaha on 13.04.2015.
 */
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
}
