package amtt.epam.com.amtt;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import amtt.epam.com.amtt.bo.CreateIssue;
import amtt.epam.com.amtt.bo.CreateIssueTask;
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
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        etProjectKey = (EditText) findViewById(R.id.et_projectkey);
        etIssyeType = (EditText) findViewById(R.id.et_issue_name);
        etDescription = (EditText) findViewById(R.id.et_description);
        etSummary = (EditText) findViewById(R.id.et_summary);
    }


    public void onCreateIssueClick(View view) {
        CreateIssue issue = new CreateIssue();
        String mProjectKey, mIssyeType, mDescription, mSummary;
        mProjectKey = etProjectKey.getText().toString();
        mIssyeType = etIssyeType.getText().toString();
        mDescription = etDescription.getText().toString();
        mSummary = etSummary.getText().toString();
        new CreateIssueTask(etUsername.getText().toString(), etPassword.getText().toString(), issue.createSimpleIssue(mProjectKey, mIssyeType, mDescription, mSummary), TestIssueActivity.this).execute();

    }

    @Override
    public void onCreationIssueResult(CreationIssueResult result) {
        String resultMessage = result == CreationIssueResult.CREATION_UNSUCCESS ? getResources().getString(R.string.issue_creating_unsuccess) :
                getResources().getString(R.string.issue_creating_success);
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }
}
