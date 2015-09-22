package amtt.epam.com.amtt.ui.activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.ui.fragments.BrowserFragment;

/**
 * @author IvanBakach
 * @version on 22.09.2015
 */
public class ShareFileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_file);
        if (savedInstanceState == null) {
            Fragment fragment = BrowserFragment.newInstance();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fl_folders_layout, fragment);
            fragmentTransaction.commit();
        }
    }
}
