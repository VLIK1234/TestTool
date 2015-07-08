package amtt.epam.com.amtt.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Artsiom_Kaliaha on 13.04.2015.
 */
public class IOUtils {

    private static final String CLASS_NAME = IOUtils.class.getSimpleName();

    public static void close(Closeable... closeablesArray) {
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

}
