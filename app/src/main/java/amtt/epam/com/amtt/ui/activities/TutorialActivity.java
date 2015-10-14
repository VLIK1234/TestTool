package amtt.epam.com.amtt.ui.activities;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.ui.fragments.BrowserFilesFragment;

/**
 * @author IvanBakach
 * @version on 14.10.2015
 */
public class TutorialActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        int[] drawablesArray = {R.drawable.ic_action_action_list};
    }

//    private class TutorialPagerAdapter extends FragmentStatePagerAdapter {
//        public TutorialPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }
//
//        @Override
//        public BrowserFilesFragment getItem(int position) {
//            return BrowserFilesFragment.newInstance(mFolderPaths.get(position), mSharedFilePaths, ShareFilesActivity.this);
//        }
//
//        @Override
//        public int getCount() {
//            return mFolderPaths.size();
//        }
//    }
}
