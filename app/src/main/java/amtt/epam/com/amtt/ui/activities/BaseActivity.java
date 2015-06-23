package amtt.epam.com.amtt.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 @author Ivan_Bakach
 @version on 26.03.2015
 */

public class BaseActivity extends AppCompatActivity {

    static final int CURSOR_LOADER_ID = 0;
    static final int NO_FLAGS = 0;

    public void showProgress(boolean show) {
        View progressBar = findViewById(android.R.id.progress);
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

}
