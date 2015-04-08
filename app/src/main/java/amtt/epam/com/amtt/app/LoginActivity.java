package amtt.epam.com.amtt.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.authorization.AuthorizationCallback;
import amtt.epam.com.amtt.authorization.AuthorizationResult;
import amtt.epam.com.amtt.authorization.AuthorizationTask;
import amtt.epam.com.amtt.util.CredentialsManager;

public class LoginActivity extends BaseActivity implements AuthorizationCallback {

    private EditText userName;
    private EditText password;
    private EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        url = (EditText) findViewById(R.id.jira_url);
        //TODO check inputs before login
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true, R.id.progress);
                CredentialsManager.getInstance().setCredentials(userName.getText().toString(), password.getText().toString(), LoginActivity.this);
                new AuthorizationTask(CredentialsManager.getInstance().getCredentials(LoginActivity.this), url.getText().toString(), LoginActivity.this).execute();

            }
        });
    }

    @Override
    public void onAuthorizationResult(AuthorizationResult result) {

        String resultMessage = result == AuthorizationResult.AUTHORIZATION_DENIED ? getResources().getString(R.string.authorization_denied) :
                getResources().getString(R.string.authorization_success);
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
        showProgress(false, R.id.progress);
        if (result == AuthorizationResult.AUTHORIZATION_SUCCESS) {
            CredentialsManager.getInstance().setUserName(userName.getText().toString(), LoginActivity.this);
          //  CredentialsManager.getInstance().setCredentials(userName.getText().toString(), password.getText().toString(), LoginActivity.this);
            CredentialsManager.getInstance().setUrl(url.getText().toString(), LoginActivity.this);
            CredentialsManager.getInstance().setAccess(true, LoginActivity.this);
            finish();
        }
    }
}