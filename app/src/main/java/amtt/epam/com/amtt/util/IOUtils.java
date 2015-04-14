package amtt.epam.com.amtt.util;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;

import amtt.epam.com.amtt.R;

/**
 * Created by Artsiom_Kaliaha on 13.04.2015.
 */
public class IOUtils {

    private static final String CLASS_NAME = IOUtils.class.getSimpleName();

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Logger.e(CLASS_NAME, e.getMessage());
            }
        }
    }

    public static String[] loadCrashDialogData(String filePath) throws IOException {
        String[] loadedStrings = new String[2];

        BufferedReader bufferedReader = null;
        String crashHeadText = null;
        StringBuilder crashText = new StringBuilder();
        String buffer;
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            crashHeadText = bufferedReader.readLine();
            while ((buffer = bufferedReader.readLine()) != null) {
                crashText.append(buffer);
            }
        } finally {
            if (bufferedReader != null) {
                IOUtils.close(bufferedReader);
            }
        }
        loadedStrings[0] = crashHeadText;
        loadedStrings[1] = crashText.toString();

        return loadedStrings;
    }

}
