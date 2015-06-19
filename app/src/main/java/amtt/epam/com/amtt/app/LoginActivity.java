package amtt.epam.com.amtt.app;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.JiraGetContentCallback;
import amtt.epam.com.amtt.api.loadcontent.JiraContent;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.exception.ExceptionHandler;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.Constants.Symbols;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.InputsUtil;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Artsiom_Kaliaha
 * @version on 07.05.2015
 */

@SuppressWarnings("unchecked")
public class LoginActivity extends BaseActivity implements Callback<JUserInfo>, LoaderCallbacks<Cursor> {

    private static final int SINGLE_USER_CURSOR_LOADER_ID = 1;
    public static final String KEY_USER_ID = "key_user_id";

    private final String TAG = this.getClass().getSimpleName();
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private EditText mUrlEditText;
    private Button mLoginButton;
    private String mRequestUrl;
    private boolean mIsUserInDatabase;
    private boolean isNeedShowingTopButton;
    private InputMethodManager mInputMethodManager;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        if (isNewUserAdditionFromUserInfo()) {
            JiraApi.get().signOut();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initViews() {
        mUserNameEditText = (EditText) findViewById(R.id.et_username);
        mPasswordEditText = (EditText) findViewById(R.id.et_password);
        mUrlEditText = (EditText) findViewById(R.id.et_jira_url);
        mUrlEditText.setText("https://");
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
        //getClient user info and perform auth in one request
        String requestSuffix = JiraApiConst.USER_INFO_PATH + mUserNameEditText.getText().toString();
        JiraApi.get().searchData(requestSuffix, UserInfoProcessor.NAME, userName, password, mRequestUrl, this);
    }

    private void insertUserToDatabase(final JUserInfo user) {
        DbObjectManager.INSTANCE.add(user, new IResult<Integer>() {
            @Override
            public void onResult(Integer result) {
                ActiveUser.getInstance().setId(result);
                finish();
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void checkFields() {
        boolean isAnyEmptyField = false;
        //check username
        if (TextUtils.isEmpty(mUserNameEditText.getText().toString())) {
            mUserNameEditText.setError(getString(R.string.enter_prefix) + getString(R.string.enter_username));
            isAnyEmptyField = true;
        } else if (InputsUtil.haveWhitespaces(mUserNameEditText.getText().toString())) {
            mUserNameEditText.setError(getString(R.string.label_user_name) + getString(R.string.label_no_whitespaces));
            isAnyEmptyField = true;
        } else if (InputsUtil.hasAtSymbol(mUserNameEditText.getText().toString())) {
            mUserNameEditText.setError(getString(R.string.enter_prefix) + getString(R.string.enter_username) + getString(R.string.label_cannot_at));
            isAnyEmptyField = true;
        }

        if (TextUtils.isEmpty(mPasswordEditText.getText().toString())) {
            isAnyEmptyField = true;
            mPasswordEditText.setError(getString(R.string.enter_prefix) + getString(R.string.enter_password));
        }

        //check url
        if (TextUtils.isEmpty(mUrlEditText.getText().toString()) || getString(R.string.url_prefix).equals(mUrlEditText.getText().toString())) {
            isAnyEmptyField = true;
            mUrlEditText.setError(getString(R.string.enter_prefix) + getString(R.string.enter_url));
        } else if (InputsUtil.checkUrl(mUrlEditText.getText().toString())) {
            isAnyEmptyField = true;
            mUrlEditText.setError(getString(R.string.enter_prefix) + getString(R.string.enter_correct_url));
        } else if (getString(R.string.epam_url).equals(mUrlEditText.getText().toString())) {
            isAnyEmptyField = true;
            mUrlEditText.setError(getString(R.string.enter_prefix) + getString(R.string.enter_postfix_jira));
        }
        if (!isAnyEmptyField) {
            showProgress(true);
            mLoginButton.setEnabled(false);
            StepUtil.checkUser(mUserNameEditText.getText().toString(), new IResult<List<JUserInfo>>() {
                @Override
                public void onResult(List<JUserInfo> result) {
                    mIsUserInDatabase = result.size() > 0;
                    ActiveUser.getInstance().clearActiveUser();
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            sendAuthRequest();
                        }
                    });
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
        ScheduledExecutorService worker =
                Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            public void run() {
                TopButtonService.start(getBaseContext());
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
    }

    private boolean isNewUserAdditionFromUserInfo() {
        isNeedShowingTopButton = ActiveUser.getInstance().getUrl() == null;
        return ActiveUser.getInstance().getUrl() != null;
    }

    //Callbacks
    //Jira
    @Override
    public void onLoadStart() {

    }

    @Override
    public void onLoadExecuted(JUserInfo user) {
        showProgress(false);
        if (user != null && !mIsUserInDatabase) {
            user.setUrl(mUrlEditText.getText().toString());
            setActiveUser();
            user.setCredentials(ActiveUser.getInstance().getCredentials());
            insertUserToDatabase(user);
            Toast.makeText(this, R.string.auth_passed, Toast.LENGTH_SHORT).show();
        } else {
            setActiveUser();
            Toast.makeText(this, R.string.auth_passed, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onLoadError(Exception e) {
        ExceptionHandler.getInstance().processError(e).showDialog(LoginActivity.this, LoginActivity.this);
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


