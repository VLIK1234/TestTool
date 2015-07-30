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
import android.view.View;
import android.widget.Button;
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
import amtt.epam.com.amtt.contentprovider.AmttUri;
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
public class UserInfoActivity extends BaseActivity implements Callback<JUserInfo>, LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    private final String TAG = this.getClass().getSimpleName();
    private static final int MESSAGE_REFRESH = 100;

    private static final int AMTT_ACTIVITY_REQUEST_CODE = 1;
    private static final int LOGIN_ACTIVITY_REQUEST_CODE = 2;

    private static final int SINGLE_USER_CURSOR_LOADER_ID = 2;
    private TextView mNameTextView;
    private TextView mEmailAddressTextView;
    private TextView mDisplayNameTextView;
    private TextView mTimeZoneTextView;
    private TextView mLocaleTextView;
    private TextView mJiraUrlTextView;
    private ImageView mUserImageImageView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private UserInfoHandler mHandler;
    private Boolean isNeedShowingTopButton = true;
    private Boolean isNewUser = false;
    private Button mAddSpreadsheetButton;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_user_info);
        initViews();
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, UserInfoActivity.this);
        mHandler = new UserInfoHandler(UserInfoActivity.this);
        mSwipeRefreshLayout.setOnRefreshListener(UserInfoActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isNeedShowingTopButton) {
            if (isNewUser) {
                TopButtonService.start(getBaseContext());
            } else {
                TopButtonService.sendActionChangeTopButtonVisibility(true);
            }
        }
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
                TopButtonService.close(getBaseContext());
                isNeedShowingTopButton = false;
                startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_ACTIVITY_REQUEST_CODE);
            }
            return true;
            case R.id.action_list: {
                startActivityForResult(new Intent(this, AmttActivity.class), AMTT_ACTIVITY_REQUEST_CODE);
            }
            return true;
            case android.R.id.home: {
                TopButtonService.start(this);
                finish();
            }
            return true;
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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AMTT_ACTIVITY_REQUEST_CODE:
                    if (data != null) {
                        Bundle args = new Bundle();
                        long selectedUserId = data.getLongExtra(AmttActivity.KEY_USER_ID, 0);
                        args.putLong(AmttActivity.KEY_USER_ID, selectedUserId);
                        getLoaderManager().restartLoader(SINGLE_USER_CURSOR_LOADER_ID, args, UserInfoActivity.this);
                    } else {
                        startActivityForResult(new Intent(UserInfoActivity.this, LoginActivity.class), LOGIN_ACTIVITY_REQUEST_CODE);
                        isNeedShowingTopButton = false;
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case LOGIN_ACTIVITY_REQUEST_CODE:
                    getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, UserInfoActivity.this);
                    break;
            }
        }
    }

    public void initViews() {
        mNameTextView = (TextView) findViewById(R.id.tv_user_name);
        mEmailAddressTextView = (TextView) findViewById(R.id.tv_email);
        mDisplayNameTextView = (TextView) findViewById(R.id.tv_fullname);
        mTimeZoneTextView = (TextView) findViewById(R.id.tv_time_zone);
        mLocaleTextView = (TextView) findViewById(R.id.tv_locale);
        mJiraUrlTextView = (TextView) findViewById(R.id.tv_jira_url);
        mUserImageImageView = (ImageView) findViewById(R.id.tv_avatar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mAddSpreadsheetButton = (Button) findViewById(R.id.btn_add_spreadsheet);
        mAddSpreadsheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setActiveUser(JUserInfo user) {
        ActiveUser.getInstance().clearActiveUser();
        ActiveUser.getInstance().setUrl(user.getUrl());
        ActiveUser.getInstance().setCredentials(user.getCredentials());
        ActiveUser.getInstance().setId(user.getId());
        ActiveUser.getInstance().setUserName(user.getName());
        ActiveUser.getInstance().setLastProjectKey(user.getLastProjectKey());
        ActiveUser.getInstance().setLastAssigneeName(user.getLastAssigneeName());
        ActiveUser.getInstance().setLastComponentsIds(user.getLastComponentsIds());
        ActiveUser.getInstance().setSpreadsheetLink(user.getLastSpreadsheetUrl());
        mNameTextView.setText(user.getName());
        mEmailAddressTextView.setText(user.getEmailAddress());
        mDisplayNameTextView.setText(user.getDisplayName());
        mTimeZoneTextView.setText(user.getTimeZone());
        mLocaleTextView.setText(user.getLocale());
        mJiraUrlTextView.setText(user.getUrl());
        ImageLoader.getInstance().displayImage(user.getAvatarUrls().getAvatarUrl(), mUserImageImageView);
        JiraContent.getInstance().clearData();
    }

    private void refreshUserInfo() {
        String requestSuffix = JiraApiConst.USER_INFO_PATH + ActiveUser.getInstance().getUserName();
        JiraApi.get().searchData(requestSuffix, UserInfoProcessor.NAME, null, null, null, this);
    }

    //Callback
    //Jira
    @Override
    public void onLoadStart() {

    }

    @Override
    public void onLoadExecuted(JUserInfo user) {
        user.setUrl(ActiveUser.getInstance().getUrl());
        setActiveUser(user);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadError(Exception e) {
        DialogUtils.createDialog(this, ExceptionType.valueOf(e)).show();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if (id == CURSOR_LOADER_ID) {
            loader = new CursorLoader(this,
                    AmttUri.USER.get(),
                    null,
                    UsersTable._ID + "=?",
                    new String[]{String.valueOf(ActiveUser.getInstance().getId())},
                    null);
        } else if (id == SINGLE_USER_CURSOR_LOADER_ID) {
            loader = new CursorLoader(UserInfoActivity.this,
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
                        if (data.getCount() > 0) {
                            reloadData(data);
                        } else {
                            Logger.d(TAG, "data==0");
                        }
                    } else {
                        Logger.d(TAG, "data==null");
                    }
                    break;
                case SINGLE_USER_CURSOR_LOADER_ID:
                    reloadData(data);
                    break;
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
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
