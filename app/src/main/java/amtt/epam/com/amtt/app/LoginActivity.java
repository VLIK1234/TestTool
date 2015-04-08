package amtt.epam.com.amtt.app;

import amtt.epam.com.amtt.util.Constants;
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
import amtt.epam.com.amtt.util.CredentialsManager;

import java.util.Objects;

public class LoginActivity extends BaseActivity implements AuthorizationCallback {

    private EditText userName;
    private EditText password;
    private EditText url;
    private String toastText = Constants.Keys.VOID;

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
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                if((Objects.equals(userName.getText().toString(), null))||(Objects.equals(userName.getText().toString(), ""))) {
                    toastText.concat("Input username");
                    if((Objects.equals(password.getText().toString(), null))||(Objects.equals(password.getText().toString(), ""))){
                        toastText.concat( "Input password\n");
                        if((!Objects.equals(url.getText().toString(), null))&&(!Objects.equals(url.getText().toString(), ""))){
                            showProgress(true);
                            CredentialsManager.getInstance().setCredentials(userName.getText().toString(), password.getText().toString(), LoginActivity.this);
                            new AuthorizationTask(CredentialsManager.getInstance().getCredentials(LoginActivity.this), url.getText().toString(), LoginActivity.this).execute();

                        }
                        else{
                            toastText += "Input URL\n";
                        }

                    }
                    else{
                       
                    }

                }
                else{

                }

                if(!Objects.equals(toastText, Constants.Keys.VOID)){
                    Toast.makeText(LoginActivity.this, toastText, Toast.LENGTH_LONG );
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
        if (result == AuthorizationResult.AUTHORIZATION_SUCCESS) {
            CredentialsManager.getInstance().setUserName(userName.getText().toString(), LoginActivity.this);
            //  CredentialsManager.getInstance().setCredentials(userName.getText().toString(), password.getText().toString(), LoginActivity.this);
            CredentialsManager.getInstance().setUrl(url.getText().toString(), LoginActivity.this);
            CredentialsManager.getInstance().setAccess(true, LoginActivity.this);
            finish();
        }
    }
}