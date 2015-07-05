package amtt.epam.com.amtt.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

/**
 @author Ivan_Bakach
 @version on 26.03.2015
 */

public class BaseActivity extends AppCompatActivity {

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

    public void showKeyboard(View view){
        if (view != null) {
            mInputManager = (InputMethodManager) getBaseContext().getSystemService(INPUT_METHOD_SERVICE);
            mInputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

}
