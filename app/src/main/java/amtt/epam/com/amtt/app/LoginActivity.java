package amtt.epam.com.amtt.app;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.exception.ExceptionHandler;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.JiraOperationResult;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbObjectManger;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.helper.SystemInfoHelper;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.ticket.JiraContent;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.Constants.Str;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.StepUtil;
import amtt.epam.com.amtt.view.EditText;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 @author Artsiom_Kaliaha
 @version on 07.05.2015
 */

@SuppressWarnings("unchecked")
public class LoginActivity extends BaseActivity implements JiraCallback<JUserInfo>, LoaderCallbacks<Cursor> {

    private static final int SINGLE_USER_CURSOR_LOADER_ID = 1;

    private EditText mUserName;
    private EditText mPassword;
    private EditText mUrl;
    private Button mLoginButton;
    private CheckBox mEpamJira;
    private String mRequestUrl;
    private boolean mIsUserInDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initViews() {
        mUserName = (EditText) findViewById(R.id.user_name);
        mUserName.setText("admin");
        mPassword = (EditText) findViewById(R.id.password);
        mPassword.setText("bujhm515");
        mPassword.clearErrorOnTextChanged(true);
        mPassword.clearErrorOnFocus(true);
        mUrl = (EditText) findViewById(R.id.jira_url);
        mUrl.setText("https://amtt05.atlassian.net");
        mEpamJira = (CheckBox) findViewById(R.id.epam_jira_checkbox);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });
    }

    private void sendAuthRequest() {
        mRequestUrl = mEpamJira.isChecked() ? mUrl.getText().toString() + JiraApiConst.EPAM_JIRA_SUFFIX : mUrl.getText().toString();
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
            //get user info and perform auth in one request
            String requestSuffix = JiraApiConst.USER_INFO_PATH + mUserName.getText().toString() + JiraApiConst.EXPAND_GROUPS;
            RestMethod<JUserInfo> userInfoMethod = JiraApi.getInstance().buildDataSearch(requestSuffix,
                    new UserInfoProcessor(),
                    userName,
                    password,
                    mRequestUrl);
            new JiraTask.Builder<JUserInfo>()
                    .setRestMethod(userInfoMethod)
                    .setCallback(LoginActivity.this)
                    .createAndExecute();
    }

    private void insertUserToDatabase(final JUserInfo user) {
        DbObjectManger.INSTANCE.addOrUpdateAsync(user, new IResult<Integer>() {
            @Override
            public void onResult(Integer result) {
                ActiveUser.getInstance().setId(result);
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void checkFields() {
        boolean isAnyEmptyField = false;
        if (TextUtils.isEmpty(mUserName.getText().toString())) {
            mUserName.setError("");
            isAnyEmptyField = true;
        }
        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            isAnyEmptyField = true;
            mPassword.setError("");
        }
        if (TextUtils.isEmpty(mUrl.getText().toString())) {
            isAnyEmptyField = true;
            mUrl.setError("");
        }
        if (!isAnyEmptyField) {
            showProgress(true);
            mLoginButton.setEnabled(false);
            StepUtil.buildCheckUser(mUserName.getText().toString(), new IResult<List<DatabaseEntity>>() {
                @Override
                public void onResult(List<DatabaseEntity> result) {
                    mIsUserInDatabase = result.size() > 0;
                    sendAuthRequest();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }

    private void populateActiveUserInfo() {
        final ActiveUser activeUser = ActiveUser.getInstance();
        final String userName = mUserName.getText().toString();
        final String password = mPassword.getText().toString();
        activeUser.setCredentials(userName, password, mRequestUrl);
    }

    //Callbacks
    //Jira
    @Override
    public void onRequestStarted() {
    }

    @Override
    public void onRequestPerformed(RestResponse<JUserInfo> restResponse) {
        showProgress(false);
        Toast.makeText(this, R.string.auth_passed, Toast.LENGTH_SHORT).show();
        if (restResponse.getOpeartionResult() == JiraOperationResult.REQUEST_PERFORMED) {
            if (restResponse.getResultObject() != null && !mIsUserInDatabase) {
                JUserInfo user = restResponse.getResultObject();
                user.setUrl(mUrl.getText().toString());
                if (ActiveUser.getInstance().getCredentials() == null) {
                    populateActiveUserInfo();
                }
                else {
                    populateActiveUserInfo();
                    user.setCredentials(ActiveUser.getInstance().getCredentials());
                }
                insertUserToDatabase(user);
            }
        }
        ScheduledExecutorService worker =
                Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            public void run() {
                populateActiveUserInfo();
                TopButtonService.start(getBaseContext());
                JiraContent.getInstance().getPrioritiesNames(null);
                JiraContent.getInstance().getProjectsNames(null);
                JiraContent.getInstance().setEnvironment(SystemInfoHelper.getDeviceOsInfo());
            }
        };
        worker.schedule(task, 1, TimeUnit.SECONDS);
        finish();
    }

    @Override
    public void onRequestError(AmttException e) {
        ExceptionHandler.getInstance().processError(e).showDialog(this, this);
        showProgress(false);
        mLoginButton.setEnabled(true);
    }

    //Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if (id == SINGLE_USER_CURSOR_LOADER_ID) {
            loader = new CursorLoader(LoginActivity.this,
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
                case SINGLE_USER_CURSOR_LOADER_ID:
                    JUserInfo user = new JUserInfo(data);
                    mUserName.setText(user.getName());
                    mUrl.setText(user.getUrl());
                    mPassword.setText(Str.EMPTY);
                    break;
            }
        } finally {
            IOUtils.close(data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}


