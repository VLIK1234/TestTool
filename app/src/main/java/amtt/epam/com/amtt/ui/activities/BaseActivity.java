package amtt.epam.com.amtt.ui.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import amtt.epam.com.amtt.util.Logger;

/**
 * @author Ivan_Bakach
 * @version on 26.03.2015
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    static final int CURSOR_LOADER_ID = 0;
    static final int NO_FLAGS = 0;
    private InputMethodManager mInputManager;

    public void showProgress(boolean show) {
        View progressBar = findViewById(android.R.id.progress);
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void hideKeyboard(Window window) {
        View view = window.getCurrentFocus();
        if (view != null) {
            mInputManager = (InputMethodManager) getBaseContext().getSystemService(INPUT_METHOD_SERVICE);
            mInputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public FileOutputStream openFileOutput(String name, int mode) {
        FileOutputStream outputStream = null;
        try {
            outputStream = super.openFileOutput(name, mode);
        } catch (FileNotFoundException e) {
            Logger.e(TAG, e.getMessage());
            File file = new File(getFilesDir() + "/" + Uri.parse(name).getLastPathSegment());
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
