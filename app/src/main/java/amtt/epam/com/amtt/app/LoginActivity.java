package amtt.epam.com.amtt.app;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.internal.util.Predicate;

import org.apache.http.auth.AuthenticationException;

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
import amtt.epam.com.amtt.util.InputsUtil;
import amtt.epam.com.amtt.util.StepUtil;
import amtt.epam.com.amtt.view.TextInput;

/**
 * @author Artsiom_Kaliaha
 * @version on 07.05.2015
 */

@SuppressWarnings("unchecked")
public class LoginActivity extends BaseActivity implements JiraCallback<JUserInfo>, LoaderCallbacks<Cursor> {

    private static final int SINGLE_USER_CURSOR_LOADER_ID = 1;

    private boolean isAnyEmptyField = false;

    private TextInput mUserNameTextInput;
    private TextInput mPasswordTextInput;
    private TextInput mUrlTextInput;
    private Button mLoginButton;
    private String mRequestUrl;
    private boolean mIsUserInDatabase;
    private RestMethod<JUserInfo> userInfoMethod;

    private Predicate<EditText> mPredicateIsEmpty;
    private Predicate<EditText> mPredicateHasWhitespaces;
    private Predicate<EditText> mPredicateHasAtSymbol;
    private Predicate<EditText> mPredicateIsCorrectUrl;
    private Predicate<EditText> mPredicateIsEpamUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initPredicates();
        initViews();
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initViews() {
        Map<Predicate<EditText>, CharSequence> userNameValidationMap = new HashMap<>();
        userNameValidationMap.put(mPredicateIsEmpty, getString(R.string.enter_prefix) + getString(R.string.enter_username));
        userNameValidationMap.put(mPredicateHasWhitespaces, getString(R.string.label_user_name) + getString(R.string.label_no_whitespaces));
        userNameValidationMap.put(mPredicateHasAtSymbol, getString(R.string.enter_prefix) + getString(R.string.enter_username) + getString(R.string.label_cannot_at));

        Map<Predicate<EditText>, CharSequence> passwordValidationMap = new HashMap<>();
        passwordValidationMap.put(mPredicateIsEmpty, getString(R.string.enter_prefix) + getString(R.string.enter_password));

        Map<Predicate<EditText>, CharSequence> urlValidationMap = new HashMap<>();
        urlValidationMap.put(mPredicateIsEmpty, getString(R.string.enter_prefix) + getString(R.string.enter_url));
        urlValidationMap.put(mPredicateIsCorrectUrl, getString(R.string.enter_prefix) + getString(R.string.enter_correct_url));
        urlValidationMap.put(mPredicateIsEpamUrl, getString(R.string.enter_prefix) + getString(R.string.enter_postfix_jira));

        mUserNameTextInput = (TextInput) findViewById(R.id.username_input);
        mUserNameTextInput.setValidationMap(userNameValidationMap);
        mPasswordTextInput = (TextInput) findViewById(R.id.password_input);
        mPasswordTextInput.setValidationMap(passwordValidationMap);
        mUrlTextInput = (TextInput) findViewById(R.id.url_input);
        mUrlTextInput.setValidationMap(urlValidationMap);
        mLoginButton = (Button) findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });
    }

    private void initPredicates() {
        mPredicateIsEmpty = new Predicate<EditText>() {
            @Override
            public boolean apply(EditText editText) {
                return TextUtils.isEmpty(editText.getText().toString());
            }
        };
        mPredicateHasWhitespaces = new Predicate<EditText>() {
            @Override
            public boolean apply(EditText editText) {
                return InputsUtil.hasWhitespaces(editText);
            }
        };
        mPredicateHasAtSymbol = new Predicate<EditText>() {
            @Override
            public boolean apply(EditText editText) {
                return InputsUtil.hasAtSymbol(editText);
            }
        };
        mPredicateIsCorrectUrl = new Predicate<EditText>() {
            @Override
            public boolean apply(EditText editText) {
                return InputsUtil.checkUrl(editText);
            }
        };
        mPredicateIsEpamUrl = new Predicate<EditText>() {
            @Override
            public boolean apply(EditText editText) {
                return getBaseContext().getString(R.string.epam_url).equals(editText.getText().toString());
            }
        };
    }

    private void sendAuthRequest() {
        mRequestUrl = mUrlTextInput.getText().toString();
        String userName = mUserNameTextInput.getText().toString();
        String password = mPasswordTextInput.getText().toString();
        //get user info and perform auth in one request
        String requestSuffix = JiraApiConst.USER_INFO_PATH + mUserNameTextInput.getText().toString();
        userInfoMethod = JiraApi.getInstance().buildDataSearch(requestSuffix,
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
        if (!mUserNameTextInput.validate() | !mPasswordTextInput.validate() | !mUrlTextInput.validate()) {
            return;
        }
        showProgress(true);
        mLoginButton.setEnabled(false);
        StepUtil.checkUser(mUserNameTextInput.getText().toString(), new IResult<List<DatabaseEntity>>() {
            @Override
            public void onResult(List<DatabaseEntity> result) {
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
                JiraContent.getInstance().getPrioritiesNames(null);
                JiraContent.getInstance().getProjectsNames(null);
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
                finish();
            } else if (restResponse.getResultObject() == null) {
                ExceptionHandler.getInstance().processError(new AmttException(new AuthenticationException(), 403, userInfoMethod)).showDialog(LoginActivity.this, LoginActivity.this);
                mLoginButton.setEnabled(true);
            } else {
                setActiveUser();
                Toast.makeText(this, R.string.auth_passed, Toast.LENGTH_SHORT).show();
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


