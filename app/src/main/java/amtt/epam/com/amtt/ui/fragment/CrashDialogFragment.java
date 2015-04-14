package amtt.epam.com.amtt.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.io.File;

import amtt.epam.com.amtt.R;

/**
 * Created by Artsiom_Kaliaha on 14.04.2015.
 */
public class CrashDialogFragment extends DialogFragment {

    public static final String KEY_PATH = "key_path";
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_CONTENT = "key_content";

    public CrashDialogFragment() {
    }

    public static CrashDialogFragment newInstance(String title, String message, String path) {
        CrashDialogFragment fragment = new CrashDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putString(KEY_CONTENT, message);
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
                        File crashFile = new File(path);
                        crashFile.delete();
                        dismiss();
                    }
                });

        return builder.create();
    }
}
