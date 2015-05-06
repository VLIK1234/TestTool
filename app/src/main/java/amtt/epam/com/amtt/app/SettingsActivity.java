package amtt.epam.com.amtt.app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.task.DataBaseCallback;
import amtt.epam.com.amtt.database.task.DataBaseOperationType;
import amtt.epam.com.amtt.database.task.DataBaseResponse;
import amtt.epam.com.amtt.database.task.DataBaseTask;
import amtt.epam.com.amtt.database.task.DataBaseTaskResult;
import amtt.epam.com.amtt.fragment.LoginFragment.FragmentLoginCallback;
import amtt.epam.com.amtt.fragment.UserFragment;
import amtt.epam.com.amtt.fragment.UserListFragment.ListFragmentUserCallback;
import amtt.epam.com.amtt.fragment.LoginFragment;
import amtt.epam.com.amtt.fragment.UserListFragment;

public class SettingsActivity extends ActionBarActivity implements DataBaseCallback<Boolean>, FragmentLoginCallback, ListFragmentUserCallback {

    private static final String TAG_LOGIN_FRAGMENT = "tag_login_fragment";
    private static final String TAG_USER_LIST_FRAGMENT = "tag_user_list_fragment";
    private static final String TAG_USER_FRAGMENT = "tag_user_fragment";

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mFragmentManager = getFragmentManager();
        checkUsersInDB();
    }

    private void checkUsersInDB() {
        new DataBaseTask.Builder()
                .setContext(this)
                .setCallback(this)
                .setOperationType(DataBaseOperationType.CHECK_USERS_AVAILABILITY)
                .createAndExecute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_new_qa) {
            mFragmentManager.beginTransaction().replace(R.id.container, new LoginFragment(), TAG_LOGIN_FRAGMENT).addToBackStack(null).commit();
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
    @Override
    public void onDataBaseActionDone(DataBaseResponse<Boolean> dataBaseResponse) {
        if (dataBaseResponse.getTaskResult() == DataBaseTaskResult.DONE) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (dataBaseResponse.getValueResult()) {
                transaction.replace(R.id.container, new UserListFragment(), TAG_USER_LIST_FRAGMENT);
            } else {
                transaction.replace(R.id.container, new LoginFragment(), TAG_LOGIN_FRAGMENT);
            }
            transaction.commit();
        }
    }

    @Override
    public void onUserLoggedIn() {
        mFragmentManager.beginTransaction().replace(R.id.container, new UserListFragment(), TAG_USER_LIST_FRAGMENT).commit();
    }

    @Override
    public void onListItemClick(long id) {
        mFragmentManager.beginTransaction().replace(R.id.container, UserFragment.getInstance(id), TAG_USER_FRAGMENT).addToBackStack(null).commit();
    }
}
