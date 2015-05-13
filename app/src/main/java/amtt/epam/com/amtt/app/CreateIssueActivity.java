package amtt.epam.com.amtt.app;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraContent;
import amtt.epam.com.amtt.api.JiraContentCallback;
import amtt.epam.com.amtt.api.JiraContentConst;
import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.exception.ExceptionHandler;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.JiraOperationResult;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.view.EditText;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class CreateIssueActivity extends BaseActivity implements JiraContentCallback {

    private EditText mEditTextDescription;
    private EditText mEditTextSummary;
    private Spinner mSpinnerProjectNames;
    private Spinner mSpinnerIssueTypes;
    private Button mButtonCreateIssue;
    private Spinner mSpinnerPriorities;
    private Spinner mSpinnerVersions;
    private EditText mEditTextEnvironment;
    private Spinner mAssignableUsers;
    private final String TAG = this.getClass().getSimpleName();
    private ArrayAdapter<String> mProjectsAdapter;
    private ArrayAdapter<String> mIssueTypesAdapter;
    private ArrayAdapter<String> mVersionsAdapter;
    private ArrayAdapter<String> mPrioritiesAdapter;
    private String mIssueTypeName;
    private String mPriorityName;
    private String mVersionName;
    private int mCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_issue);
        initViews();
    }

    private void initViews() {

        mEditTextEnvironment = (EditText) findViewById(R.id.et_environment);
        mEditTextDescription = (EditText) findViewById(R.id.et_description);

        mEditTextSummary = (EditText) findViewById(R.id.et_summary);
        mEditTextSummary.clearErrorOnTextChanged(true);
        mEditTextSummary.clearErrorOnFocus(true);

        mAssignableUsers = (Spinner) findViewById(R.id.spin_assignable_users);

        mButtonCreateIssue = (Button) findViewById(R.id.btn_create);
        mButtonCreateIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean isValid = true;

                if (TextUtils.isEmpty(mEditTextSummary.getText().toString())) {
                    mEditTextSummary.setError(Constants.DialogKeys.INPUT_SUMMARY);
                    isValid = false;
                }

                if (isValid) {
                    JiraContent.getInstance().createIssueAsynchronously(getIssueTypeName(),
                        getPriorityName(), getVersionName(), mEditTextSummary.getText().toString(),
                        mEditTextDescription.getText().toString(), mEditTextEnvironment.getText().toString(),
                        CreateIssueActivity.this);
                }
            }
        });

        mSpinnerIssueTypes = (Spinner) findViewById(R.id.spin_issue_name);
        mSpinnerIssueTypes.setEnabled(false);
        mSpinnerIssueTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIssueTypeName = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        mSpinnerVersions = (Spinner) findViewById(R.id.spin_affects_versions);
        mSpinnerVersions.setEnabled(false);
        mSpinnerVersions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mVersionName = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        mSpinnerPriorities = (Spinner) findViewById(R.id.spin_priority);
        mSpinnerPriorities.setEnabled(false);
        mSpinnerPriorities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPriorityName = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        JiraContent.getInstance().getPrioritiesNames(CreateIssueActivity.this);

        mSpinnerProjectNames = (Spinner) findViewById(R.id.spin_projects_name);
        mSpinnerProjectNames.setEnabled(false);
        JiraContent.getInstance().getProjectsNames(CreateIssueActivity.this);
        mSpinnerProjectNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCounter = 1;
                showProgress(true);
                JiraContent.getInstance().getProjectKeyByName((String) parent.getItemAtPosition(position), CreateIssueActivity.this);
                mSpinnerIssueTypes.setEnabled(false);
                JiraContent.getInstance().getIssueTypesNames(CreateIssueActivity.this);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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

            /** if (restResponse.getResultObject().getClass() == JUserAssignableResponse.class) {
             JUserAssignableResponse jUserAssignableResponse = (JUserAssignableResponse) restResponse.getResultObject();
             ArrayAdapter<String> jUsersAssignable = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, jUserAssignableResponse.getAssignableUsersNames());
             jUsersAssignable.setDropDownViewResource(R.layout.spinner_dropdown_item);
             mAssignableUsers.setAdapter(jUsersAssignable); */

    @Override
    public void success(Object result, JiraContentConst tagResult) {
        if (tagResult == JiraContentConst.PROJECTS_NAMES) {
            mProjectsAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, (ArrayList<String>) result);
            mProjectsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mSpinnerProjectNames.setAdapter(mProjectsAdapter);
            mSpinnerProjectNames.setEnabled(true);
            mCounter=mCounter+1;
            JiraContent.getInstance().getPrioritiesNames(CreateIssueActivity.this);
        } else if (tagResult == JiraContentConst.PROJECT_KEY_BY_NAME) {
            mSpinnerVersions.setEnabled(false);
            JiraContent.getInstance().getVersionsNames((String) result, CreateIssueActivity.this);
        } else if (tagResult == JiraContentConst.ISSUE_TYPES_NAMES) {
            mIssueTypesAdapter = new ArrayAdapter<String>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
            mIssueTypesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mSpinnerIssueTypes.setAdapter(mIssueTypesAdapter);
            mSpinnerIssueTypes.setEnabled(true);
            mCounter=mCounter+1;
        } else if (tagResult == JiraContentConst.VERSIONS_NAMES) {
            if (result != null) {
                mVersionsAdapter = new ArrayAdapter<String>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
                mVersionsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mSpinnerVersions.setAdapter(mVersionsAdapter);
            }
            mSpinnerVersions.setEnabled(true);
            mCounter=mCounter+1;
        } else if (tagResult == JiraContentConst.PRIORITIES_NAMES) {
            mPrioritiesAdapter = new ArrayAdapter<String>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
            mPrioritiesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mSpinnerPriorities.setAdapter(mPrioritiesAdapter);
            mSpinnerPriorities.setEnabled(true);
            mSpinnerPriorities.setSelection(2);
            mCounter=mCounter+1;
        }
        showProgress(true);
    }

    @Override
    public void showProgress(boolean show) {
        if (mCounter < 3) {
            super.showProgress(true);
            mButtonCreateIssue.setEnabled(false);
        } else {
            super.showProgress(false);
            mButtonCreateIssue.setEnabled(true);
        }
    }

}
