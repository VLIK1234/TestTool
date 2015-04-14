package amtt.epam.com.amtt.app;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.asynctask.CreateIssueTask;
import amtt.epam.com.amtt.asynctask.ShowUserDataTask;
import amtt.epam.com.amtt.bo.issue.TypeSearchedData;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreateIssue;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreationIssueResult;
import amtt.epam.com.amtt.callbacks.CreationIssueCallback;
import amtt.epam.com.amtt.callbacks.ShowUserDataCallback;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.Converter;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.util.PreferenceUtils;


public class CreateIssueActivity extends BaseActivity implements CreationIssueCallback, ShowUserDataCallback {

    private final String TAG = this.getClass().getSimpleName();
    private EditText etDescription, etSummary;
    private ArrayList<String> projectsNames = new ArrayList<>();
    private ArrayList<String> projectsKeys = new ArrayList<>();
    private Spinner inputProjectsKey, inputIssueTypes;
    private String username, url;
    private Button createIssue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_issue);
        username = CredentialsManager.getInstance().getUserName(CreateIssueActivity.this);
        url = CredentialsManager.getInstance().getUrl(CreateIssueActivity.this);
        etDescription = (EditText) findViewById(R.id.et_description);
        etSummary = (EditText) findViewById(R.id.et_summary);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getProjectsNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputProjectsKey = (Spinner) findViewById(R.id.spin_projects_key);
        inputProjectsKey.setAdapter(adapter);
        inputProjectsKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showProgress(true);
                new ShowUserDataTask(username, url, TypeSearchedData.SEARCH_ISSUE, CreateIssueActivity.this).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        createIssue = (Button) findViewById(R.id.btn_create);
        createIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateIssue issue = new CreateIssue();
                String projectKey, issueType, description, summary;
                description = etDescription.getText().toString();
                summary = etSummary.getText().toString();
                issueType = inputIssueTypes.getSelectedItem().toString();
                projectKey = getProjectKey();
                showProgress(true);
                createIssue.setVisibility(View.GONE);
                new CreateIssueTask(url, issue.createSimpleIssue(projectKey, issueType, description, summary), CreateIssueActivity.this).execute();

            }
        });


    }

    public int getSelectedItemPositionProject() {
        return this.inputProjectsKey.getSelectedItemPosition();
    }

    public ArrayList<String> getProjectsNames() {
        projectsNames = Converter.setToArrayList(PreferenceUtils.getSet(Constants.SharedPreferenceKeys.PROJECTS_NAMES, null, CreateIssueActivity.this));
        return projectsNames;
    }

    public ArrayList<String> getProjectsKeys() {
        projectsKeys = Converter.setToArrayList(PreferenceUtils.getSet(Constants.SharedPreferenceKeys.PROJECTS_KEYS, null, CreateIssueActivity.this));
        return projectsKeys;
    }

    public String getProjectKey() {
        projectsKeys = getProjectsKeys();
        return projectsKeys.get(projectsNames.size() - ((projectsNames.indexOf(inputProjectsKey.getSelectedItem().toString())) + 1));

    }

    @Override
    public void onCreationIssueResult(CreationIssueResult result) {
        String resultMessage = result == CreationIssueResult.CREATION_UNSUCCESS ? getResources().getString(R.string.issue_creating_unsuccess) :
                getResources().getString(R.string.issue_creating_success);
        showProgress(false);
        createIssue.setVisibility(View.VISIBLE);
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
        if (resultMessage.equals(getResources().getString(R.string.issue_creating_success))) {
            finish();

        }
    }

    @Override
    public void onShowUserDataResult(JMetaResponse result) {
        int index = result.getProjects().size() - (getSelectedItemPositionProject() + 1);
        ArrayList<String> issueTypesNames = result.getProjects().get(index).getIssueTypesNames();
        ArrayAdapter<String> issueNames = new ArrayAdapter<>(CreateIssueActivity.this, android.R.layout.simple_spinner_item, issueTypesNames);
        issueNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputIssueTypes = (Spinner) findViewById(R.id.spin_issue_name);
        inputIssueTypes.setAdapter(issueNames);
        showProgress(false);
    }

}
