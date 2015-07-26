package amtt.epam.com.amtt.util;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
                    Logger.e(TAG, e.getMessage());
                }
            }
        }
    }

    public static FileOutputStream openFileOutput(String path, boolean createIfNotExists) throws FileNotFoundException {
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            if (!createIfNotExists) {
                throw e;
            }
            File file = createNewFile(path);
            outputStream = new FileOutputStream(file);
        }

        return outputStream;
    }

    public static File createNewFile(String path) {
        return new File(path);
    }

    public static void byteArrayToFile(byte[] byteArray, File file){
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            bos.write(byteArray);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
