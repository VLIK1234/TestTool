package amtt.epam.com.amtt.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.ticket.JiraContent;
import amtt.epam.com.amtt.ticket.JiraGetContentCallback;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.view.ACTextViewWithProgress;
import amtt.epam.com.amtt.view.EditText;
import amtt.epam.com.amtt.view.SpinnerWithProgress;

@SuppressWarnings("unchecked")
public class CreateIssueActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();
    private static final int MESSAGE_TEXT_CHANGED = 100;
    private ACTextViewWithProgress mAssignableUsersACTextView;
    private Button mCreateIssueButton;
    private EditText mDescriptionEditText;
    private EditText mEnvironmentEditText;
    private EditText mSummaryEditText;
    private String mAssignableUserName;
    private String mIssueTypeName;
    private String mPriorityName;
    private String mVersionName;
    private String mDelayedRun;

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

    private void reinitRelatedViews(String projectKey) {
        initIssueTypesSpinner(projectKey);
        initAssigneeACTextView();
    }

    private void initProjectNamesSpinner() {
        final SpinnerWithProgress mProjectNamesSpinner = (SpinnerWithProgress) findViewById(R.id.spin_projects_name);
        mProjectNamesSpinner.setEnabled(false);
        mProjectNamesSpinner.showProgress(true);
        JiraContent.getInstance().getProjectsNames(new JiraGetContentCallback<HashMap<JProjects, String>>() {
            @Override
            public void resultOfDataLoading(HashMap<JProjects, String> result) {
                if (result != null) {
                    ArrayList<String> projectNames = new ArrayList<>();
                    projectNames.addAll(result.values());
                    ArrayAdapter<String> mProjectsAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, projectNames);
                    mProjectsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    mProjectNamesSpinner.setAdapter(mProjectsAdapter);
                    mProjectNamesSpinner.showProgress(false);
                    mProjectNamesSpinner.setEnabled(true);
                }
            }
        });
        mProjectNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JiraContent.getInstance().getProjectKeyByName((String) parent.getItemAtPosition(position), new JiraGetContentCallback<String>() {
                    @Override
                    public void resultOfDataLoading(String result) {
                        reinitRelatedViews(result);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initPrioritiesSpinner() {
        final SpinnerWithProgress mPrioritiesSpinner = (SpinnerWithProgress) findViewById(R.id.spin_priority);
        mPrioritiesSpinner.setEnabled(false);
        mPrioritiesSpinner.showProgress(true);
        JiraContent.getInstance().getPrioritiesNames(new JiraGetContentCallback<HashMap<String, String>>() {
            @Override
            public void resultOfDataLoading(HashMap<String, String> result) {
                if (result != null) {
                    ArrayList<String> priorityNames = new ArrayList<>();
                    priorityNames.addAll(result.values());
                    ArrayAdapter<String> mPrioritiesAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, priorityNames);
                    mPrioritiesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    mPrioritiesSpinner.setAdapter(mPrioritiesAdapter);
                    mPrioritiesSpinner.setSelection(2);
                    mPrioritiesSpinner.showProgress(false);
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

    private void initVersionsSpinner(String projectKey) {
        final SpinnerWithProgress mVersionsSpinner = (SpinnerWithProgress) findViewById(R.id.spin_affects_versions);
        mVersionsSpinner.setEnabled(false);
        mVersionsSpinner.showProgress(true);
        JiraContent.getInstance().getVersionsNames(projectKey, new JiraGetContentCallback<HashMap<String, String>>() {
            @Override
            public void resultOfDataLoading(HashMap<String, String> result) {
                if (result != null) {
                    ArrayList<String> versionNames = new ArrayList<>();
                    versionNames.addAll(result.values());
                    ArrayAdapter<String> mVersionsAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, versionNames);
                    mVersionsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    mVersionsSpinner.setAdapter(mVersionsAdapter);
                    mVersionsSpinner.showProgress(false);
                    mVersionsSpinner.setEnabled(true);
                }
            }
        });
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

    private void initIssueTypesSpinner(String projectKey) {
        final SpinnerWithProgress mIssueTypesSpinner = (SpinnerWithProgress) findViewById(R.id.spin_issue_name);
        mIssueTypesSpinner.setEnabled(false);
        mIssueTypesSpinner.showProgress(true);
        JiraContent.getInstance().getIssueTypesNames(new JiraGetContentCallback<ArrayList<String>>() {
            @Override
            public void resultOfDataLoading(ArrayList<String> result) {
                if (result != null) {
                    ArrayAdapter<String> mIssueTypesAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, result);
                    mIssueTypesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    mIssueTypesSpinner.setAdapter(mIssueTypesAdapter);
                    mIssueTypesSpinner.showProgress(false);
                    mIssueTypesSpinner.setEnabled(true);
                }
            }
        });
        initVersionsSpinner(projectKey);
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
            public void resultOfDataLoading(String result) {
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
            public void resultOfDataLoading(String result) {
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
                    showProgress(true);
                    JiraContent.getInstance().createIssue(mIssueTypeName,
                            mPriorityName, mVersionName, mSummaryEditText.getText().toString(),
                            mDescriptionEditText.getText().toString(), mEnvironmentEditText.getText().toString(),
                            mAssignableUserName, new JiraGetContentCallback<Boolean>() {
                                @Override
                                public void resultOfDataLoading(Boolean result) {
                                    if (result != null) {
                                        if (result) {
                                            finish();
                                        }
                                    }
                                    showProgress(false);
                                }
                            });
                }
            }
        });
    }

    private void initSummaryEditText() {
        mSummaryEditText = (EditText) findViewById(R.id.et_summary);
        mSummaryEditText.clearErrorOnTextChanged(true);
        mSummaryEditText.clearErrorOnFocus(true);
    }

    private void initAssigneeACTextView() {
        mAssignableUsersACTextView = (ACTextViewWithProgress) findViewById(R.id.et_assignable_users);
        mAssignableUsersACTextView.setEnabled(false);
        mAssignableUsersACTextView.showProgress(true);
        mAssignableUsersACTextView.showProgress(true);
        JiraContent.getInstance().getUsersAssignable("", new JiraGetContentCallback<ArrayList<String>>() {
            @Override
            public void resultOfDataLoading(ArrayList<String> result) {
                if (result != null) {
                    ArrayAdapter<String> mAssignableUsersAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, result);
                    mAssignableUsersAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    mAssignableUsersACTextView.setAdapter(mAssignableUsersAdapter);
                    mAssignableUsersACTextView.setThreshold(3);
                    mAssignableUsersACTextView.setEnabled(true);
                    mAssignableUsersACTextView.showProgress(false);
                    mCreateIssueButton.setEnabled(true);
                }
            }
        });
        mAssignableUsersACTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 2) {
                    mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, s), 750);
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

    private void setAssignableNames(Editable s, int keyCode) {
        mAssignableUsersACTextView.showProgress(true);
        JiraContent.getInstance().getUsersAssignable(s.toString(), new JiraGetContentCallback<ArrayList<String>>() {
            @Override
            public void resultOfDataLoading(ArrayList<String> result) {
                if (result != null) {
                    ArrayAdapter<String> mAssignableUsersAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, result);
                    mAssignableUsersAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    mAssignableUsersACTextView.setAdapter(mAssignableUsersAdapter);
                    mAssignableUsersACTextView.setThreshold(3);
                    mAssignableUsersACTextView.showProgress(false);
                    mAssignableUsersACTextView.setEnabled(true);
                }
            }
        });
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CreateIssueActivity.this.setAssignableNames((Editable) msg.obj, msg.arg1);
        }
    };

}
