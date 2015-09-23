package amtt.epam.com.amtt.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.ui.fragments.BrowserFilesFragment;
import amtt.epam.com.amtt.util.FileUtil;

/**
 * @author IvanBakach
 * @version on 22.09.2015
 */
public class ShareFileActivity extends BaseActivity implements BrowserFilesFragment.IOpenFolder{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_file);
        if (savedInstanceState == null) {
            addBrowserFilesFragment(FileUtil.getUsersCacheDir());
        }
    }

    private void addBrowserFilesFragment(String folderPath) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        BrowserFilesFragment fragment = BrowserFilesFragment.newInstance(folderPath, this);
        fragmentTransaction.replace(R.id.fl_folders_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void openFolder(String folderPath) {
        addBrowserFilesFragment(folderPath);
    }
}
