package amtt.epam.com.amtt.ui.activities;


import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.WeakReference;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.loadcontent.JiraContent;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.contentprovider.LocalUri;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.exception.ExceptionType;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.DialogUtils;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Artsiom_Kaliaha
 * @version on 07.05.2015
 */
@SuppressWarnings("unchecked")
public class UserInfoActivity extends BaseActivity implements LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = this.getClass().getSimpleName();
    private static final int MESSAGE_REFRESH = 100;
    private static final int ACCOUNTS_ACTIVITY_REQUEST_CODE = 1;
    private static final int LOGIN_ACTIVITY_REQUEST_CODE = 2;
    private static final int SINGLE_USER_CURSOR_LOADER_ID = 2;
    private TextView mNameTextView;
    private TextView mEmailAddressTextView;
    private TextView mDisplayNameTextView;
    private TextView mTimeZoneTextView;
    private TextView mLocaleTextView;
    private TextView mJiraUrlTextView;
    private TextView mSpreadsheetUrlTextView;
    private ImageView mUserImageImageView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private UserInfoHandler mHandler;
    private final ActiveUser mUser = ActiveUser.getInstance();
    private final JiraContent mJira = JiraContent.getInstance();
    private final JiraApi mJiraApi = JiraApi.getInstance();

    public static class UserInfoHandler extends Handler {

        private final WeakReference<UserInfoActivity> mActivity;

        UserInfoHandler(UserInfoActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UserInfoActivity service = mActivity.get();
            service.refreshUserInfo();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_user_info);
        initViews();
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, UserInfoActivity.this);
        mHandler = new UserInfoHandler(UserInfoActivity.this);
        mSwipeRefreshLayout.setOnRefreshListener(UserInfoActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add: {
                TopButtonService.closeService(getBaseContext());
                startActivityForResult(new Intent(UserInfoActivity.this, LoginActivity.class), LOGIN_ACTIVITY_REQUEST_CODE);
                return true;
            }
            case R.id.action_list: {
                startActivityForResult(new Intent(UserInfoActivity.this, AccountsActivity.class), ACCOUNTS_ACTIVITY_REQUEST_CODE);
                return true;
            }
            case android.R.id.home: {
                TopButtonService.start(this);
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        mHandler.removeMessages(MESSAGE_REFRESH);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_REFRESH), 750);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ACCOUNTS_ACTIVITY_REQUEST_CODE) {
            if (data != null) {
                Bundle args = new Bundle();
                long selectedUserId = data.getLongExtra(AccountsActivity.KEY_USER_ID, 0);
                args.putLong(AccountsActivity.KEY_USER_ID, selectedUserId);
                getLoaderManager().restartLoader(SINGLE_USER_CURSOR_LOADER_ID, args, UserInfoActivity.this);
            } else {
                startActivityForResult(new Intent(UserInfoActivity.this, LoginActivity.class), LOGIN_ACTIVITY_REQUEST_CODE);
            }
        } else if (resultCode == RESULT_CANCELED && requestCode == LOGIN_ACTIVITY_REQUEST_CODE) {
            getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, UserInfoActivity.this);
        }
    }

    private void initViews() {
        mNameTextView = (TextView) findViewById(R.id.tv_user_name);
        mEmailAddressTextView = (TextView) findViewById(R.id.tv_email);
        mDisplayNameTextView = (TextView) findViewById(R.id.tv_fullname);
        mTimeZoneTextView = (TextView) findViewById(R.id.tv_time_zone);
        mLocaleTextView = (TextView) findViewById(R.id.tv_locale);
        mJiraUrlTextView = (TextView) findViewById(R.id.tv_jira_url);
        mSpreadsheetUrlTextView = (TextView) findViewById(R.id.tv_spreadsheet_url);
        mUserImageImageView = (ImageView) findViewById(R.id.tv_avatar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
    }

    private void setActiveUser(JUserInfo user) {
        mUser.clearActiveUser();
        mUser.setUrl(user.getUrl());
        mUser.setCredentials(user.getCredentials());
        mUser.setId(user.getId());
        mUser.setUserName(user.getName());
        mUser.setLastProjectKey(user.getLastProjectKey());
        mUser.setLastAssigneeName(user.getLastAssigneeName());
        mUser.setLastComponentsIds(user.getLastComponentsIds());
        mUser.setSpreadsheetLink(user.getLastSpreadsheetUrl());
        mNameTextView.setText(user.getName());
        mEmailAddressTextView.setText(user.getEmailAddress());
        mDisplayNameTextView.setText(user.getDisplayName());
        mTimeZoneTextView.setText(user.getTimeZone());
        mLocaleTextView.setText(user.getLocale());
        mJiraUrlTextView.setText(user.getUrl());
        if (mSpreadsheetUrlTextView!=null && user.getLastSpreadsheetUrl() != null) {
            mSpreadsheetUrlTextView.setText(user.getLastSpreadsheetUrl());
        }
        ImageLoader.getInstance().displayImage(user.getAvatarUrls().getAvatarUrl(), mUserImageImageView);
        mJira.clearData();
    }

    private void refreshUserInfo() {
        String requestSuffix = JiraApiConst.USER_INFO_PATH + mUser.getUserName();
        mJiraApi.searchData(requestSuffix, new UserInfoProcessor(), new Callback<JUserInfo>() {
            @Override
            public void onLoadStart() {
            }

            @Override
            public void onLoadExecuted(JUserInfo user) {
                user.setUrl(mUser.getUrl());
                setActiveUser(user);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onLoadError(Exception e) {
                Logger.e(TAG, e.getMessage(), e);
                DialogUtils.createDialog(UserInfoActivity.this, ExceptionType.valueOf(e)).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        String selection = UsersTable._ID + "=?";
        if (id == CURSOR_LOADER_ID) {
            loader = new CursorLoader(UserInfoActivity.this, LocalUri.USER.get(), null, selection,
                    new String[]{String.valueOf(mUser.getId())}, null);
        } else if (id == SINGLE_USER_CURSOR_LOADER_ID) {
            loader = new CursorLoader(UserInfoActivity.this, LocalUri.USER.get(), null, selection,
                    new String[]{String.valueOf(args.getLong(AccountsActivity.KEY_USER_ID))}, null);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try {
            if (loader.getId() == CURSOR_LOADER_ID || loader.getId() == SINGLE_USER_CURSOR_LOADER_ID) {
                if (data != null && data.getCount() > 0) {
                    reloadData(data);
                }
            }
        } finally {
            IOUtils.close(data);
        }
    }

    private void reloadData(Cursor data) {
        JUserInfo userInfo = new JUserInfo(data);
        setActiveUser(userInfo);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

}
