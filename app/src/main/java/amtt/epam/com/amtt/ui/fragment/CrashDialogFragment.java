package amtt.epam.com.amtt.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;

import amtt.epam.com.amtt.R;

/**
 * Created by Artsiom_Kaliaha on 14.04.2015.
 */
public class CrashDialogFragment extends DialogFragment {

    class DeleteFileTask extends AsyncTask<Void, Void, Boolean> {

        private final String mFilePath;

        public DeleteFileTask(String filePath) {
            mFilePath = filePath;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return new File(mFilePath).delete();
        }

        @Override
        protected void onPostExecute(Boolean isDeleted) {
            Toast.makeText(getActivity(), isDeleted ? getString(R.string.file_deleted) : getString(R.string.file_not_deleted), Toast.LENGTH_SHORT).show();
        }

    }

    public static final String KEY_PATH = "key_path";
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_CONTENT = "key_content";

    public CrashDialogFragment() {
    }

    public static CrashDialogFragment newInstance(String rawString, String path) {
        CrashDialogFragment fragment = new CrashDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, rawString.substring(0, 19));
        bundle.putString(KEY_CONTENT, rawString.substring(19));
        bundle.putString(KEY_PATH, path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        final String path = arguments.getString(KEY_PATH);
        String title = arguments.getString(KEY_TITLE);
        String message = arguments.getString(KEY_CONTENT);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.close_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setNeutralButton(R.string.delete_crash_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteFileTask(path).execute();
                    }
                });

        return builder.create();
    }

}
