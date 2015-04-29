package amtt.epam.com.amtt.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import amtt.epam.com.amtt.util.Logger;
import amtt.epam.com.amtt.util.UtilConstants;

public class LoginActivity extends BaseActivity implements JiraCallback<String> {

    private EditText mUserName;
    private EditText mPassword;
    private EditText mUrl;
    private String toastText = UtilConstants.SharedPreference.EMPTY_STRING;
    private Button mLoginButton;
    private CheckBox mEpamJira;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserName = (EditText) findViewById(R.id.user_name);
        mPassword = (EditText) findViewById(R.id.password);
        mUrl = (EditText) findViewById(R.id.jira_url);
        mEpamJira = (CheckBox) findViewById(R.id.epamJiraCheckBox);

        mUrl.setText("https://jira.epam.com");

        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mUserName.getText().toString())) {
                    toastText += (UtilConstants.Dialog.INPUT_USERNAME + UtilConstants.Dialog.NEW_LINE);
                }
                if (TextUtils.isEmpty(mPassword.getText().toString())) {
                    toastText += (UtilConstants.Dialog.INPUT_PASSWORD + UtilConstants.Dialog.NEW_LINE);
                }
                if (TextUtils.isEmpty(mUrl.getText().toString())) {
                    toastText += (UtilConstants.Dialog.INPUT_URL);
                }
                if (!TextUtils.isEmpty(toastText)) {
                    Toast.makeText(LoginActivity.this, toastText, Toast.LENGTH_LONG).show();
                }
                if (toastText.length() == 0) {
                    String requestUrl = mEpamJira.isChecked() ? mUrl.getText().toString() + JiraApiConst.EPAM_JIRA_SUFFIX : mUrl.getText().toString();
                    RestMethod<String> authMethod = JiraApi.getInstance().buildAuth(mUserName.getText().toString(), mPassword.getText().toString(), requestUrl);
                    new JiraTask.Builder<String>()
                            .setRestMethod(authMethod)
                            .setCallback(LoginActivity.this)
                            .createAndExecute();
                }
                toastText = UtilConstants.SharedPreference.EMPTY_STRING;
            }
        });
    }

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
        finish();
    }

    @Override
    public void onRequestError(AmttException e) {
        ExceptionHandler.getInstance().processError(e).showDialog(this, LoginActivity.this);
        showProgress(false);
        mLoginButton.setEnabled(true);
    }

}