package amtt.epam.com.amtt.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author Ivan_Bakach
 * @version on 26.03.2015
 */

public class BaseActivity extends AppCompatActivity {

    static final int CURSOR_LOADER_ID = 0;
    static final int NO_FLAGS = 0;
    private InputMethodManager mInputManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    public void showProgress(boolean show) {
        View progressBar = findViewById(android.R.id.progress);
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void hideKeyboard() {
        View view = getWindow().getDecorView();
        if (view != null) {
            mInputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showKeyboard(final View view) {
        if (view != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (view != null && mInputManager != null) {
                        view.requestFocus();
                        mInputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            }, 500);
        }
    }

}
