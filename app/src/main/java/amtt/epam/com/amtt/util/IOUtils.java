package amtt.epam.com.amtt.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;

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
