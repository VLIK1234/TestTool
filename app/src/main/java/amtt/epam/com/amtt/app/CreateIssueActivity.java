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
import amtt.epam.com.amtt.view.EditText;

@SuppressWarnings("unchecked")
public class CreateIssueActivity extends BaseActivity implements JiraGetContentCallback {

    private final String TAG = this.getClass().getSimpleName();
    private AutoCompleteTextView mAssignableUsersACTextView;
    private Button mCreateIssueButton;
    private EditText mDescriptionEditText;
    private EditText mEnvironmentEditText;
    private EditText mSummaryEditText;
    private Queue<JiraContentConst> mQueueRequests = new LinkedList<>();
    private Spinner mIssueTypesSpinner;
    private Spinner mVersionsSpinner;
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
        initProjectNamesSpinner();
        initSummaryEditText();
        initEnvironmentEditText();
        initDescriptionEditText();
        initPrioritiesSpinner();
        initCreateIssueButton();
    }

    private void reinitRelatedViews() {
        initIssueTypesSpinner();
        initVersionsSpinner();
        initAssigneeSpinner();
    }

    private void initProjectNamesSpinner() {
        final Spinner mProjectNamesSpinner = (Spinner) findViewById(R.id.spin_projects_name);
        mProjectNamesSpinner.setEnabled(false);
        mQueueRequests.add(JiraContentConst.PROJECTS_NAMES);
        JiraContent.getInstance().getProjectsNames(new JiraGetContentCallback<ArrayList<String>>() {
            @Override
            public void resultOfDataLoading(ArrayList<String> result, JiraContentConst tagResult) {
                if (result != null) {
                    ArrayAdapter<String> mProjectsAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, result);
                    mProjectsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    mProjectNamesSpinner.setAdapter(mProjectsAdapter);
                    mProjectNamesSpinner.setEnabled(true);
                }
            }
        });
        mProjectNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mQueueRequests.add(JiraContentConst.PROJECT_KEY_BY_NAME);
                JiraContent.getInstance().getProjectKeyByName((String) parent.getItemAtPosition(position), new JiraGetContentCallback<String>() {
                    @Override
                    public void resultOfDataLoading(String result, JiraContentConst tagResult) {
                        mQueueRequests.add(JiraContentConst.VERSIONS_NAMES);
                        JiraContent.getInstance().getVersionsNames(result, CreateIssueActivity.this);
                        mQueueRequests.add(JiraContentConst.USERS_ASSIGNABLE_NAMES);
                        JiraContent.getInstance().getUsersAssignable("", CreateIssueActivity.this);
                        mQueueRequests.add(JiraContentConst.ISSUE_TYPES_NAMES);
                        JiraContent.getInstance().getIssueTypesNames(CreateIssueActivity.this);
                    }
                });
                showProgressIfNeed();
                reinitRelatedViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initPrioritiesSpinner() {
        final Spinner mPrioritiesSpinner = (Spinner) findViewById(R.id.spin_priority);
        mPrioritiesSpinner.setEnabled(false);
        mQueueRequests.add(JiraContentConst.PRIORITIES_NAMES);
        JiraContent.getInstance().getPrioritiesNames(new JiraGetContentCallback<ArrayList<String>>() {
            @Override
            public void resultOfDataLoading(ArrayList<String> result, JiraContentConst tagResult) {
                if (result != null) {
                    ArrayAdapter<String> mPrioritiesAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, result);
                    mPrioritiesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    mPrioritiesSpinner.setAdapter(mPrioritiesAdapter);
                    mPrioritiesSpinner.setSelection(2);
                    mPrioritiesSpinner.setEnabled(true);
                }
            }
        });
        mPrioritiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPriorityName = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initVersionsSpinner() {
        mVersionsSpinner = (Spinner) findViewById(R.id.spin_affects_versions);
        mVersionsSpinner.setEnabled(false);
        mVersionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mVersionName = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initIssueTypesSpinner() {
        mIssueTypesSpinner = (Spinner) findViewById(R.id.spin_issue_name);
        mIssueTypesSpinner.setEnabled(false);
        mIssueTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIssueTypeName = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initDescriptionEditText() {
        mDescriptionEditText = (EditText) findViewById(R.id.et_description);
        JiraContent.getInstance().getDescription(new JiraGetContentCallback<String>() {
            @Override
            public void resultOfDataLoading(String result, JiraContentConst tagResult) {
                if (result != null) {
                    mDescriptionEditText.setText(result);
                }
            }
        });
    }

    private void initEnvironmentEditText() {
        mEnvironmentEditText = (EditText) findViewById(R.id.et_environment);
        JiraContent.getInstance().getEnvironment(new JiraGetContentCallback<String>() {
            @Override
            public void resultOfDataLoading(String result, JiraContentConst tagResult) {
                if (result != null) {
                    mEnvironmentEditText.setText(result);
                }
            }
        });
    }

    private void initCreateIssueButton() {
        mCreateIssueButton = (Button) findViewById(R.id.btn_create);
        mCreateIssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isValid = true;
                if (TextUtils.isEmpty(mSummaryEditText.getText().toString())) {
                    mSummaryEditText.setError(Constants.DialogKeys.INPUT_SUMMARY);
                    isValid = false;
                }
                if (isValid) {
                    mQueueRequests.add(JiraContentConst.CREATE_ISSUE);
                    JiraContent.getInstance().createIssue(mIssueTypeName,
                            mPriorityName, mVersionName, mSummaryEditText.getText().toString(),
                            mDescriptionEditText.getText().toString(), mEnvironmentEditText.getText().toString(),
                            mAssignableUserName, CreateIssueActivity.this);
                }
            }
        });
    }

    private void initSummaryEditText() {
        mSummaryEditText = (EditText) findViewById(R.id.et_summary);
        mSummaryEditText.clearErrorOnTextChanged(true);
        mSummaryEditText.clearErrorOnFocus(true);
    }

    private void initAssigneeSpinner() {
        mAssignableUsersACTextView = (AutoCompleteTextView) findViewById(R.id.et_assignable_users);
        mAssignableUsersACTextView.setEnabled(false);
        mAssignableUsersACTextView.addTextChangedListener(new TextWatcher() {
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
    }

    @Override
    public void resultOfDataLoading(Object result, JiraContentConst tagResult) {
        if (tagResult == JiraContentConst.ISSUE_TYPES_NAMES) {
            if (result != null) {
                ArrayAdapter<String> mIssueTypesAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
                mIssueTypesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mIssueTypesSpinner.setAdapter(mIssueTypesAdapter);
                mIssueTypesSpinner.setEnabled(true);
            }
        } else if (tagResult == JiraContentConst.VERSIONS_NAMES) {
            if (result != null) {
                ArrayAdapter<String> mVersionsAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
                mVersionsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mVersionsSpinner.setAdapter(mVersionsAdapter);
                mVersionsSpinner.setEnabled(true);
            }
        } else if (tagResult == JiraContentConst.USERS_ASSIGNABLE_NAMES) {
            if (result != null) {
                ArrayAdapter<String> mAssignableUsersAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, (ArrayList<String>) result);
                mAssignableUsersAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                mAssignableUsersACTextView.setAdapter(mAssignableUsersAdapter);
                mAssignableUsersACTextView.setThreshold(3);
                mAssignableUsersACTextView.setEnabled(true);
            }

        } else if (tagResult == JiraContentConst.CREATE_ISSUE) {
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
            mCreateIssueButton.setEnabled(false);
        } else {
            showProgress(false);
            mCreateIssueButton.setEnabled(true);
        }
    }

}
