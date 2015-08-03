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


public class ViewPagerActivity extends FragmentActivity {

    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        /*
      The pager adapter, which provides the pages to the view pager widget.
     */
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(supportFragmentManager);
        mPager.setAdapter(pagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private static final List<WeakReference<Fragment>> fragList = new ArrayList<WeakReference<Fragment>>();

    @Override
    public void onAttachFragment(Fragment fragment) {
//        FragmentInfoManger.INSTANSE.setInfoArrayList(this,fragment);
        fragList.add(new WeakReference(fragment));
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
        final Fragment[] arrayFragmnet = new Fragment[]{
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
