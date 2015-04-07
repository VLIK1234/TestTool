package amtt.epam.com.amtt.app;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.authorization.AuthorizationCallback;
import amtt.epam.com.amtt.authorization.AuthorizationResult;
import amtt.epam.com.amtt.authorization.AuthorizationTask;
import amtt.epam.com.amtt.util.Constants;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                new AuthorizationTask(LoginActivity.this, userName.getText().toString(), password.getText().toString(), url.getText().toString(), LoginActivity.this).execute();

            }
        });
    }

    @Override
    public void onAuthorizationResult(AuthorizationResult result) {

        String resultMessage = result == AuthorizationResult.AUTHORIZATION_DENIED ? getResources().getString(R.string.authorization_denied) :
            getResources().getString(R.string.authorization_success);
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
        showProgress(false, R.id.progress);
        //TODO check "result == AuthorizationResult.AUTHORIZATION_SUCCESS" is better, no?
        if (resultMessage.equals(getResources().getString(R.string.authorization_success))) {
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.NAME_SP, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.USER_NAME, userName.getText().toString());
            //TODO we store password?
            editor.putString(Constants.PASSWORD, password.getText().toString());
            editor.putString(Constants.URL, url.getText().toString());
            editor.putBoolean(Constants.ACCESS, true);
            editor.apply();
            finish();
        }
    }
}