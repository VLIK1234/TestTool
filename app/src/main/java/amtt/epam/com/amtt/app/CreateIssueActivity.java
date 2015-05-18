package amtt.epam.com.amtt.app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.ticket.JiraContent;
import amtt.epam.com.amtt.ticket.JiraContentConst;
import amtt.epam.com.amtt.ticket.JiraGetContentCallback;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.Logger;
import amtt.epam.com.amtt.view.EditText;

@SuppressWarnings("unchecked")
public class CreateIssueActivity extends BaseActivity implements JiraGetContentCallback {

    private final String TAG = this.getClass().getSimpleName();
    private ArrayAdapter<String> mAssignableUsersAdapter;
    private ArrayAdapter<String> mIssueTypesAdapter;
    private ArrayAdapter<String> mVersionsAdapter;
    private ArrayAdapter<String> mPrioritiesAdapter;
    private ArrayAdapter<String> mProjectsAdapter;
    private AutoCompleteTextView mACTextViewAssignableUsers;
    private Button mButtonCreateIssue;
    private EditText mEditTextDescription;
    private EditText mEditTextEnvironment;
    private EditText mEditTextSummary;
    private Queue<JiraContentConst> mQueueRequests = new LinkedList<>();
    private Spinner mSpinnerIssueTypes;
    private Spinner mSpinnerPriorities;
    private Spinner mSpinnerProjectNames;
    private Spinner mSpinnerVersions;
    private String mAssignableUserName;
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
        mACTextViewAssignableUsers = (AutoCompleteTextView) findViewById(R.id.et_assignable_users);
        mACTextViewAssignableUsers.setEnabled(false);
        mACTextViewAssignableUsers.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 3) {
                    mQueueRequests.add(JiraContentConst.USERS_ASSIGNABLE_NAMES);
                    JiraContent.getInstance().getUsersAssignable(s.toString(), CreateIssueActivity.this);
                    mAssignableUserName = s.toString();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        mEditTextSummary = (amtt.epam.com.amtt.view.EditText) findViewById(R.id.et_summary);
        mEditTextSummary.clearErrorOnTextChanged(true);
        mEditTextSummary.clearErrorOnFocus(true);

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
                    JiraContent.getInstance().createIssue(getIssueTypeName(),
                        getPriorityName(), getVersionName(), mEditTextSummary.getText().toString(),
                        mEditTextDescription.getText().toString(), mEditTextEnvironment.getText().toString(),
                            getAssignableUserName(), CreateIssueActivity.this);
                }
            }
        });

        mEditTextEnvironment = (amtt.epam.com.amtt.view.EditText) findViewById(R.id.et_environment);
        JiraContent.getInstance().getEnvironment(this);

        mEditTextDescription = (amtt.epam.com.amtt.view.EditText) findViewById(R.id.et_description);
        JiraContent.getInstance().getDescription(this);

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
                mSpinnerIssueTypes.setEnabled(false);
                mSpinnerVersions.setEnabled(false);
                mACTextViewAssignableUsers.setEnabled(false);
                mQueueRequests.add(JiraContentConst.PROJECT_KEY_BY_NAME);
                JiraContent.getInstance().getProjectKeyByName((String) parent.getItemAtPosition(position), CreateIssueActivity.this);
                showProgressIfNeed();
                mQueueRequests.add(JiraContentConst.ISSUE_TYPES_NAMES);
                JiraContent.getInstance().getIssueTypesNames(CreateIssueActivity.this);
                showProgressIfNeed();
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

    public String getAssignableUserName() {
        return mAssignableUserName;
    }

    @Override
    public void resultOfDataLoading(Object result, JiraContentConst tagResult) {
        if (tagResult == JiraContentConst.PROJECTS_NAMES) {
            if (result != null) {
                mProjectsAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, (ArrayList<String>) result);
                mProjectsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mSpinnerProjectNames.setAdapter(mProjectsAdapter);
                mSpinnerProjectNames.setEnabled(true);
            }

        } else if (tagResult == JiraContentConst.PROJECT_KEY_BY_NAME) {
            mQueueRequests.add(JiraContentConst.VERSIONS_NAMES);
            JiraContent.getInstance().getVersionsNames((String) result, CreateIssueActivity.this);
            mQueueRequests.add(JiraContentConst.USERS_ASSIGNABLE_NAMES);
            JiraContent.getInstance().getUsersAssignable("", CreateIssueActivity.this);
        } else if (tagResult == JiraContentConst.ISSUE_TYPES_NAMES) {
            if (result != null) {
                mIssueTypesAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
                mIssueTypesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mSpinnerIssueTypes.setAdapter(mIssueTypesAdapter);
                mSpinnerIssueTypes.setEnabled(true);
            }
        } else if (tagResult == JiraContentConst.VERSIONS_NAMES) {
            if (result != null) {
                mVersionsAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
                mVersionsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mSpinnerVersions.setAdapter(mVersionsAdapter);
                mSpinnerVersions.setEnabled(true);
            }
        } else if (tagResult == JiraContentConst.PRIORITIES_NAMES) {
            if (result != null) {
                mPrioritiesAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
                mPrioritiesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mSpinnerPriorities.setAdapter(mPrioritiesAdapter);
                mSpinnerPriorities.setSelection(2);
                mSpinnerPriorities.setEnabled(true);
            }
        } else if (tagResult == JiraContentConst.USERS_ASSIGNABLE_NAMES) {
            if (result != null) {
                mAssignableUsersAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
                mAssignableUsersAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mACTextViewAssignableUsers.setAdapter(mAssignableUsersAdapter);
                mACTextViewAssignableUsers.setThreshold(3);
                mACTextViewAssignableUsers.setEnabled(true);
            }

        } else if (tagResult == JiraContentConst.ENVIRONMENT) {
            if (result != null) {
                mEditTextEnvironment.setText(result.toString());
            }
        } else if (tagResult == JiraContentConst.DESCRIPTION) {
            if (result != null) {
                mEditTextDescription.setText(result.toString());
            }
        }else if (tagResult == JiraContentConst.CREATE_ISSUE) {
            if (result != null) {
                if((Boolean)result){
                    finish();
                }
            }
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
