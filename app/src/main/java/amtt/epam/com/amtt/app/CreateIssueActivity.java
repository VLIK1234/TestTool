package amtt.epam.com.amtt.app;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.exception.ExceptionHandler;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.JiraOperationResult;
import amtt.epam.com.amtt.bo.JMetaResponse;
import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.JProjectExtVersionsResponse;
import amtt.epam.com.amtt.bo.JUserAssignableResponse;
import amtt.epam.com.amtt.bo.issue.CreateIssue;
import amtt.epam.com.amtt.processing.PriorityProcessor;
import amtt.epam.com.amtt.processing.ProjectsProcessor;
import amtt.epam.com.amtt.processing.UsersAssignableProcessor;
import amtt.epam.com.amtt.processing.VersionsProcessor;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.Converter;
import amtt.epam.com.amtt.util.PreferenceUtils;
import amtt.epam.com.amtt.util.UtilConstants;
import amtt.epam.com.amtt.view.EditText;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class CreateIssueActivity extends BaseActivity implements JiraCallback {

    private EditText mDescription;
    private EditText mSummary;
    private ArrayList<String> mProjectsNames = new ArrayList<>();
    private ArrayList<String> mProjectsKeys = new ArrayList<>();
    private Spinner mProjectName;
    private Spinner mIssueTypes;
    private Button mCreateIssue;
    private Spinner mPriority;
    private Spinner mVersions;
    private EditText mEnvironment;
    private Spinner mAssignableUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_issue);
        mDescription = (EditText) findViewById(R.id.et_description);
        mSummary = (EditText) findViewById(R.id.et_summary);
        mSummary.clearErrorOnTextChanged(true);
        mSummary.clearErrorOnFocus(true);
        mIssueTypes = (Spinner) findViewById(R.id.spin_issue_name);
        mVersions = (Spinner) findViewById(R.id.spin_affects_versions);
        mAssignableUsers = (Spinner) findViewById(R.id.spin_assignable_users);
        mPriority = (Spinner) findViewById(R.id.spin_priority);
        mEnvironment = (EditText) findViewById(R.id.et_environment);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_layout, getProjectsNames());
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mProjectName = (Spinner) findViewById(R.id.spin_projects_name);
        mProjectName.setAdapter(adapter);
        mProjectName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getMetaAsynchronously();
                getVersionsAsynchronously();
                getPriorityAsynchronously();
              //  getUsersAssignableAsynchronously();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        mCreateIssue = (Button) findViewById(R.id.btn_create);
        mCreateIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isValid = true;

                if (TextUtils.isEmpty(mSummary.getText().toString())) {
                    mSummary.setError(Constants.DialogKeys.INPUT_SUMMARY);
                    isValid = false;
                }

                if (isValid) {
                    createIssueAsynchronously();
                }
            }
        });
    }

    public int getSelectedItemPositionProject() {
        return this.mProjectName.getSelectedItemPosition();
    }

    public ArrayList<String> getProjectsNames() {
        mProjectsNames = Converter.setToArrayList(PreferenceUtils.getSet(UtilConstants.SharedPreference.PROJECTS_NAMES, null));
        return mProjectsNames;
    }

    public ArrayList<String> getProjectsKeys() {
        mProjectsKeys = Converter.setToArrayList(PreferenceUtils.getSet(UtilConstants.SharedPreference.PROJECTS_KEYS, null));
        return mProjectsKeys;
    }

    public String getProjectKey() {
        mProjectsKeys = getProjectsKeys();
        //TODO unreadable string
        //too many fields in line. Why do you get position from end of the list
        /**
         *
         * will fixed when will create extend JIRA ticket
         *
         */
        return mProjectsKeys.get(mProjectsNames.size() - ((mProjectsNames.indexOf(mProjectName.getSelectedItem().toString())) + 1));

    }

    private void createIssueAsynchronously() {
        mCreateIssue.setEnabled(false);
        showProgress(true);
        CreateIssue issue = new CreateIssue();
        String projectKey, issueType, description, summary, priority, versions, environment, assignableUserName;
        description = mDescription.getText().toString();
        summary = mSummary.getText().toString();
        issueType ="Epic";//= mIssueTypes.getSelectedItem().toString();
        priority = mPriority.getSelectedItem().toString();
        try {
            versions = mVersions.getSelectedItem().toString();
        }
        catch(Exception e){
            versions = null;
        }
        environment = mEnvironment.getText().toString();
        //assignableUserName = mAssignableUsers.getSelectedItem().toString();

        projectKey = "FOUR";//= getProjectKey();

        RestMethod<JMetaResponse> createIssue = JiraApi.getInstance().buildIssueCeating(issue.createSimpleIssue(projectKey, issueType, description, summary, priority, versions, environment));
        new JiraTask.Builder<JMetaResponse>()
            .setRestMethod(createIssue)
            .setCallback(CreateIssueActivity.this)
            .createAndExecute();
    }

    private void getMetaAsynchronously() {
        RestMethod<JMetaResponse> searchMethod = JiraApi.getInstance().buildDataSearch(JiraApiConst.USER_PROJECTS_PATH, new ProjectsProcessor());
        new JiraTask.Builder<JMetaResponse>()
            .setRestMethod(searchMethod)
            .setCallback(CreateIssueActivity.this)
            .createAndExecute();
    }


    @Override
    public void onRequestStarted() {
        showProgress(false);
        mCreateIssue.setEnabled(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onRequestPerformed(RestResponse restResponse) {
        if (restResponse.getOpeartionResult() == JiraOperationResult.ISSUE_CREATED) {
            Toast.makeText(this, R.string.issue_created, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (restResponse.getResultObject().getClass() == JMetaResponse.class) {
                JMetaResponse jMetaResponse = (JMetaResponse) restResponse.getResultObject();
                int index = jMetaResponse.getProjects().size() - (getSelectedItemPositionProject() + 1);
                ArrayList<String> issueTypesNames = jMetaResponse.getProjects().get(index).getIssueTypesNames();
                ArrayAdapter<String> issueNames = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, issueTypesNames);
                issueNames.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mIssueTypes.setAdapter(issueNames);
                // getVersionsAsynchronously();
            } else if (restResponse.getResultObject().getClass() == JProjectExtVersionsResponse.class) {
                JProjectExtVersionsResponse jProjectExtVersionsResponse = (JProjectExtVersionsResponse) restResponse.getResultObject();
                ArrayAdapter<String> jVersions = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, jProjectExtVersionsResponse.getVersionsNames());
                mVersions.setAdapter(jVersions);
            } else if (restResponse.getResultObject().getClass() == JUserAssignableResponse.class) {
                JUserAssignableResponse jUserAssignableResponse = (JUserAssignableResponse) restResponse.getResultObject();
                ArrayAdapter<String> jUsersAssignable = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, jUserAssignableResponse.getAssignableUsersNames());
                mAssignableUsers.setAdapter(jUsersAssignable);
            } else if (restResponse.getResultObject().getClass() == JPriorityResponse.class) {
                JPriorityResponse jPriorityResponse = (JPriorityResponse) restResponse.getResultObject();
                ArrayAdapter<String> jPriority = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, jPriorityResponse.getPriorityNames());
                mPriority.setAdapter(jPriority);
            }
            mCreateIssue.setEnabled(true);
        }
    }

    @Override
    public void onRequestError(AmttException e) {
        ExceptionHandler.getInstance().processError(e).showDialog(this, CreateIssueActivity.this);
    }

    private void getVersionsAsynchronously() {
        String path = JiraApiConst.PROJECT_VERSIONS_PATH + getProjectKey() + JiraApiConst.PROJECT_VERSIONS_PATH_V;
        RestMethod<JProjectExtVersionsResponse> searchMethod = JiraApi.getInstance().buildDataSearch(path, new VersionsProcessor());
        new JiraTask.Builder<JProjectExtVersionsResponse>()
            .setRestMethod(searchMethod)
            .setCallback(CreateIssueActivity.this)
            .createAndExecute();
    }

    private void getUsersAssignableAsynchronously() {
        String path = JiraApiConst.USERS_ASSIGNABLE_PATH + getProjectKey();
        RestMethod<JUserAssignableResponse> searchMethod = JiraApi.getInstance().buildDataSearch(path, new UsersAssignableProcessor());
        new JiraTask.Builder<JUserAssignableResponse>()
            .setRestMethod(searchMethod)
            .setCallback(CreateIssueActivity.this)
            .createAndExecute();
    }

    private void getPriorityAsynchronously() {
        String path = JiraApiConst.PROJECT_PRIORITY_PATH;
        RestMethod<JPriorityResponse> searchMethod = JiraApi.getInstance().buildDataSearch(path, new PriorityProcessor());
        new JiraTask.Builder<JPriorityResponse>()
            .setRestMethod(searchMethod)
            .setCallback(CreateIssueActivity.this)
            .createAndExecute();
    }
}
