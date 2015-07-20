package com.example.ivan_bakach.testpermission.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

//import com.example.ivan_bakach.injectlibrary.FragmentInfoManger;
import com.example.ivan_bakach.testpermission.fragment.FirstPageFragment;
import com.example.ivan_bakach.testpermission.R;
import com.example.ivan_bakach.testpermission.fragment.SecondPageFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class PermissionTestClientActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    public FragmentManager supportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        supportFragmentManager = getSupportFragmentManager();
        mPagerAdapter = new ScreenSlidePagerAdapter(supportFragmentManager);
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static List<WeakReference<Fragment>> fragList = new ArrayList<WeakReference<Fragment>>();
    int i = 0;
    @Override
    public void onAttachFragment(Fragment fragment) {
//        FragmentInfoManger.INSTANSE.setInfoArrayList(this,fragment);
        fragList.add(new WeakReference(fragment));
    }

    public static List<Fragment> getActiveFragments() {
        ArrayList<Fragment> ret = new ArrayList<Fragment>();
        for(WeakReference<Fragment> ref : fragList) {
            Fragment f = ref.get();
            if(f != null) {
                if(f.isVisible()) {
                    ret.add(f);
                }
            }
        }
        return ret;
    }

    public static String printListFragment(List<Fragment> list) {
        String result = PermissionTestClientActivity.class.getSimpleName()+ " ";
        for (Fragment fragment:list) {
            result+=(fragment.getClass().getSimpleName()+" ");
        }
        return result;
    }
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        Fragment[] arrayFragmnet = new Fragment[]{
            new FirstPageFragment(), new SecondPageFragment()
        };
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return arrayFragmnet[position];
        }

        @Override
        public int getCount() {
            return arrayFragmnet.length;
        }
    }
}
