package amtt.epam.com.amtt.app;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.LoginItemAdapter;
import amtt.epam.com.amtt.adapter.LoginItemAdapter.ViewHolder;
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
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.dao.Dao;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.database.task.DataBaseCRUD;
import amtt.epam.com.amtt.database.task.DataBaseCallback;
import amtt.epam.com.amtt.database.task.DataBaseMethod;
import amtt.epam.com.amtt.database.task.DataBaseTask;
import amtt.epam.com.amtt.database.task.DataBaseTask.DataBaseResponse;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.Constants.Str;
import amtt.epam.com.amtt.view.EditText;

/**
 * Created by Artsiom_Kaliaha on 07.05.2015.
 */
@SuppressWarnings("unchecked")
public class LoginActivity extends BaseActivity implements JiraCallback<JiraUserInfo>, DataBaseCallback<Boolean>, LoaderCallbacks<Cursor> {

    private static final int AMTT_ACTIVITY_REQUEST_CODE = 1;

    private AutoCompleteTextView mUserName;
    private EditText mPassword;
    private EditText mUrl;
    private String mToastText;
    private Button mLoginButton;
    private CheckBox mEpamJira;
    private String mRequestUrl;
    private Map<String, String> mUserUrlMap;
    private Map<String, Integer> mUserIdMap;
    private boolean isInDatabase;

    private LoaderCallbacks<Cursor> mSingleUserCallback = new LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(LoginActivity.this,
                    AmttUri.USER.get(),
                    null,
                    UsersTable._ID + "=?",
                    new String[] {String.valueOf(args.getLong(AmttActivity.KEY_USER_ID))},
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            JiraUserInfo user = new JiraUserInfo(data);
            mUserName.setText(user.getName());
            mUrl.setText(user.getEmailAddress());
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TopButtonService.close(this);
        initViews();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TopButtonService.start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AMTT_ACTIVITY_REQUEST_CODE:
                    getLoaderManager().initLoader(CURSOR_LOADER_ID, data.getBundleExtra(AmttActivity.KEY_USER_ID), mSingleUserCallback);
                    break;
            }
        }
    }

    private void initViews() {
        mUserName = (AutoCompleteTextView) findViewById(R.id.user_name);
        mUserName.setText("artsiom_kaliaha");
        mPassword = (EditText) findViewById(R.id.password);
        mUrl = (EditText) findViewById(R.id.jira_url);
        mUrl.setText("https://amtt03.atlassian.net");
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
            //get user info and perform auth in one request
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
        DataBaseMethod<Boolean> dataBaseMethod = DataBaseCRUD.getInstance().buildCheckUser(mUserName.getText().toString(), mUrl.getText().toString());
        new DataBaseTask.Builder<Boolean>()
                .setCallback(this)
                .setMethod(dataBaseMethod)
                .createAndExecute();
    }

    private void insertUserToDatabase(JiraUserInfo user) {
        user.setUrl(mRequestUrl);
        DataBaseMethod<Void> dataBaseMethod = new DataBaseCRUD().buildResetPreviousActiveUser();
        new DataBaseTask.Builder<>()
                .setCallback(this)
                .setMethod(dataBaseMethod)
                .createAndExecute();
        int userId = new Dao().addOrUpdate(user);
        ActiveUser.getInstance().setId(userId);
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

    private void populateActiveUserInfo() {
        ActiveUser activeUser = ActiveUser.getInstance();
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        activeUser.setCredentials(userName, password, mRequestUrl);
        if (activeUser.getId() == ActiveUser.DEFAULT_ID) {
            int userId = mUserIdMap.get(userName);
            activeUser.setId(userId);
            JiraUserInfo user = new JiraUserInfo();
            user.setName(userName);
            new Dao().update(user);
        }
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
            if (restResponse.getResultObject() instanceof JiraUserInfo && !isInDatabase) {
                JiraUserInfo user = restResponse.getResultObject();
                insertUserToDatabase(user);
            }
        }
        populateActiveUserInfo();
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
    public void onDataBaseRequestPerformed(DataBaseResponse<Boolean> dataBaseResponse) {
        isInDatabase = dataBaseResponse.getResult();
        sendAuthRequest(dataBaseResponse.getResult());
        TopButtonService.authSuccess(this);
    }

    @Override
    public void onDataBaseRequestError(Exception e) {
        Toast.makeText(this, R.string.database_operation_error, Toast.LENGTH_SHORT).show();
    }

    //Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, AmttUri.USER.get(), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        showProgress(false);
        if (data != null) {
            if (data.getCount() < 2) {
                //TODO put userName and url to EditText
                mUserUrlMap = new HashMap<>();
                mUserIdMap = new HashMap<>();
                String userName;
                String url;
                int userId;
                while (data.moveToNext()) {
                    url = data.getString(data.getColumnIndex(UsersTable._URL));
                    userName = data.getString(data.getColumnIndex(UsersTable._USER_NAME));
                    userId = data.getInt(data.getColumnIndex(UsersTable._ID));
                    mUserUrlMap.put(userName, url);
                    mUserIdMap.put(userName, userId);
                }

                LoginItemAdapter adapter = new LoginItemAdapter(this, data, NO_FLAGS);
                mUserName.setAdapter(adapter);
                mUserName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ViewHolder vh = (ViewHolder) view.getTag();
                        String userName = vh.getTextView().getText().toString();
                        mUserName.setText(userName);
                        mUrl.setText(mUserUrlMap.get(userName));
                    }
                });
            } else {
                startActivityForResult(new Intent(this, AmttActivity.class), AMTT_ACTIVITY_REQUEST_CODE);
            }

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

}
