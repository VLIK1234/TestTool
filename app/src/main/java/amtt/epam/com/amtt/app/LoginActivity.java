package amtt.epam.com.amtt.app;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.internal.util.Predicate;

import org.apache.http.auth.AuthenticationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraGetContentCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.exception.ExceptionHandler;
import amtt.epam.com.amtt.api.loadcontent.JiraContent;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.JiraOperationResult;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.Constants.Symbols;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.InputsUtil;
import amtt.epam.com.amtt.util.Logger;
import amtt.epam.com.amtt.util.Validator;
import amtt.epam.com.amtt.view.TextInput;


/**
 * @author Artsiom_Kaliaha
 * @version on 07.05.2015
 */

@SuppressWarnings("unchecked")
public class LoginActivity extends BaseActivity implements JiraCallback<JUserInfo>, LoaderCallbacks<Cursor> {

    private static final int SINGLE_USER_CURSOR_LOADER_ID = 1;

    private TextInput mUserNameTextInput;
    private TextInput mPasswordTextInput;
    private TextInput mUrlTextInput;

    private final String TAG = this.getClass().getSimpleName();
    private Button mLoginButton;
    private String mRequestUrl;
    private boolean mIsUserInDatabase;
    private RestMethod<JUserInfo> mUserInfoMethod;
    private InputMethodManager mInputMethodManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initViews() {
        mUserNameTextInput = (TextInput) findViewById(R.id.username_input);
        mUserNameTextInput.setValidators(new ArrayList<Validator>() {{
            add(InputsUtil.getEmptyValidator());
            add(InputsUtil.getWhitespacesValidator());
            add(InputsUtil.getNoEmailValidator());
        }});
        mPasswordTextInput = (TextInput) findViewById(R.id.password_input);
        mPasswordTextInput.setValidators(new ArrayList<Validator>(){{
            add(InputsUtil.getEmptyValidator());
        }});
        mUrlTextInput = (TextInput) findViewById(R.id.url_input);
        mUrlTextInput.setValidators(new ArrayList<Validator>(){{
            add(InputsUtil.getEmptyValidator());
            add(InputsUtil.getCorrectUrlValidator());
            add(InputsUtil.getEpamUrlValidator());
        }});
        mLoginButton = (Button) findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });
    }

    private void sendAuthRequest() {
        mRequestUrl = mUrlTextInput.getText().toString();
        String userName = mUserNameTextInput.getText().toString();
        String password = mPasswordTextInput.getText().toString();
        //get user info and perform auth in one request
        String requestSuffix = JiraApiConst.USER_INFO_PATH + mUserNameTextInput.getText().toString();
        mUserInfoMethod = JiraApi.getInstance().buildDataSearch(requestSuffix,
                new UserInfoProcessor(),
                userName,
                password,
                mRequestUrl);
        new JiraTask.Builder<JUserInfo>()
                .setRestMethod(mUserInfoMethod)
                .setCallback(LoginActivity.this)
                .createAndExecute();
    }

    private void insertUserToDatabase(final JUserInfo user) {
        DbObjectManager.INSTANCE.add(user, new IResult<Integer>() {
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
        if (!mUserNameTextInput.validate() | !mPasswordTextInput.validate() | !mUrlTextInput.validate()) {
            return;
        }
        showProgress(true);
        mLoginButton.setEnabled(false);
        StepUtil.checkUser(mUserNameTextInput.getText().toString(), new IResult<List<JUserInfo>>() {
            @Override
            public void onResult(List<JUserInfo> result) {
                mIsUserInDatabase = result.size() > 0;
                ActiveUser.getInstance().clearActiveUser();
                sendAuthRequest();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void setActiveUser() {
        final ActiveUser activeUser = ActiveUser.getInstance();
        final String userName = mUserNameTextInput.getText().toString();
        final String password = mPasswordTextInput.getText().toString();
        activeUser.setCredentials(userName, password, mRequestUrl);
        activeUser.setUserName(userName);
        activeUser.setUrl(mUrlTextInput.getText().toString());
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

    //Callbacks
    //Jira
    @Override
    public void onRequestStarted() {
    }

    @Override
    public void onRequestPerformed(RestResponse<JUserInfo> restResponse) {
        showProgress(false);
        if (restResponse.getOpeartionResult() == JiraOperationResult.REQUEST_PERFORMED) {
            if (restResponse.getResultObject() != null && !mIsUserInDatabase) {
                JUserInfo user = restResponse.getResultObject();
                user.setUrl(mUrlTextInput.getText().toString());
                setActiveUser();
                user.setCredentials(ActiveUser.getInstance().getCredentials());
                insertUserToDatabase(user);
                Toast.makeText(this, R.string.auth_passed, Toast.LENGTH_SHORT).show();
                mInputMethodManager.hideSoftInputFromInputMethod(mUserNameTextInput.getWindowToken(), 0);
                finish();
            } else if (restResponse.getResultObject() == null) {
                ExceptionHandler.getInstance().processError(new AmttException(new AuthenticationException(), 403, mUserInfoMethod)).showDialog(LoginActivity.this, LoginActivity.this);
                mLoginButton.setEnabled(true);
            } else {
                setActiveUser();
                Toast.makeText(this, R.string.auth_passed, Toast.LENGTH_SHORT).show();
                mInputMethodManager.hideSoftInputFromInputMethod(mUserNameTextInput.getWindowToken(), 0);
                finish();
            }
        }
    }

    @Override
    public void onRequestError(AmttException e) {
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
                    mUserNameTextInput.setText(user.getName());
                    mUrlTextInput.setText(user.getUrl());
                    mPasswordTextInput.setText(Symbols.EMPTY);
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


