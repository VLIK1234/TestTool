package amtt.epam.com.amtt;

import amtt.epam.com.amtt.authorization.AuthorizationCallback;
import amtt.epam.com.amtt.authorization.AuthorizationResult;
import amtt.epam.com.amtt.authorization.AuthorizationTask;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity implements AuthorizationCallback {

    private SharedPreferences sharedPreferences;
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

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AuthorizationTask(LoginActivity.this, userName.getText().toString(), password.getText().toString(), url.getText().toString(), LoginActivity.this).execute();
            }
        });
    }

    @Override
    public void onAuthorizationResult(AuthorizationResult result) {

        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();


        ed.putString("username", userName.getText().toString());
        ed.putString("password", password.getText().toString());
        ed.putString("url", url.getText().toString());
        ed.commit();

        String resultMessage = result == AuthorizationResult.AUTHORIZATION_DENIED ? getResources().getString(R.string.authorization_denied) :
            getResources().getString(R.string.authorization_success);
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }
}
