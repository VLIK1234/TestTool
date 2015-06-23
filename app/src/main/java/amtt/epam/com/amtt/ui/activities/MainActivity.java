package amtt.epam.com.amtt.ui.activities;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.api.loadcontent.JiraContent;
import amtt.epam.com.amtt.api.JiraGetContentCallback;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.Logger;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int AMTT_ACTIVITY_REQUEST_CODE = 1;
    private static final int SINGLE_USER_CURSOR_LOADER_ID = 2;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        PreferenceManager.setDefaultValues(this, R.xml.setting, false);
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, MainActivity.this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if (id == CURSOR_LOADER_ID) {
            loader = new CursorLoader(MainActivity.this, AmttUri.USER.get(), null, null, null, null);
        } else if (id == SINGLE_USER_CURSOR_LOADER_ID) {
            loader = new CursorLoader(MainActivity.this,
                AmttUri.USER.get(),
                null,
                UsersTable._ID + "=?",
                new String[]{String.valueOf(args.getLong(AmttActivity.KEY_USER_ID))},
                null);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try {
            switch (loader.getId()) {
                case CURSOR_LOADER_ID:
                    if (data != null) {
                        if (data.getCount() > 1) {
                            showAmttActivity();
                        } else if (data.getCount() == 1) {
                            setActiveUser(data);
                        }
                        else { Logger.d(TAG, String.valueOf(data.getCount()));
                            showLoginActivity();}
                    } else {
                        showLoginActivity();
                    }
                    break;
                case SINGLE_USER_CURSOR_LOADER_ID:
                    setActiveUser(data);
                    break;
            }
        } finally {
            IOUtils.close(data);
        }
    }

    private void setActiveUser(Cursor data) {
        JUserInfo userInfo = new JUserInfo(data);
        ActiveUser.getInstance().setUrl(userInfo.getUrl());
        ActiveUser.getInstance().setCredentials(userInfo.getCredentials());
        ActiveUser.getInstance().setId(userInfo.getId());
        ActiveUser.getInstance().setUserName(userInfo.getName());
        ActiveUser.getInstance().setLastProjectKey(userInfo.getLastProjectKey());
        Logger.e(TAG, "ID " + userInfo.getId());
        Logger.e(TAG, "LastProjectKey " + userInfo.getLastProjectKey());
        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            public void run() {
                JiraContent.getInstance().getPrioritiesNames(new JiraGetContentCallback<HashMap<String, String>>() {
                    @Override
                    public void resultOfDataLoading(HashMap<String, String> result) {
                        if (result != null) {
                            Logger.d(TAG, "Loading priority finish");
                        }
                    }
                });
                JiraContent.getInstance().getProjectsNames(new JiraGetContentCallback<HashMap<JProjects, String>>() {
                    @Override
                    public void resultOfDataLoading(HashMap<JProjects, String> result) {
                        if (result != null) {
                            Logger.d(TAG, "Loading projects finish");
                        }
                    }
                });
            }
        };
        worker.schedule(task, 1, TimeUnit.SECONDS);
        TopButtonService.start(this);
        finish();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Logger.d(TAG, "onLoaderReset");
    }

    private void showLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void showAmttActivity() {
        startActivityForResult(new Intent(MainActivity.this, AmttActivity.class), AMTT_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AMTT_ACTIVITY_REQUEST_CODE:
                    if (data != null) {
                        Bundle args = new Bundle();
                        long selectedUserId = data.getLongExtra(AmttActivity.KEY_USER_ID, 0);
                        args.putLong(AmttActivity.KEY_USER_ID, selectedUserId);
                        getLoaderManager().restartLoader(SINGLE_USER_CURSOR_LOADER_ID, args, this);
                    }
                    else{
                        showLoginActivity();
                    }
                    break;
            }
        }
    }
}