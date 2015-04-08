package amtt.epam.com.amtt.crash;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Artsiom_Kaliaha on 30.03.2015.
 */
public class CrashInfoSavingTask extends AsyncTask<Void, Void, Void> {

    private final String mPath;
    private final String mData;

    public CrashInfoSavingTask(String path, String data) {
        mPath = path;
        mData = data;
    }

    @Override
    protected Void doInBackground(Void... params) {
        File file = new File(mPath);

        if (file.exists()) {
            file.delete();
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(mData);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            //TODO why?
            //ignored
        }
        return null;
    }
}
