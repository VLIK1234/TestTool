package amtt.epam.com.amtt;

import amtt.epam.com.amtt.authorization.AuthorizationCallback;
import amtt.epam.com.amtt.authorization.AuthorizationResult;
import amtt.epam.com.amtt.authorization.AuthorizationTask;
import amtt.epam.com.amtt.authorization.JiraApi;
import amtt.epam.com.amtt.bo.CreateIssueTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import com.crashlytics.android.Crashlytics;

import amtt.epam.com.amtt.bo.CreateIssue;
import io.fabric.sdk.android.Fabric;


public class TestIssueActivity extends ActionBarActivity implements AuthorizationCallback {

    private TextView testIssue;
    private EditText etUsername, etPassword, etProjectKey, etIssyeType, etDescription, etSummary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_test_issue);
        testIssue = (TextView)findViewById(R.id.test_issue);
        etUsername = (EditText)findViewById(R.id.et_username);
        etPassword = (EditText)findViewById(R.id.et_password);
        etProjectKey = (EditText)findViewById(R.id.et_projectkey);
        etIssyeType = (EditText)findViewById(R.id.et_issue_name);
        etDescription = (EditText)findViewById(R.id.et_description);
        etSummary = (EditText)findViewById(R.id.et_summary);
    }


    public void onCreateIssueClick(View view) {
        CreateIssue issue = new CreateIssue();
        //etUsername.getText().toString(), etPassword.getText().toString(),
        new AuthorizationTask(TestIssueActivity.this, "", "", TestIssueActivity.this).execute();


        new CreateIssueTask(TestIssueActivity.this, issue.createSimpleIssue(), TestIssueActivity.this).execute();
      //  testIssue.setText(res);
    }

    @Override
    public void onAuthorizationResult(AuthorizationResult result) {
        String resultMessage = result == AuthorizationResult.AUTHORIZATION_DENIED ? getResources().getString(R.string.authorization_denied) :
            getResources().getString(R.string.authorization_success);
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }
}
