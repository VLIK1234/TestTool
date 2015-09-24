package amtt.epam.com.amtt.ui.activities;

import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.ui.fragments.BrowserFilesFragment;
import amtt.epam.com.amtt.util.FileUtil;

/**
 * @author IvanBakach
 * @version on 22.09.2015
 */
public class ShareFileActivity extends BaseActivity implements BrowserFilesFragment.IOpenFolder{
    ArrayList<String> mFilesFragments = new ArrayList<>();
    private ScreenSlidePagerAdapter mPagerAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_file);
        mPager = (ViewPager) findViewById(R.id.vp_folders_layout);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
//        addBrowserFilesFragment(FileUtil.getUsersCacheDir());
        addBrowserFilesFragment(Environment.getExternalStorageDirectory().getPath());
    }

    private void addBrowserFilesFragment(String folderPath) {
        mFilesFragments.add(folderPath);
        mPagerAdapter.notifyDataSetChanged();
        mPager.setCurrentItem(mPager.getChildCount() - 1, true);
    }

    @Override
    public void openFolder(String folderPath) {
        addBrowserFilesFragment(folderPath);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BrowserFilesFragment getItem(int position) {
            return BrowserFilesFragment.newInstance(mFilesFragments.get(position), ShareFileActivity.this);
        }

        @Override
        public int getCount() {
            return mFilesFragments.size();
        }
    }
}
