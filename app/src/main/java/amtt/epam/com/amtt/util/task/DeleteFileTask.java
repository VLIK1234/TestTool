package amtt.epam.com.amtt.util.task;

import android.os.AsyncTask;

import java.io.File;

/**
 * Created by Artsiom_Kaliaha on 14.04.2015.
 */
public class DeleteFileTask extends AsyncTask<Void,Void,Boolean> {

    private final String mFilePath;
    private final DeletionCallback mCallback;

    public DeleteFileTask(String filePath, DeletionCallback callback) {
        mFilePath = filePath;
        mCallback = callback;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return new File(mFilePath).delete();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mCallback.onDeletionDone(aBoolean);
    }

}
