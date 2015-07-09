package amtt.epam.com.amtt.util;

import android.content.Context;
import android.net.Uri;

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

    public static FileOutputStream openFileOutput(Context context, String name) {
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            Logger.e(TAG, e.getMessage());
            File file = new File(context.getFilesDir() + "/" + Uri.parse(name).getLastPathSegment());
            try {
                if (file.createNewFile()) {
                    outputStream = new FileOutputStream(file);
                }
            } catch (IOException internalException) {
                Logger.e(TAG, internalException.getMessage());
            }
        }
        return outputStream;
    }

}
