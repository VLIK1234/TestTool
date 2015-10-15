package amtt.epam.com.amtt.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.viewpagerindicator.CirclePageIndicator;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.ui.fragments.TutorialFragment;

/**
 * @author IvanBakach
 * @version on 14.10.2015
 */
public class TutorialActivity extends AppCompatActivity {

    private int[] mDrawablesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mDrawablesArray = new int[]{R.drawable.background_auth_method, R.drawable.ic_action_delete, R.drawable.ic_create_ticket};

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_folders_layout);
        TutorialPagerAdapter pagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        CirclePageIndicator titleIndicator = (CirclePageIndicator) findViewById(R.id.pi_view_pager_indicator);
        titleIndicator.setViewPager(viewPager);
        Button closeButton = (Button) findViewById(R.id.bt_got_it);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class TutorialPagerAdapter extends FragmentStatePagerAdapter {
        public TutorialPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public TutorialFragment getItem(int position) {
            return TutorialFragment.newInstance(mDrawablesArray[position]);
        }

        @Override
        public int getCount() {
            return mDrawablesArray.length;
        }
    }
}
