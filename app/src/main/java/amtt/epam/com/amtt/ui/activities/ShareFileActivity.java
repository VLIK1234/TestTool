package amtt.epam.com.amtt.ui.activities;

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
    ArrayList<BrowserFilesFragment> mFilesFragments = new ArrayList<>();
    private ScreenSlidePagerAdapter mPagerAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_file);
        mPager = (ViewPager) findViewById(R.id.vp_folders_layout);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        if (savedInstanceState == null) {
            addBrowserFilesFragment(FileUtil.getUsersCacheDir());
        }
    }

    private void addBrowserFilesFragment(String folderPath) {
        BrowserFilesFragment fragment = BrowserFilesFragment.newInstance(folderPath, this);
        int indexExistFragment = searchExistFragment(fragment);
        mFilesFragments.add(fragment);
        mPagerAdapter.getItem(mFilesFragments.size() - 1);
        mPagerAdapter.notifyDataSetChanged();
//        if (searchExistFragment(fragment) == -1) {
//        } else {
//            mFilesFragments.add(indexExistFragment, fragment);
//            mFilesFragments.remove(indexExistFragment + 1);
//            mPagerAdapter.getItem(indexExistFragment);
//            mPagerAdapter.notifyDataSetChanged();
//        }
    }

    public int searchExistFragment(BrowserFilesFragment filesFragment){
        for (int i = 0; i < mFilesFragments.size();i++) {
            if (mFilesFragments.get(i).getFolderPath().equals(filesFragment.getFolderPath())) {
                return i;
            }
        }
        return -1;
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
            return mFilesFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFilesFragments.size();
        }
    }
}
