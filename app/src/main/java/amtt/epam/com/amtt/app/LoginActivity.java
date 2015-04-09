package amtt.epam.com.amtt.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.authorization.AuthorizationCallback;
import amtt.epam.com.amtt.authorization.AuthorizationResult;
import amtt.epam.com.amtt.authorization.AuthorizationTask;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.util.InputsChecker;

public class LoginActivity extends BaseActivity implements AuthorizationCallback {

    private EditText userName;
    private EditText password;
    private EditText url;
    private String toastText = Constants.SharedPreferenceKeys.VOID;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        url = (EditText) findViewById(R.id.jira_url);
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (InputsChecker.isVoid(userName.getText().toString())) {
                    toastText += (Constants.DialogKeys.INPUT_USERNAME + Constants.DialogKeys.NEW_LINE);
                }
                if (InputsChecker.isVoid(password.getText().toString())) {
                    toastText += (Constants.DialogKeys.INPUT_PASSWORD + Constants.DialogKeys.NEW_LINE);
                }
                if (InputsChecker.isVoid(url.getText().toString())) {
                    toastText += (Constants.DialogKeys.INPUT_URL);
                } else {
                    showProgress(true);
                    CredentialsManager.getInstance().setCredentials(userName.getText().toString(), password.getText().toString(), LoginActivity.this);
                    new AuthorizationTask(CredentialsManager.getInstance().getCredentials(LoginActivity.this), url.getText().toString(), LoginActivity.this).execute();
                    loginButton.setVisibility(View.GONE);
                }
                if (!InputsChecker.isVoid(toastText)) {
                    Toast.makeText(LoginActivity.this, toastText, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onAuthorizationResult(AuthorizationResult result) {

        String resultMessage = result == AuthorizationResult.AUTHORIZATION_DENIED ? getResources().getString(R.string.authorization_denied) :
                getResources().getString(R.string.authorization_success);
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
        showProgress(false);
        loginButton.setVisibility(View.VISIBLE);
        if (result == AuthorizationResult.AUTHORIZATION_SUCCESS) {
            CredentialsManager.getInstance().setUserName(userName.getText().toString(), LoginActivity.this);
            CredentialsManager.getInstance().setUrl(url.getText().toString(), LoginActivity.this);
            CredentialsManager.getInstance().setAccess(true, LoginActivity.this);
            finish();
        }
    }
}