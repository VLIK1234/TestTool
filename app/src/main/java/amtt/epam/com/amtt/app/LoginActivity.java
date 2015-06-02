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
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.ticket.JiraContent;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.Constants.Symbols;
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

    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private EditText mUrlEditText;
    private Button mLoginButton;
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
        mUserNameEditText = (EditText) findViewById(R.id.et_username);
        mUserNameEditText.setText("admin");
        mPasswordEditText = (EditText) findViewById(R.id.et_password);
        mPasswordEditText.setText("bujhm515");
        mPasswordEditText.clearErrorOnTextChanged(true);
        mPasswordEditText.clearErrorOnFocus(true);
        mUrlEditText = (EditText) findViewById(R.id.et_jira_url);
        mUrlEditText.setText("https://amtt05.atlassian.net");
        mLoginButton = (Button) findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });
    }

    private void sendAuthRequest() {
        mRequestUrl = mUrlEditText.getText().toString();
        String userName = mUserNameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
            //get user info and perform auth in one request
            String requestSuffix = JiraApiConst.USER_INFO_PATH + mUserNameEditText.getText().toString();
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
        if (TextUtils.isEmpty(mUserNameEditText.getText().toString())) {
            mUserNameEditText.setError("");
            isAnyEmptyField = true;
        }
        if (TextUtils.isEmpty(mPasswordEditText.getText().toString())) {
            isAnyEmptyField = true;
            mPasswordEditText.setError("");
        }
        if (TextUtils.isEmpty(mUrlEditText.getText().toString())) {
            isAnyEmptyField = true;
            mUrlEditText.setError("");
        }
        if (!isAnyEmptyField) {
            showProgress(true);
            mLoginButton.setEnabled(false);
            StepUtil.checkUser(mUserNameEditText.getText().toString(), new IResult<List<DatabaseEntity>>() {
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

    private void setActiveUser() {
        final ActiveUser activeUser = ActiveUser.getInstance();
        final String userName = mUserNameEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();
        activeUser.setCredentials(userName, password, mRequestUrl);
        activeUser.setUserName(userName);
        activeUser.setUrl(mUrlEditText.getText().toString());
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
                user.setUrl(mUrlEditText.getText().toString());
                if (ActiveUser.getInstance().getCredentials() == null) {
                    setActiveUser();
                }
                else {
                    setActiveUser();
                    user.setCredentials(ActiveUser.getInstance().getCredentials());
                }
                insertUserToDatabase(user);
            }
        }
        ScheduledExecutorService worker =
                Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            public void run() {
                setActiveUser();
                TopButtonService.start(getBaseContext());
                JiraContent.getInstance().getPrioritiesNames(null);
                JiraContent.getInstance().getProjectsNames(null);
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
                    mUserNameEditText.setText(user.getName());
                    mUrlEditText.setText(user.getUrl());
                    mPasswordEditText.setText(Symbols.EMPTY);
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


