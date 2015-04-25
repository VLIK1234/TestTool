package amtt.epam.com.amtt.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.JiraTask.JiraTaskType;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.JiraOperationResult;
import amtt.epam.com.amtt.service.TopButtonService;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.util.Logger;
import amtt.epam.com.amtt.util.UtilConstants;

public class LoginActivity extends BaseActivity implements JiraCallback<String> {

    private EditText userName;
    private EditText password;
    private EditText url;
    private String toastText = UtilConstants.SharedPreference.EMPTY_STRING;
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
        userName.setText("artsiom_kaliaha");

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userName.getText().toString())) {
                    toastText += (UtilConstants.Dialog.INPUT_USERNAME + UtilConstants.Dialog.NEW_LINE);
                }
                if (TextUtils.isEmpty(password.getText().toString())) {
                    toastText += (UtilConstants.Dialog.INPUT_PASSWORD + UtilConstants.Dialog.NEW_LINE);
                }
                if (TextUtils.isEmpty(url.getText().toString())) {
                    toastText += (UtilConstants.Dialog.INPUT_URL);
                } else {
                    Logger.d(TAG, String.valueOf(TextUtils.isEmpty(userName.getText().toString())));
                    Logger.d(TAG, String.valueOf(TextUtils.isEmpty(password.getText().toString())));
                    Logger.d(TAG, String.valueOf(TextUtils.isEmpty(url.getText().toString())));


                    showProgress(true);
                    CredentialsManager.getInstance().setUrl(url.getText().toString());
                    CredentialsManager.getInstance().setCredentials(userName.getText().toString(), password.getText().toString());
                    new JiraTask.Builder<Void>()
                            .setOperationType(JiraTaskType.AUTH)
                            .setCallback(LoginActivity.this)
                            .create()
                            .execute();

                    loginButton.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(toastText)) {
                    Toast.makeText(LoginActivity.this, toastText, Toast.LENGTH_LONG).show();
                }
                toastText = "";
            }
        });
    }

    @Override
    public void onJiraRequestPerformed(RestResponse<String> restResponse) {
        Toast.makeText(this, restResponse.getResultObject(), Toast.LENGTH_SHORT).show();
        showProgress(false);
        loginButton.setVisibility(View.VISIBLE);
        if (restResponse.getOpeartionResult() == JiraOperationResult.PERFORMED) {
            CredentialsManager.getInstance().setUrl(url.getText().toString());
            CredentialsManager.getInstance().setCredentials(userName.getText().toString(), password.getText().toString());
            CredentialsManager.getInstance().setAccess(true);
            TopButtonService.authSuccess(this);
            finish();
        }
    }

}