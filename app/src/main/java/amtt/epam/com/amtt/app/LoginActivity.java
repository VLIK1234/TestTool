package amtt.epam.com.amtt.app;

import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.user.JiraUserInfo;
import amtt.epam.com.amtt.database.task.DataBaseCallback;
import amtt.epam.com.amtt.database.task.DataBaseOperationType;
import amtt.epam.com.amtt.database.task.DataBaseTask;
import amtt.epam.com.amtt.database.task.DataBaseTaskResult;
import amtt.epam.com.amtt.processing.PriorityProcessor;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.util.Constants;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.exception.ExceptionHandler;
import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.service.TopButtonService;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.view.EditText;
import amtt.epam.com.amtt.util.UtilConstants;

public class LoginActivity extends BaseActivity implements JiraCallback, DataBaseCallback {
    
    private final String TAG = this.getClass().getSimpleName();
    private EditText mUserName;
    private EditText mPassword;
    private EditText mUrl;
    private String toastText = UtilConstants.SharedPreference.EMPTY_STRING;
    private Button mLoginButton;
    private CheckBox mEpamJira;
    private String mRequestUrl;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserName = (EditText) findViewById(R.id.user_name);
        mUserName.clearErrorOnTextChanged(true);
        mUserName.clearErrorOnFocus(true);
        mUserName.setText("admin");

        mPassword = (EditText) findViewById(R.id.password);
        mPassword.clearErrorOnTextChanged(true);
        mPassword.clearErrorOnFocus(true);
        
        mUrl = (EditText) findViewById(R.id.jira_url);
        mUrl.clearErrorOnTextChanged(true);
        mUrl.clearErrorOnFocus(true);
        mUrl.setText("https://amtt02.atlassian.net");

        mEpamJira = (CheckBox) findViewById(R.id.epamJiraCheckBox);
 
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Boolean isValid = true;

                if (TextUtils.isEmpty(mUserName.getText().toString())) {
                    mUserName.setError(UtilConstants.Dialog.INPUT_USERNAME);
                    toastText += (UtilConstants.Dialog.INPUT_USERNAME + UtilConstants.Dialog.NEW_LINE);
                    isValid = false;
                }

                if (TextUtils.isEmpty(mPassword.getText().toString())) {
                    mPassword.setError(UtilConstants.Dialog.INPUT_PASSWORD);
                    isValid = false;
                    toastText += (UtilConstants.Dialog.INPUT_PASSWORD + UtilConstants.Dialog.NEW_LINE);
                }

                if (TextUtils.isEmpty(mUrl.getText().toString())) {
                    mUrl.setError(Constants.DialogKeys.INPUT_URL);
                    isValid = false;
                    toastText += (UtilConstants.Dialog.INPUT_URL);
                }
                if (!TextUtils.isEmpty(toastText)) {
                    Toast.makeText(LoginActivity.this, toastText, Toast.LENGTH_LONG).show();
                    isValid = false;
                }

                if (isValid) {
                    showProgress(true);
                    mLoginButton.setEnabled(false);
                    mRequestUrl = mEpamJira.isChecked() ? mUrl.getText().toString() + JiraApiConst.EPAM_JIRA_SUFFIX : mUrl.getText().toString();
                    getAuthorizationAsynchronously();
                }
                toastText = UtilConstants.SharedPreference.EMPTY_STRING;
            }
        });

    }

            public void onRequestStarted() {
                showProgress(true);
                mLoginButton.setEnabled(false);
            }

    @SuppressWarnings("unchecked")
    private void getAuthorizationAsynchronously() {
        RestMethod<String> authMethod = JiraApi.getInstance().buildAuth(mUserName.getText().toString(), mPassword.getText().toString(), mRequestUrl);
        new JiraTask.Builder<String>()
                .setRestMethod(authMethod)
                .setCallback(LoginActivity.this)
                .createAndExecute();
    }

    @SuppressWarnings("unchecked")
    private void getUserInfoAsynchronously() {
        String requestSuffix = JiraApiConst.USER_INFO_PATH + CredentialsManager.getInstance().getUserName() + JiraApiConst.EXPAND_GROUPS;
        RestMethod<JiraUserInfo> userInfoMethod = JiraApi.getInstance().buildDataSearch(requestSuffix, new UserInfoProcessor());
        new JiraTask.Builder<JiraUserInfo>()
                .setRestMethod(userInfoMethod)
                .setCallback(LoginActivity.this)
                .createAndExecute();
    }
    @SuppressWarnings("unchecked")
    private void getPriorityAsynchronously() {
        String path = JiraApiConst.PROJECT_PRIORITY_PATH;
        RestMethod<JPriorityResponse> searchMethod = JiraApi.getInstance().buildDataSearch(path, new PriorityProcessor());
        new JiraTask.Builder<JPriorityResponse>()
                .setRestMethod(searchMethod)
                .setCallback(LoginActivity.this)
                .createAndExecute();
    }

            @Override
            public void onRequestPerformed(RestResponse restResponse) {
                if (restResponse.getResultObject().getClass() == String.class) {
                    showProgress(false);
                    mLoginButton.setEnabled(true);
                    String resultMessage = (String) restResponse.getResultObject();
                    CredentialsManager.getInstance().setUrl(mUrl.getText().toString());
                    CredentialsManager.getInstance().setCredentials(mUserName.getText().toString(), mPassword.getText().toString());
                    CredentialsManager.getInstance().setAccess(true);
                    TopButtonService.authSuccess(this);
                    Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
                    getUserInfoAsynchronously();
                } else if(restResponse.getResultObject().getClass() == JiraUserInfo.class) {
                    JiraUserInfo jiraUserInfo = (JiraUserInfo) restResponse.getResultObject();
                    new DataBaseTask.Builder()
                            .setContext(this)
                            .setCallback(LoginActivity.this)
                            .setOperationType(DataBaseOperationType.SAVE_USER)
                            .setJiraUserInfo(jiraUserInfo)
                            .setUserKey(jiraUserInfo.getKey())
                            .setUrl(mUrl.getText().toString())
                            .create()
                            .execute();
                    showProgress(false);
                    mLoginButton.setEnabled(true);
                }
                //finish();
            }

            @Override
            public void onRequestError(AmttException e) {
                ExceptionHandler.getInstance().processError(e).showDialog(this, LoginActivity.this);
                showProgress(false);
                mLoginButton.setEnabled(true);
            }

    @Override
    public void onDataBaseActionDone(DataBaseTaskResult result) {
        int resultMessage;
        switch (result) {
            case DONE:
                resultMessage = R.string.data_base_action_done;
                break;
            case ERROR:
                resultMessage = R.string.data_base_action_error;
                break;
            default:
                resultMessage = R.string.data_base_cleared;
                break;
        }
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }


}