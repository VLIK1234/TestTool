package amtt.epam.com.amtt.app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.JiraTask.JiraSearchType;
import amtt.epam.com.amtt.api.JiraTask.JiraTaskType;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreateIssue;
import amtt.epam.com.amtt.api.result.CreateIssueResult;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.Converter;
import amtt.epam.com.amtt.util.PreferenceUtils;
import amtt.epam.com.amtt.view.EditText;


public class CreateIssueActivity extends BaseActivity implements JiraCallback {

    private EditText etDescription, etSummary;
    private ArrayList<String> projectsNames = new ArrayList<>();
    private ArrayList<String> projectsKeys = new ArrayList<>();
    private Spinner spinnerProjectsKey, spinnerIssueTypes;
    private Button buttonCreateIssue;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_issue);
        etDescription = (EditText) findViewById(R.id.et_description);
        etSummary = (EditText) findViewById(R.id.et_summary);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, getProjectsNames());
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerProjectsKey = (Spinner) findViewById(R.id.spin_projects_name);
        spinnerProjectsKey.setAdapter(adapter);
        spinnerProjectsKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showProgress(true);
                new JiraTask.Builder<>()
                        .setOperationType(JiraTaskType.SEARCH)
                        .setSearchType(JiraSearchType.ISSUE)
                        .setCallback(CreateIssueActivity.this)
                        .create()
                        .execute();
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
                    etSummary.setError(Constants.DialogKeys.INPUT_SUMMARY);
                    isValid = false;
                }

                if(isValid) {

                    buttonCreateIssue.setEnabled(false);
                    String projectKey, issueType, description, summary;
                    description = etDescription.getText().toString();
                    summary = etSummary.getText().toString();
                    issueType = spinnerIssueTypes.getSelectedItem().toString();
                    projectKey = getProjectKey();
                    showProgress(true);
                    CreateIssue issue = new CreateIssue();
                    new JiraTask.Builder<CreateIssueResult, Void>()
                            .setOperationType(JiraTaskType.CREATE_ISSUE)
                            .setCallback(CreateIssueActivity.this)
                            .setJson(issue.createSimpleIssue(projectKey, issueType, description, summary))
                            .create()
                            .execute();
                }
            }

        });

        etSummary.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                etSummary.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    public int getSelectedItemPositionProject() {
        return this.spinnerProjectsKey.getSelectedItemPosition();
    }

    public ArrayList<String> getProjectsNames() {
        projectsNames = Converter.setToArrayList(PreferenceUtils.getSet(Constants.SharedPreferenceKeys.PROJECTS_NAMES, null));
        return projectsNames;
    }

    public ArrayList<String> getProjectsKeys() {
        projectsKeys = Converter.setToArrayList(PreferenceUtils.getSet(Constants.SharedPreferenceKeys.PROJECTS_KEYS, null));
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


    @Override
    @SuppressWarnings("unchecked")
    public void onJiraRequestPerformed(RestResponse restResponse) {
        if (restResponse.getResult() instanceof CreateIssueResult) {
            Toast.makeText(this, restResponse.getMessage(), Toast.LENGTH_SHORT).show();
            if (restResponse.getResult() == CreateIssueResult.SUCCESS) {
                finish();
            }
            buttonCreateIssue.setEnabled(true);
        } else {
            if (restResponse.getResultObject() == null) {
                Toast.makeText(this, restResponse.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            JMetaResponse jMetaResponse = ((RestResponse<Void, JMetaResponse>) restResponse).getResultObject();
            int index = jMetaResponse.getProjects().size() - (getSelectedItemPositionProject() + 1);
            ArrayList<String> issueTypesNames = jMetaResponse.getProjects().get(index).getIssueTypesNames();
            ArrayAdapter<String> issueNames = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, issueTypesNames);
            issueNames.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinnerIssueTypes = (Spinner) findViewById(R.id.spin_issue_name);
            spinnerIssueTypes.setAdapter(issueNames);
            buttonCreateIssue.setEnabled(true);
        }
        showProgress(false);

    }

}
