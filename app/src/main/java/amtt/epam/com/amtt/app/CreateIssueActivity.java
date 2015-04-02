package amtt.epam.com.amtt.app;

import amtt.epam.com.amtt.asynctask.CreateIssueTask;
import amtt.epam.com.amtt.asynctask.ShowUserDataTask;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.util.CreateMetaUtil;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreateIssue;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreationIssueResult;
import amtt.epam.com.amtt.callbacks.CreationIssueCallback;
import amtt.epam.com.amtt.callbacks.ShowUserDataCallback;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import amtt.epam.com.amtt.R;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class CreateIssueActivity extends ActionBarActivity implements CreationIssueCallback, ShowUserDataCallback {

    private EditText etDescription, etSummary;
    private ArrayList<String> projectsNames = new ArrayList<String>();
    private ArrayList<String> issueTypesNames = new ArrayList<String>();
    private ArrayList<String> issueTypesKeys = new ArrayList<String>();
    private Spinner etProjectKey, etIssyeType;
    private ArrayAdapter<String> adapter3;
    private SharedPreferences sharedPreferences;
    private JMetaResponse metaResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_issue);
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        Set<String> pNames = new HashSet();
        pNames = sharedPreferences.getStringSet("projectsNames", null);
        if (pNames != null) {
            for (String name : pNames) {

                projectsNames.add(name);

            }
            Log.i("CreateIssueActivitySIZE", String.valueOf(projectsNames.size()));
        }

        etDescription = (EditText) findViewById(R.id.et_description);
        etSummary = (EditText) findViewById(R.id.et_summary);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, projectsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etProjectKey = (Spinner) findViewById(R.id.et_projectkey);
        etProjectKey.setAdapter(adapter);
        etProjectKey.setPrompt("Project");
        etProjectKey.setSelection(0);
        etProjectKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                String username, password, url;
                username = sharedPreferences.getString("username", "");
                password = sharedPreferences.getString("password", "");
                url = sharedPreferences.getString("url", "");
                new ShowUserDataTask(username, password, url, CreateIssueActivity.this).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


    }

    public int getSelectedItemPositionProject() {
        return this.etProjectKey.getSelectedItemPosition();
    }


    public void onCreateIssueClick(View view) {
        CreateIssue issue = new CreateIssue();
        String mProjectKey, mIssyeType, mDescription, mSummary;
        mDescription = etDescription.getText().toString();
        mSummary = etSummary.getText().toString();
        mIssyeType = etIssyeType.getSelectedItem().toString();
        mProjectKey = etProjectKey.getSelectedItem().toString();
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String username, password, url;
        username = sharedPreferences.getString("username", "");
        password = sharedPreferences.getString("password", "");
        url = sharedPreferences.getString("url", "");
        new CreateIssueTask(username, password, url, issue.createSimpleIssue(mProjectKey, mIssyeType, mDescription, mSummary), CreateIssueActivity.this).execute();

    }

    @Override
    public void onCreationIssueResult(CreationIssueResult result) {
        String resultMessage = result == CreationIssueResult.CREATION_UNSUCCESS ? getResources().getString(R.string.issue_creating_unsuccess) :
            getResources().getString(R.string.issue_creating_success);
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowUserDataResult(JMetaResponse result) {

        CreateMetaUtil createMetaUtil = new CreateMetaUtil();

        projectsNames = createMetaUtil.getProjectsNames(result);
        issueTypesNames = createMetaUtil.getIssueTypesNames(result.getProjects().get(getSelectedItemPositionProject()));
        adapter3 = new ArrayAdapter<String>(CreateIssueActivity.this, android.R.layout.simple_spinner_item, issueTypesNames);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etIssyeType = (Spinner) findViewById(R.id.et_issue_name);
        etIssyeType.setAdapter(adapter3);
        etIssyeType.setPrompt("Issue");
        etIssyeType.setSelection(0);
    }
}
