package amtt.epam.com.amtt.util;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Artsiom_Kaliaha on 13.04.2015.
 */
public class IOUtils {

    private static final String CLASS_NAME = IOUtils.class.getSimpleName();

    public static void close(@NonNull Closeable... closeablesArray) {
        for (Closeable closeable : closeablesArray) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    Logger.e(CLASS_NAME, e.getMessage());
                }
            }
        }
    }

    public static String getExtension(String path) {
        int extensionStartIndex = path.lastIndexOf('.');
        return path.substring(extensionStartIndex + 1, path.length());
    }

    public static FileOutputStream openFileOutput(String path) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            try {
                File file = new File(path);
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException repeatedException) {
                Logger.e(CLASS_NAME, repeatedException.getMessage());
            }
        }
        return outputStream;
    }

    public static void destroyProcesses(@NonNull Process... processArray) {
        for (Process process : processArray) {
            if (process != null) {
                process.destroy();
            }
        }
    }

    /**
     * Method for retrieving string data from internal storage
     */
    public static String loadStringFromInternalStorage(String filePath) throws IOException {
        BufferedReader bufferedReader = null;
        StringBuilder crashText = new StringBuilder();
        String buffer;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            while ((buffer = bufferedReader.readLine()) != null) {
                crashText.append(buffer);
            }
        } finally {
            IOUtils.close(bufferedReader);
        }
        return crashText.toString();
    }

}
