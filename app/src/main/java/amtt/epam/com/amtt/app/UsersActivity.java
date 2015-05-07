package amtt.epam.com.amtt.app;

import android.app.FragmentManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.UserAdapter;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.fragment.LoginFragment;
import amtt.epam.com.amtt.fragment.LoginFragment.FragmentLoginCallback;
import amtt.epam.com.amtt.fragment.UserFragment;
import amtt.epam.com.amtt.service.TopButtonService;

public class UsersActivity extends BaseActivity
        implements FragmentLoginCallback, LoaderCallbacks<Cursor> {

    private static final String TAG_LOGIN_FRAGMENT = "tag_login_fragment";
    private static final String TAG_USER_LIST_FRAGMENT = "tag_user_list_fragment";
    private static final String TAG_USER_FRAGMENT = "tag_user_fragment";
    static final int CURSOR_LOADER_ID = 0;
    static final int NO_FLAGS = 0;

    private FragmentManager mFragmentManager;
    private UserAdapter mAdapter;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mFragmentManager = getFragmentManager();
        initViews();
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, UsersActivity.this);
        mHandler = new Handler();
    }

    private void initViews() {
        ListView listView = (ListView) findViewById(android.R.id.list);
        mAdapter = new UserAdapter(this, null, NO_FLAGS);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFragmentManager.beginTransaction().replace(R.id.container, UserFragment.getInstance(id), TAG_USER_FRAGMENT).addToBackStack(null).commit();
                TopButtonService.authSuccess(UsersActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_users, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new_user) {
            LoginFragment loginFragment = (LoginFragment) mFragmentManager.findFragmentByTag(TAG_LOGIN_FRAGMENT);
            if (loginFragment == null) {
                mFragmentManager.beginTransaction().replace(R.id.container, new LoginFragment(), TAG_LOGIN_FRAGMENT).addToBackStack(null).commit();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStack();
        } else {
            finish();
        }
    }

    //Callbacks
    //Login fragment
    @Override
    public void onUserLoggedIn() {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, UsersActivity.this);
    }

    //Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, AmttContentProvider.USER_CONTENT_URI, UsersTable.PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() > 0) {
            mAdapter.changeCursor(data);
        } else {
            //such a construction with handler and runnable caused by IllegalStateException
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mFragmentManager.beginTransaction().replace(R.id.container, new LoginFragment(), TAG_LOGIN_FRAGMENT).addToBackStack(null).commit();
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
