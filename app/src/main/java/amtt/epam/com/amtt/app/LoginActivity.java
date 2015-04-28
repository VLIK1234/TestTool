package amtt.epam.com.amtt.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.JiraTask.JiraTaskType;
import amtt.epam.com.amtt.api.result.AuthorizationResult;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.service.TopButtonService;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.view.EditText;

public class LoginActivity extends BaseActivity implements JiraCallback<AuthorizationResult,Void> {

    private EditText userName;
    private EditText password;
    private EditText url;
    private Button loginButton;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        url = (EditText) findViewById(R.id.jira_url);

        url.setText("https://amtt01.atlassian.net");
        userName.setText("iryna_monchanka");

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Boolean isValid = true;

                if (TextUtils.isEmpty(userName.getText().toString())) {
                    userName.setError(Constants.DialogKeys.INPUT_USERNAME);
                    userName.invalidate();
                    isValid = false;
                }

                if (TextUtils.isEmpty(password.getText().toString())) {
                    password.setError(Constants.DialogKeys.INPUT_PASSWORD);
                    //password.getOverlay();
                    password.invalidate();
                    isValid = false;
                }

                if (TextUtils.isEmpty(url.getText().toString())) {
                    url.setError(Constants.DialogKeys.INPUT_URL);
                    url.invalidate();
                    isValid = false;
                }

                if(isValid){
                    showProgress(true);
                    CredentialsManager.getInstance().setUrl(url.getText().toString());
                    CredentialsManager.getInstance().setCredentials(userName.getText().toString(), password.getText().toString());
                    new JiraTask.Builder<AuthorizationResult, Void>()
                            .setOperationType(JiraTaskType.AUTH)
                            .setCallback(LoginActivity.this)
                            .create()
                            .execute();
                    loginButton.setEnabled(false);
                }

            }
        });

        userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    userName.setError(null);
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    password.setError(null);
            }
        });

        url.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    url.setError(null);
            }
        });

    }

    @Override
    public void onJiraRequestPerformed(RestResponse<AuthorizationResult,Void> restResponse) {
        Toast.makeText(this, restResponse.getMessage(), Toast.LENGTH_SHORT).show();
        showProgress(false);
        loginButton.setEnabled(true);
        if (restResponse.getResult() == AuthorizationResult.SUCCESS) {
            CredentialsManager.getInstance().setUserName(userName.getText().toString());
            CredentialsManager.getInstance().setUrl(url.getText().toString());
            CredentialsManager.getInstance().setAccess(true);
            TopButtonService.authSuccess(this);
            finish();
        }
    }

}