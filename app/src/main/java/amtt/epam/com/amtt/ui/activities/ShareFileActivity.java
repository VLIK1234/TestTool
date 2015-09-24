package amtt.epam.com.amtt.ui.activities;

import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.LinkedList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.ui.fragments.BrowserFilesFragment;

/**
 * @author IvanBakach
 * @version on 22.09.2015
 */
public class ShareFileActivity extends BaseActivity implements BrowserFilesFragment.IOpenFolder{
    LinkedList<String> mFolderPaths = new LinkedList<>();
    private ScreenSlidePagerAdapter mPagerAdapter;
    private ViewPager mPager;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_file);
        mPager = (ViewPager) findViewById(R.id.vp_folders_layout);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(mFolderPaths.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mPager.addOnPageChangeListener(mOnPageChangeListener);
//        addBrowserFilesFragment(FileUtil.getUsersCacheDir());
        addBrowserFilesFragment(Environment.getExternalStorageDirectory().getPath());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPager.removeOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    public void onBackPressed() {
        if(mPager.getCurrentItem()!=0) {
            mPager.setCurrentItem(mPager.getCurrentItem()-1, true);
        } else {
            super.onBackPressed(); // This will pop the Activity from the stack.
        }
    }

    private void addBrowserFilesFragment(String folderPath) {
        int addIndex = mFolderPaths.size()<1?mPager.getCurrentItem():mPager.getCurrentItem() + 1;
        mFolderPaths.add(addIndex, folderPath);
        mPagerAdapter.notifyDataSetChanged();
        //remove all item after addIndex if addIndex isn't last
        mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
        if (mPager.getCurrentItem()+1<=mFolderPaths.size()-1) {
            for (int i = mPager.getCurrentItem()+1; i < mFolderPaths.size();i++) {
                mFolderPaths.remove(i);
            }
        }
        mPagerAdapter.notifyDataSetChanged();
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
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public BrowserFilesFragment getItem(int position) {
            return BrowserFilesFragment.newInstance(mFolderPaths.get(position), ShareFileActivity.this);
        }

        @Override
        public int getCount() {
            return mFolderPaths.size();
        }
    }
}
