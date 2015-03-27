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
import amtt.epam.com.amtt.bo.CreationIssueCallback;
import amtt.epam.com.amtt.bo.CreationIssueResult;
import io.fabric.sdk.android.Fabric;


public class TestIssueActivity extends ActionBarActivity implements CreationIssueCallback {

    private EditText etUsername, etPassword, etProjectKey, etIssyeType, etDescription, etSummary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_test_issue);
        etUsername = (EditText)findViewById(R.id.et_username);
        etPassword = (EditText)findViewById(R.id.et_password);
        etProjectKey = (EditText)findViewById(R.id.et_projectkey);
        etIssyeType = (EditText)findViewById(R.id.et_issue_name);
        etDescription = (EditText)findViewById(R.id.et_description);
        etSummary = (EditText)findViewById(R.id.et_summary);
    }


    public void onCreateIssueClick(View view) {
        CreateIssue issue = new CreateIssue();
        String mProjectKey, mIssyeType, mDescription, mSummary;
        mProjectKey = etProjectKey.getText().toString();
        mIssyeType =  etIssyeType.getText().toString();
        mDescription = etDescription.getText().toString();
        mSummary = etSummary.getText().toString();
        new CreateIssueTask(etUsername.getText().toString(), etPassword.getText().toString(),issue.createSimpleIssue(mProjectKey, mIssyeType, mDescription, mSummary), TestIssueActivity.this).execute();

    }

    @Override
    public void onCreationIssueResult(CreationIssueResult result) {
        String resultMessage = result == CreationIssueResult.CREATION_UNSUCCESS ? getResources().getString(R.string.authorization_denied) :
            getResources().getString(R.string.authorization_success);
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }
}
