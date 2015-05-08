package amtt.epam.com.amtt.app;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

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
import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.database.task.DataBaseCallback;
import amtt.epam.com.amtt.database.task.DataBaseOperationType;
import amtt.epam.com.amtt.database.task.DataBaseResponse;
import amtt.epam.com.amtt.database.task.DataBaseTask;
import amtt.epam.com.amtt.database.task.DataBaseTaskResult;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.Constants.Str;
import amtt.epam.com.amtt.view.EditText;

/**
 * Created by Artsiom_Kaliaha on 07.05.2015.
 */
public class LoginActivity extends BaseActivity implements JiraCallback<JiraUserInfo>, DataBaseCallback<Boolean>, LoaderCallbacks<Cursor> {

    private AutoCompleteTextView mUserName;
    private EditText mPassword;
    private EditText mUrl;
    private String mToastText;
    private Button mLoginButton;
    private CheckBox mEpamJira;
    private String mRequestUrl;
    private Map<String,String> mUserUrlMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initViews() {
        mUserName = (AutoCompleteTextView) findViewById(R.id.user_name);
        mPassword = (EditText) findViewById(R.id.password);
        mUrl = (EditText) findViewById(R.id.jira_url);
        mEpamJira = (CheckBox) findViewById(R.id.epam_jira_checkbox);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void sendAuthRequest(boolean isUserInDatabase) {
        mRequestUrl = mEpamJira.isChecked() ? mUrl.getText().toString() + JiraApiConst.EPAM_JIRA_SUFFIX : mUrl.getText().toString();
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        if (isUserInDatabase) {
            RestMethod<String> authMethod = JiraApi.getInstance().buildAuth(userName, password, mRequestUrl);
            new JiraTask.Builder<String>()
                    .setRestMethod(authMethod)
                    .setCallback(LoginActivity.this)
                    .createAndExecute();
        } else {
            String requestSuffix = JiraApiConst.USER_INFO_PATH + mUserName.getText().toString() + JiraApiConst.EXPAND_GROUPS;
            RestMethod<JiraUserInfo> userInfoMethod = JiraApi.getInstance().buildDataSearch(requestSuffix,
                    new UserInfoProcessor(),
                    userName,
                    password,
                    mRequestUrl);
            new JiraTask.Builder<JiraUserInfo>()
                    .setRestMethod(userInfoMethod)
                    .setCallback(LoginActivity.this)
                    .createAndExecute();
        }
    }

    private void isUserAlreadyInDatabase() {
        new DataBaseTask.Builder<Boolean>()
                .setContext(this)
                .setCallback(this)
                .setOperationType(DataBaseOperationType.CHECK_USERS_AVAILABILITY)
                .setUserName(mUserName.getText().toString())
                .createAndExecute();
    }

    private void insertUserToDatabase(JiraUserInfo user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UsersTable._USER_NAME, user.getName());
        contentValues.put(UsersTable._URL, mRequestUrl);
        contentValues.put(UsersTable._KEY, user.getKey());
        contentValues.put(UsersTable._EMAIL, user.getEmailAddress());
        contentValues.put(UsersTable._AVATAR_16, user.getAvatarUrls().getAvatarXSmallUrl());
        contentValues.put(UsersTable._AVATAR_24, user.getAvatarUrls().getAvatarSmallUrl());
        contentValues.put(UsersTable._AVATAR_32, user.getAvatarUrls().getAvatarMediumUrl());
        contentValues.put(UsersTable._AVATAR_48, user.getAvatarUrls().getAvatarUrl());
        getContentResolver().insert(AmttContentProvider.USER_CONTENT_URI, contentValues);

        ActiveUser activeUser = ActiveUser.getInstance();
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        activeUser.setCredentials(userName, password);
        activeUser.setUrl(mRequestUrl);
    }

    private void checkFields() {
        if (TextUtils.isEmpty(mUserName.getText().toString())) {
            mToastText = getString(R.string.enter_prefix) + getString(R.string.enter_username);
        }
        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            if (TextUtils.isEmpty(mToastText)) {
                mToastText = getString(R.string.enter_prefix) + getString(R.string.enter_password);
            } else {
                mToastText += Str.COMMA + getString(R.string.enter_password);
            }
        }
        if (TextUtils.isEmpty(mUrl.getText().toString())) {
            if (TextUtils.isEmpty(mToastText)) {
                mToastText = getString(R.string.enter_prefix) + getString(R.string.enter_url);
            } else {
                mToastText += Str.COMMA + getString(R.string.enter_url);
            }
        }
        if (!TextUtils.isEmpty(mToastText)) {
            Toast.makeText(this, mToastText, Toast.LENGTH_LONG).show();
        } else {
            isUserAlreadyInDatabase();
        }
        mToastText = Str.EMPTY;
    }

    //Callbacks
    //Jira
    @Override
    public void onRequestStarted() {
        showProgress(true);
        mLoginButton.setEnabled(false);
    }

    @Override
    public void onRequestPerformed(RestResponse<JiraUserInfo> restResponse) {
        showProgress(false);
        Toast.makeText(this, R.string.auth_passed, Toast.LENGTH_SHORT).show();
        if (restResponse.getOpeartionResult() == JiraOperationResult.REQUEST_PERFORMED) {
            if (restResponse.getResultObject() instanceof JiraUserInfo) {
                JiraUserInfo user = restResponse.getResultObject();
                insertUserToDatabase(user);
            }
        }
        finish();
    }

    @Override
    public void onRequestError(AmttException e) {
        ExceptionHandler.getInstance().processError(e).showDialog(this, this);
        showProgress(false);
        mLoginButton.setEnabled(true);
    }

    //Database task
    @Override
    public void onDataBaseActionDone(DataBaseResponse<Boolean> dataBaseResponse) {
        if (dataBaseResponse.getTaskResult() == DataBaseTaskResult.DONE) {
            sendAuthRequest(dataBaseResponse.getValueResult());
        }
    }

    //Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, AmttContentProvider.USER_CONTENT_URI, UsersTable.PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        showProgress(false);
        if (data != null && data.getCount() > 0) {
            mUserUrlMap = new HashMap<>();
            String userName;
            String url;
            while(data.moveToNext()) {
                url = data.getString(data.getColumnIndex(UsersTable._URL));
                userName = data.getString(data.getColumnIndex(UsersTable._USER_NAME));
                mUserUrlMap.put(userName, url);
            }

            ArrayAdapter<String> usersAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line,
                    mUserUrlMap.keySet().toArray(new String[mUserUrlMap.keySet().size()]));
            mUserName.setAdapter(usersAdapter);
            mUserName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String userName = ((TextView)view).getText().toString();
                    mUserName.setText(userName);
                    mUrl.setText(mUserUrlMap.get(userName));
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

}
