package amtt.epam.com.amtt.ui.activities;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.contentprovider.LocalUri;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.api.loadcontent.JiraContent;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.googleapi.api.loadcontent.GSpreadsheetContent;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.InputsUtil;
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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ACCOUNTS_ACTIVITY_REQUEST_CODE = 1;
    private static final int SINGLE_USER_CURSOR_LOADER_ID = 2;
    private final String TAG = this.getClass().getSimpleName();
    private final ActiveUser mUser = ActiveUser.getInstance();
    private final JiraContent mJira = JiraContent.getInstance();
    private final GSpreadsheetContent mSpreadsheetContent = GSpreadsheetContent.getInstance();

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
            loader = new CursorLoader(MainActivity.this, LocalUri.USER.get(), null, null, null, null);
        } else if (id == SINGLE_USER_CURSOR_LOADER_ID) {
            loader = new CursorLoader(MainActivity.this,
                LocalUri.USER.get(),
                null,
                UsersTable._ID + "=?",
                new String[]{String.valueOf(args.getLong(AccountsActivity.KEY_USER_ID))},
                null);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try {
            switch (loader.getId()) {
                case CURSOR_LOADER_ID:
                    showLoginActivity();
//                    if (data == null || data.getCount() < 1) {
//                        showLoginActivity();
//                    } else if (data.getCount() > 1) {
//                        showAccountsActivity();
//                    } else {
//                        setActiveUser(data);
//                    }
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
        mUser.clearActiveUser();
        JUserInfo userInfo = new JUserInfo(data);
        mUser.setUrl(userInfo.getUrl());
        mUser.setCredentials(userInfo.getCredentials());
        mUser.setId(userInfo.getId());
        mUser.setUserName(userInfo.getName());
        mUser.setLastProjectKey(userInfo.getLastProjectKey());
        mUser.setLastAssigneeName(userInfo.getLastAssigneeName());
        mUser.setLastComponentsIds(userInfo.getLastComponentsIds());
        mUser.setSpreadsheetLink(userInfo.getLastSpreadsheetUrl());
        Logger.d(TAG, "ID " + userInfo.getId());
        Logger.d(TAG, "LastProjectKey " + userInfo.getLastProjectKey());
        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            public void run() {
                mJira.getPrioritiesNames(mUser.getUrl(), new GetContentCallback<HashMap<String, String>>() {
                    @Override
                    public void resultOfDataLoading(HashMap<String, String> result) {
                        if (result != null) {
                            Logger.d(TAG, "Loading priority finish");
                        }
                    }
                });
                mJira.getProjectsNames(mUser.getId(), new GetContentCallback<HashMap<JProjects, String>>() {
                    @Override
                    public void resultOfDataLoading(HashMap<JProjects, String> result) {
                        if (result != null) {
                            Logger.d(TAG, "Loading projects finish");
                        }
                    }
                });
                if (!InputsUtil.isEmpty(mUser.getSpreadsheetLink())) {
                    mSpreadsheetContent.getAllTestCases(mUser.getSpreadsheetLink(), new GetContentCallback<List<GEntryWorksheet>>() {
                        @Override
                        public void resultOfDataLoading(List<GEntryWorksheet> result) {
                            if (result != null) {
                                Logger.d(TAG, "Loading testcases finish");
                            } else {
                                Logger.d(TAG, "Loading testcases error");
                            }
                        }
                    });
                }
            }
        };
        worker.schedule(task, 2, TimeUnit.SECONDS);
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

    private void showAccountsActivity() {
        startActivityForResult(new Intent(MainActivity.this, AccountsActivity.class), ACCOUNTS_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACCOUNTS_ACTIVITY_REQUEST_CODE:
                    if (data != null) {
                        Bundle args = new Bundle();
                        long selectedUserId = data.getLongExtra(AccountsActivity.KEY_USER_ID, 0);
                        args.putLong(AccountsActivity.KEY_USER_ID, selectedUserId);
                        getLoaderManager().restartLoader(SINGLE_USER_CURSOR_LOADER_ID, args, this);
                    }
                    else{
                        showLoginActivity();
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            finish();
        }
    }
}