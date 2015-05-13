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
import amtt.epam.com.amtt.api.IContentSuccess;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraContent;
import amtt.epam.com.amtt.api.JiraContentTypesConst;
import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.exception.ExceptionHandler;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.JiraOperationResult;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.view.EditText;

@SuppressWarnings("unchecked")
public class CreateIssueActivity extends BaseActivity implements JiraCallback, IContentSuccess{

    private EditText mDescription;
    private EditText mSummary;
    private Spinner mProjectName;
    private Spinner mIssueTypes;
    private Button mCreateIssue;
    private Spinner mPriorities;
    private Spinner mVersions;
    private EditText mEnvironment;
    private Spinner mAssignableUsers;
    private final String TAG = this.getClass().getSimpleName();
    private ArrayAdapter<String> mProjectsAdapter;
    private ArrayAdapter<String> mIssueTypesAdapter;
    private ArrayAdapter<String> mVersionsAdapter;
    private ArrayAdapter<String> mPrioritiesAdapter;
    private String mIssueTypeName;
    private String mPriorityName;
    private String mVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_issue);
        mCreateIssue = (Button) findViewById(R.id.btn_create);
        mEnvironment = (EditText) findViewById(R.id.et_environment);
        mDescription = (EditText) findViewById(R.id.et_description);
        mSummary = (EditText) findViewById(R.id.et_summary);
        mSummary.clearErrorOnTextChanged(true);
        mSummary.clearErrorOnFocus(true);
        mAssignableUsers = (Spinner) findViewById(R.id.spin_assignable_users);

        mIssueTypes = (Spinner) findViewById(R.id.spin_issue_name);
        mIssueTypes.setEnabled(false);
        mIssueTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIssueTypeName = mIssueTypesAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        mVersions = (Spinner) findViewById(R.id.spin_affects_versions);
        mVersions.setEnabled(false);
        mVersions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mVersionName = mVersionsAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        mPriorities = (Spinner) findViewById(R.id.spin_priority);
        mPriorities.setEnabled(false);
        mPriorities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPriorityName = mPrioritiesAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        mCreateIssue.setEnabled(false);
        showProgress(true);
        JiraContent.getInstance().getPrioritiesNames(CreateIssueActivity.this);

        mProjectName = (Spinner) findViewById(R.id.spin_projects_name);
        mProjectName.setEnabled(false);
        showProgress(true);
        mCreateIssue.setEnabled(false);
        JiraContent.getInstance().getProjectsNames(CreateIssueActivity.this);
        mProjectName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showProgress(true);
                mCreateIssue.setEnabled(false);
                JiraContent.getInstance().getProjectKeyByName(mProjectsAdapter.getItem(position).toString(), CreateIssueActivity.this);
                showProgress(true);
                mCreateIssue.setEnabled(false);
                mIssueTypes.setEnabled(false);
                JiraContent.getInstance().getIssueTypesNames(mProjectsAdapter.getItem(position).toString(), CreateIssueActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        mCreateIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isValid = true;

                if (TextUtils.isEmpty(mSummary.getText().toString())) {
                    mSummary.setError(Constants.DialogKeys.INPUT_SUMMARY);
                    isValid = false;
                }

                if (isValid) {
                    JiraContent.getInstance().createIssueAsynchronously(getIssueTypeName(),
                            getPriorityName(), getVersionName(), mSummary.getText().toString(),
                            mDescription.getText().toString(), mEnvironment.getText().toString(),
                            CreateIssueActivity.this);
                }
            }
        });
    }

    public String getIssueTypeName() {
        return mIssueTypeName;
    }

    public String getPriorityName() {
        return mPriorityName;
    }

    public String getVersionName() {
        return mVersionName;
    }

    @Override
    public void onRequestStarted() {
        showProgress(true);
        mCreateIssue.setEnabled(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onRequestPerformed(RestResponse restResponse) {
        if (restResponse.getOpeartionResult() == JiraOperationResult.ISSUE_CREATED) {
            Toast.makeText(this, R.string.issue_created, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            /** if (restResponse.getResultObject().getClass() == JUserAssignableResponse.class) {
             JUserAssignableResponse jUserAssignableResponse = (JUserAssignableResponse) restResponse.getResultObject();
             ArrayAdapter<String> jUsersAssignable = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, jUserAssignableResponse.getAssignableUsersNames());
             jUsersAssignable.setDropDownViewResource(R.layout.spinner_dropdown_item);
             mAssignableUsers.setAdapter(jUsersAssignable); */
        }
        showProgress(false);
        mCreateIssue.setEnabled(true);
    }


    @Override
    public void onRequestError(AmttException e) {
        showProgress(false);
        ExceptionHandler.getInstance().processError(e).showDialog(this, CreateIssueActivity.this);
    }

    @Override
    public void success(Object result, JiraContentTypesConst tagResult) {
        if (tagResult == JiraContentTypesConst.PROJECTS_NAMES) {
            mProjectsAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, (ArrayList<String>) result);
            mProjectsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mProjectName.setAdapter(mProjectsAdapter);
            mProjectName.setEnabled(true);
            mProjectName.setSelection(0);
            showProgress(true);
            mCreateIssue.setEnabled(false);
            JiraContent.getInstance().getPrioritiesNames(CreateIssueActivity.this);
        } else if (tagResult == JiraContentTypesConst.PROJECT_KEY_BY_NAME) {
            showProgress(true);
            mCreateIssue.setEnabled(false);
            mVersions.setEnabled(false);
            JiraContent.getInstance().getVersionsNames((String) result, CreateIssueActivity.this);
        } else if (tagResult == JiraContentTypesConst.ISSUE_TYPES_NAMES) {
            mIssueTypesAdapter = new ArrayAdapter<String>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
            mIssueTypesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mIssueTypes.setAdapter(mIssueTypesAdapter);
            mIssueTypes.setEnabled(true);
            mIssueTypes.setSelection(0);
            mCreateIssue.setEnabled(true);
            showProgress(false);
        } else if (tagResult == JiraContentTypesConst.VERSIONS_NAMES) {
            if ((ArrayList<String>) result !=null){
            mVersionsAdapter = new ArrayAdapter<String>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
            mVersionsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mVersions.setAdapter(mVersionsAdapter);
            mVersions.setSelection(0);
            }
            showProgress(false);
            mCreateIssue.setEnabled(true);
            mVersions.setEnabled(true);

        } else if (tagResult == JiraContentTypesConst.PRIORITIES_NAMES) {
            mPrioritiesAdapter = new ArrayAdapter<String>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
            mPrioritiesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mPriorities.setAdapter(mPrioritiesAdapter);
            mPriorities.setEnabled(true);
            showProgress(false);
            mCreateIssue.setEnabled(true);
            mPriorities.setSelection(2);
        }
    }

}
