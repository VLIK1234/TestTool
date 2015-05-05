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
            mFragmentManager.beginTransaction().replace(R.id.container, new LoginFragment()).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Callbacks
    @Override
    public void onDataBaseActionDone(DataBaseResponse<Boolean> dataBaseResponse) {
        if (dataBaseResponse.getTaskResult() == DataBaseTaskResult.DONE) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (dataBaseResponse.getValueResult()) {
                transaction.add(R.id.container, new LoginFragment());
            } else {
                transaction.add(R.id.container, new UserListFragment());
            }
            transaction.commit();
        }
    }

    @Override
    public void onUserLoggedIn() {
        mFragmentManager.beginTransaction().replace(R.id.container, new UserListFragment()).commit();
    }

    @Override
    public void onListItemClick(long id) {
        mFragmentManager.beginTransaction().replace(R.id.container, UserFragment.getInstance(id)).commit();
    }
}
