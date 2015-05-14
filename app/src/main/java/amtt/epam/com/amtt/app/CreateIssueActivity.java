package amtt.epam.com.amtt.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraContent;
import amtt.epam.com.amtt.api.JiraContentConst;
import amtt.epam.com.amtt.api.JiraGetContentCallback;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.view.EditText;

@SuppressWarnings("unchecked")
public class CreateIssueActivity extends BaseActivity implements JiraGetContentCallback {

    private final String TAG = this.getClass().getSimpleName();
    private ArrayAdapter<String> mAssignableUsersAdapter;
    private ArrayAdapter<String> mIssueTypesAdapter;
    private ArrayAdapter<String> mVersionsAdapter;
    private ArrayAdapter<String> mPrioritiesAdapter;
    private ArrayAdapter<String> mProjectsAdapter;
    private Button mButtonCreateIssue;
    private EditText mEditTextDescription;
    private EditText mEditTextEnvironment;
    private EditText mEditTextSummary;
    private Queue<JiraContentConst> mQueueRequests = new LinkedList<>();
    private Spinner mSpinnerAssignableUsers;
    private Spinner mSpinnerIssueTypes;
    private Spinner mSpinnerPriorities;
    private Spinner mSpinnerProjectNames;
    private Spinner mSpinnerVersions;
    private String mIssueTypeName;
    private String mPriorityName;
    private String mVersionName;

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

        mSpinnerAssignableUsers = (Spinner) findViewById(R.id.spin_assignable_users);

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
                    mQueueRequests.add(JiraContentConst.CREATE_ISSUE);
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
        mQueueRequests.add(JiraContentConst.PRIORITIES_NAMES);
        JiraContent.getInstance().getPrioritiesNames(CreateIssueActivity.this);
        mSpinnerPriorities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPriorityName = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        mSpinnerProjectNames = (Spinner) findViewById(R.id.spin_projects_name);
        mSpinnerProjectNames.setEnabled(false);
        mQueueRequests.add(JiraContentConst.PROJECTS_NAMES);
        JiraContent.getInstance().getProjectsNames(CreateIssueActivity.this);
        mSpinnerProjectNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showProgressIfNeed();
                mQueueRequests.add(JiraContentConst.PROJECT_KEY_BY_NAME);
                JiraContent.getInstance().getProjectKeyByName((String) parent.getItemAtPosition(position), CreateIssueActivity.this);
                mQueueRequests.add(JiraContentConst.ISSUE_TYPES_NAMES);
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

    @Override
    public void loadData(Object result, JiraContentConst tagResult) {
        if (tagResult == JiraContentConst.PROJECTS_NAMES) {
            if (result != null) {
                mProjectsAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, (ArrayList<String>) result);
                mProjectsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mSpinnerProjectNames.setAdapter(mProjectsAdapter);
            }
            mSpinnerProjectNames.setEnabled(true);
        } else if (tagResult == JiraContentConst.PROJECT_KEY_BY_NAME) {
            mSpinnerVersions.setEnabled(false);
            mQueueRequests.add(JiraContentConst.VERSIONS_NAMES);
            JiraContent.getInstance().getVersionsNames((String) result, CreateIssueActivity.this);
            mQueueRequests.add(JiraContentConst.USERS_ASSIGNABLE_NAMES);
            JiraContent.getInstance().getUsersAssignableAsynchronously((String) result, CreateIssueActivity.this);
        } else if (tagResult == JiraContentConst.ISSUE_TYPES_NAMES) {
            if (result != null) {
                mIssueTypesAdapter = new ArrayAdapter<String>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
                mIssueTypesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mSpinnerIssueTypes.setAdapter(mIssueTypesAdapter);
            }
            mSpinnerIssueTypes.setEnabled(true);
        } else if (tagResult == JiraContentConst.VERSIONS_NAMES) {
            if (result != null) {
                mVersionsAdapter = new ArrayAdapter<String>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
                mVersionsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mSpinnerVersions.setAdapter(mVersionsAdapter);
            }
            mSpinnerVersions.setEnabled(true);
        } else if (tagResult == JiraContentConst.PRIORITIES_NAMES) {
            mPrioritiesAdapter = new ArrayAdapter<String>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
            mPrioritiesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            mSpinnerPriorities.setAdapter(mPrioritiesAdapter);
            mSpinnerPriorities.setEnabled(true);
            mSpinnerPriorities.setSelection(2);
        } else if (tagResult == JiraContentConst.USERS_ASSIGNABLE_NAMES) {
            if (result != null) {
                mAssignableUsersAdapter = new ArrayAdapter<String>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
                mAssignableUsersAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mSpinnerAssignableUsers.setAdapter(mAssignableUsersAdapter);
            }
            mSpinnerAssignableUsers.setEnabled(true);
        }else if (tagResult == JiraContentConst.CREATE_ISSUE) {
            if (result != null) {
                if((Boolean)result){
                    mQueueRequests.remove(JiraContentConst.CREATE_ISSUE);
                }
            }
            mSpinnerAssignableUsers.setEnabled(true);
        }
        mQueueRequests.remove(tagResult);
        showProgressIfNeed();
    }

    public void showProgressIfNeed() {
        if (!mQueueRequests.isEmpty()) {
            showProgress(true);
            mButtonCreateIssue.setEnabled(false);
        } else {
            showProgress(false);
            mButtonCreateIssue.setEnabled(true);
        }
    }

}
