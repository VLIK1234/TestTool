package amtt.epam.com.amtt.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.authorization.AuthorizationCallback;
import amtt.epam.com.amtt.authorization.AuthorizationResult;
import amtt.epam.com.amtt.authorization.AuthorizationTask;

public class LoginActivity extends ActionBarActivity implements AuthorizationCallback {

    private EditText userName;
    private EditText password;
    private EditText url;
    //TODO dublicates
    private static final String USER_NAME = "username";
    private static final String PASSWORD = "password";
    private static final String URL = "url";
    private static final String NAME_SP = "data";
    private static final String ACCESS = "access";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        url = (EditText) findViewById(R.id.jira_url);
        //TODO hardcoded
        url.setText("https://jiraprojectthree.atlassian.net");
        //TODO check inputs before login
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibleProgress();
                new AuthorizationTask(userName.getText().toString(), password.getText().toString(), url.getText().toString(), LoginActivity.this).execute();

            }
        });
    }

    @Override
    public void onAuthorizationResult(String responseString, Exception e) {
        Toast.makeText(this, responseString, Toast.LENGTH_SHORT).show();
//        //TODO check "result == AuthorizationResult.AUTHORIZATION_SUCCESS" is better, no?
//        if (resultMessage.equals(getResources().getString(R.string.authorization_success))) {
//            SharedPreferences sharedPreferences = getSharedPreferences(NAME_SP, MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString(USER_NAME, userName.getText().toString());
//            //TODO we store password?
//            editor.putString(PASSWORD, password.getText().toString());
//            editor.putString(URL, url.getText().toString());
//            editor.putBoolean(ACCESS, true);
//            editor.apply();
//            //TODO misprint
//            setInisibleProgress();
//            finish();
//        }
    }

    //TODO why not to move to common base activity?
    private void setVisibleProgress() {
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
    }

    private void setInisibleProgress() {
        findViewById(R.id.progress).setVisibility(View.GONE);
    }
}