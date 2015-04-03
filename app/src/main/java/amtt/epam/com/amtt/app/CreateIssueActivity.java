package amtt.epam.com.amtt.app;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.asynctask.CreateIssueTask;
import amtt.epam.com.amtt.asynctask.ShowUserDataTask;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.util.CreateMetaObjectsHelper;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreateIssue;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreationIssueResult;
import amtt.epam.com.amtt.callbacks.CreationIssueCallback;
import amtt.epam.com.amtt.callbacks.ShowUserDataCallback;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Set;


public class CreateIssueActivity extends ActionBarActivity implements CreationIssueCallback, ShowUserDataCallback {

    private EditText etDescription, etSummary;
    private ArrayList<String> projectsNames = new ArrayList<>();
    private Spinner inputProjectKey, inputIssueType;
    private SharedPreferences sharedPreferences;
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "password";
    public static final String URL = "url";
    public static final String NAME_SP = "data";
    public static final String VOID = "";
    public static final String PROJECT_NAMES ="projectsNames";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_issue);
        sharedPreferences = getSharedPreferences(NAME_SP, MODE_PRIVATE);
        //todo move to method getProjectNames and use in proper adapter
        Set<String> pNames;
        pNames = sharedPreferences.getStringSet(PROJECT_NAMES, null);
        if (pNames != null) {
            for (String name : pNames) {
                projectsNames.add(name);
            }
        }
        etDescription = (EditText) findViewById(R.id.et_description);
        etSummary = (EditText) findViewById(R.id.et_summary);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, projectsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputProjectKey = (Spinner) findViewById(R.id.spin_project_key);
        inputProjectKey.setAdapter(adapter);
        inputProjectKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String username, password, url;
                username = sharedPreferences.getString(USER_NAME, VOID);
                password = sharedPreferences.getString(PASSWORD, VOID);
                url = sharedPreferences.getString(URL, VOID);
                new ShowUserDataTask(username, password, url, CreateIssueActivity.this).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


    }

    public int getSelectedItemPositionProject() {
        return this.inputProjectKey.getSelectedItemPosition();
    }


    public void onCreateIssueClick(View view) {
        CreateIssue issue = new CreateIssue();
        String mProjectKey, mIssueType, mDescription, mSummary;
        mDescription = etDescription.getText().toString();
        mSummary = etSummary.getText().toString();
        mIssueType = inputIssueType.getSelectedItem().toString();
        mProjectKey = inputProjectKey.getSelectedItem().toString();
        String username, password, url;
        username = sharedPreferences.getString(USER_NAME, VOID);
        password = sharedPreferences.getString(PASSWORD, VOID);
        url = sharedPreferences.getString(URL, VOID);
        new CreateIssueTask(username, password, url, issue.createSimpleIssue(mProjectKey, mIssueType, mDescription, mSummary), CreateIssueActivity.this).execute();

    }

    @Override
    public void onCreationIssueResult(CreationIssueResult result) {
        String resultMessage = result == CreationIssueResult.CREATION_UNSUCCESS ? getResources().getString(R.string.issue_creating_unsuccess) :
            getResources().getString(R.string.issue_creating_success);
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
        if(resultMessage.equals(getResources().getString(R.string.issue_creating_success))){
            finish();
        }
    }

    @Override
    public void onShowUserDataResult(JMetaResponse result) {

        //todo why util? POJO class should do this.
        //for example: result.getProjectNames()
        projectsNames = CreateMetaObjectsHelper.getProjectsNames(result);
        ArrayList<String> issueTypesNames = CreateMetaObjectsHelper.getIssueTypesNames(result.getProjects().get(getSelectedItemPositionProject()));
        ArrayAdapter<String> issueNames = new ArrayAdapter<>(CreateIssueActivity.this, android.R.layout.simple_spinner_item, issueTypesNames);
        issueNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputIssueType = (Spinner) findViewById(R.id.spin_issue_name);
        inputIssueType.setAdapter(issueNames);
    }
}
