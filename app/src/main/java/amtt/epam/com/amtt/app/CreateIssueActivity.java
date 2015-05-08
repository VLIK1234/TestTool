package amtt.epam.com.amtt.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.exception.ExceptionHandler;
import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.JiraOperationResult;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreateIssue;
import amtt.epam.com.amtt.processing.ProjectsProcessor;
import amtt.epam.com.amtt.util.Converter;
import amtt.epam.com.amtt.util.PreferenceUtils;
import amtt.epam.com.amtt.view.EditText;
import amtt.epam.com.amtt.util.Constants;


@SuppressWarnings("unchecked")
public class CreateIssueActivity extends BaseActivity implements JiraCallback<JMetaResponse> {

    private EditText etDescription, etSummary;
    private ArrayList<String> projectsNames = new ArrayList<>();
    private ArrayList<String> projectsKeys = new ArrayList<>();
    private Spinner spinnerProjectsKey, spinnerIssueTypes;
    private Button buttonCreateIssue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_issue);
        etDescription = (EditText) findViewById(R.id.et_description);
        etSummary = (EditText) findViewById(R.id.et_summary);
        etSummary.clearErrorOnTextChanged(true);
        etSummary.clearErrorOnFocus(true);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, getProjectsNames());
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerProjectsKey = (Spinner) findViewById(R.id.spin_projects_name);
        spinnerProjectsKey.setAdapter(adapter);
        spinnerProjectsKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getMetaAsynchronously();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        buttonCreateIssue = (Button) findViewById(R.id.btn_create);
        buttonCreateIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isValid = true;

                if (TextUtils.isEmpty(etSummary.getText().toString())) {
                    etSummary.setError("");
                    isValid = false;
                }

                if (isValid) {

                    createIssueAsynchronously();
                }
            }
        });
    }

    public int getSelectedItemPositionProject() {
        return this.spinnerProjectsKey.getSelectedItemPosition();
    }

    public ArrayList<String> getProjectsNames() {
        projectsNames = Converter.setToArrayList(PreferenceUtils.getSet(Constants.SharedPreference.PROJECTS_NAMES, null));
        return projectsNames;
    }

    public ArrayList<String> getProjectsKeys() {
        projectsKeys = Converter.setToArrayList(PreferenceUtils.getSet(Constants.SharedPreference.PROJECTS_KEYS, null));
        return projectsKeys;
    }

    public String getProjectKey() {
        projectsKeys = getProjectsKeys();
        //TODO unreadable string
        //too many fields in line. Why do you get position from end of the list
        /**
        *
        * will fixed when will create extend JIRA ticket
        *
        */
        return projectsKeys.get(projectsNames.size() - ((projectsNames.indexOf(spinnerProjectsKey.getSelectedItem().toString())) + 1));

    }


    private void createIssueAsynchronously() {
        buttonCreateIssue.setEnabled(false);
        showProgress(true);
        CreateIssue issue = new CreateIssue();
        String projectKey, issueType, description, summary;
        description = etDescription.getText().toString();
        summary = etSummary.getText().toString();
        issueType = spinnerIssueTypes.getSelectedItem().toString();
        projectKey = getProjectKey();

        RestMethod<JMetaResponse> createIssue = JiraApi.getInstance().buildIssueCreating(issue.createSimpleIssue(projectKey, issueType, description, summary));
        new JiraTask.Builder<JMetaResponse>()
                .setRestMethod(createIssue)
                .setCallback(CreateIssueActivity.this)
                .createAndExecute();
    }

    private void getMetaAsynchronously() {
        RestMethod<JMetaResponse> searchMethod = JiraApi.getInstance().buildDataSearch(JiraApiConst.USER_PROJECTS_PATH, new ProjectsProcessor(), null, null,null);
        new JiraTask.Builder<JMetaResponse>()
                .setRestMethod(searchMethod)
                .setCallback(CreateIssueActivity.this)
                .createAndExecute();
    }


    @Override
    public void onRequestStarted() {
        showProgress(false);
        buttonCreateIssue.setEnabled(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onRequestPerformed(RestResponse<JMetaResponse> restResponse) {
        if (restResponse.getOpeartionResult() == JiraOperationResult.ISSUE_CREATED) {
            Toast.makeText(this, R.string.issue_created, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            JMetaResponse jMetaResponse = restResponse.getResultObject();
            int index = jMetaResponse.getProjects().size() - (getSelectedItemPositionProject() + 1);
            ArrayList<String> issueTypesNames = jMetaResponse.getProjects().get(index).getIssueTypesNames();
            ArrayAdapter<String> issueNames = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, issueTypesNames);
            issueNames.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerIssueTypes = (Spinner) findViewById(R.id.spin_issue_name);
            spinnerIssueTypes.setAdapter(issueNames);

        }
        buttonCreateIssue.setEnabled(true);
    }

    @Override
    public void onRequestError(AmttException e) {
        ExceptionHandler.getInstance().processError(e).showDialog(this, CreateIssueActivity.this);
    }
}
