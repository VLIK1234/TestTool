package amtt.epam.com.amtt.app;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.exception.ExceptionHandler;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.database.task.DataBaseCallback;
import amtt.epam.com.amtt.database.task.DataBaseOperationType;
import amtt.epam.com.amtt.database.task.DataBaseResponse;
import amtt.epam.com.amtt.database.task.DataBaseTask;
import amtt.epam.com.amtt.database.task.DataBaseTaskResult;
import amtt.epam.com.amtt.service.TopButtonService;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.view.EditText;

/**
 * Created by Artsiom_Kaliaha on 07.05.2015.
 */
public class LoginActivity extends BaseActivity implements JiraCallback<String>, DataBaseCallback<Boolean> {

    private EditText mUserName;
    private EditText mPassword;
    private EditText mUrl;
    private String mToastText = Constants.Str.EMPTY;
    private Button mLoginButton;
    private CheckBox mEpamJira;

    private InputMethodManager mInputManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mInputManager.hideSoftInputFromWindow(mUserName.getWindowToken(), NO_FLAGS);
    }

    private void initViews() {
        mUserName = (EditText) findViewById(R.id.user_name);
        mPassword = (EditText) findViewById(R.id.password);
        mUrl = (EditText) findViewById(R.id.jira_url);
        mEpamJira = (CheckBox) findViewById(R.id.epam_jira_checkbox);

        mUrl.setText("https://amtt02.atlassian.net");
        mUserName.setText("artsiom_kaliaha");

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
    private void sendAuthRequest() {
        String requestUrl = mEpamJira.isChecked() ? mUrl.getText().toString() + JiraApiConst.EPAM_JIRA_SUFFIX : mUrl.getText().toString();
        RestMethod<String> authMethod = JiraApi.getInstance().buildAuth(mUserName.getText().toString(), mPassword.getText().toString(), requestUrl);
        new JiraTask.Builder<String>()
                .setRestMethod(authMethod)
                .setCallback(LoginActivity.this)
                .createAndExecute();
    }

    private void isUserAlreadyInDatabase() {
        new DataBaseTask.Builder<Boolean>()
                .setContext(this)
                .setCallback(this)
                .setOperationType(DataBaseOperationType.CHECK_USERS_AVAILABILITY)
                .setUserName(mUserName.getText().toString())
                .createAndExecute();
    }

    private void insertUserToDatabase() {
        ContentValues userValues = new ContentValues();
        userValues.put(UsersTable._USER_NAME, mUserName.getText().toString());
        userValues.put(UsersTable._PASSWORD, mPassword.getText().toString());
        getContentResolver().insert(AmttContentProvider.USER_CONTENT_URI, userValues);
        finish();
    }

    private void checkFields() {
        if (TextUtils.isEmpty(mUserName.getText().toString())) {
            mToastText = getString(R.string.enter_prefix) + getString(R.string.enter_username);
        }
        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            if (TextUtils.isEmpty(mToastText)) {
                mToastText = getString(R.string.enter_prefix) + getString(R.string.enter_password);
            } else {
                mToastText += Constants.Str.COMMA + getString(R.string.enter_password);
            }
        }
        if (TextUtils.isEmpty(mUrl.getText().toString())) {
            if (TextUtils.isEmpty(mToastText)) {
                mToastText = getString(R.string.enter_prefix) + getString(R.string.enter_url);
            } else {
                mToastText += Constants.Str.COMMA + getString(R.string.enter_url);
            }
        }
        if (!TextUtils.isEmpty(mToastText)) {
            Toast.makeText(this, mToastText, Toast.LENGTH_LONG).show();
        } else {
            isUserAlreadyInDatabase();
        }
        mToastText = Constants.Str.EMPTY;
    }

    //Callbacks
    //Jira
    @Override
    public void onRequestStarted() {
        showProgress(true);
        mLoginButton.setEnabled(false);
    }

    @Override
    public void onRequestPerformed(RestResponse<String> restResponse) {
        showProgress(false);
        mLoginButton.setEnabled(true);
        String resultMessage = restResponse.getResultObject();
        CredentialsManager.getInstance().setUrl(mUrl.getText().toString());
        CredentialsManager.getInstance().setCredentials(mUserName.getText().toString(), mPassword.getText().toString());
        CredentialsManager.getInstance().setAccess(true);
        TopButtonService.authSuccess(this);
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
        insertUserToDatabase();
        TopButtonService.authSuccess(this);
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
        if (dataBaseResponse.getTaskResult() == DataBaseTaskResult.DONE && !dataBaseResponse.getValueResult()) {
            sendAuthRequest();
        } else {
            Toast.makeText(this, getString(R.string.user_already_exists), Toast.LENGTH_SHORT).show();
            showProgress(false);
        }
    }

}
