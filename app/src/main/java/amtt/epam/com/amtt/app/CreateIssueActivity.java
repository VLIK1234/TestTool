package amtt.epam.com.amtt.app;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.ticket.ScreenshotAdapter;
import amtt.epam.com.amtt.bo.JCreateIssueResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.helper.SystemInfoHelper;
import amtt.epam.com.amtt.observer.AmttFileObserver;
import amtt.epam.com.amtt.ticket.*;
import amtt.epam.com.amtt.view.AutocompleteProgressView;
import amtt.epam.com.amtt.view.EditText;
import amtt.epam.com.amtt.view.SpinnerProgress;
import amtt.epam.com.amtt.view.TextView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public class CreateIssueActivity extends BaseActivity implements ScreenshotAdapter.ViewHolder.ClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private static final int MESSAGE_TEXT_CHANGED = 100;
    private AutocompleteProgressView mAssignableAutocompleteView;
    private EditText mDescriptionEditText;
    private EditText mEnvironmentEditText;
    private EditText mSummaryEditText;
    private String mAssignableUserName = null;
    private String mIssueTypeName;
    private String mPriorityName;
    private String mVersionName;
    private AssigneeHandler mHandler;
    private ScreenshotAdapter mAdapter;
    private ArrayList<String> listScreenshot;
    public SpinnerProgress mProjectNamesSpinner;

    public static class AssigneeHandler extends Handler {

        private final WeakReference<CreateIssueActivity> mActivity;

        AssigneeHandler(CreateIssueActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CreateIssueActivity service = mActivity.get();
            service.setAssignableNames(msg.obj.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_issue);
        mHandler = new AssigneeHandler(this);
        listScreenshot = (ArrayList<String>) AmttFileObserver.getImageArray().clone();
        initViews();
    }

    private void initViews() {
        initProjectNamesSpinner();
        initSummaryEditText();
        initEnvironmentEditText();
        initDescriptionEditText();
        initPrioritiesSpinner();
        initCreateIssueButton();
        initAttachmentsView();
    }

    private void reinitRelatedViews(String projectKey) {
        initIssueTypesSpinner();
        initVersionsSpinner(projectKey);
        initAssigneeAutocompleteView();
    }

    private void initProjectNamesSpinner() {
        mProjectNamesSpinner = (SpinnerProgress) findViewById(R.id.spin_projects_name);
        mProjectNamesSpinner.setEnabled(false);
        mProjectNamesSpinner.showProgress(true);
        JiraContent.getInstance().getProjectsNames(new JiraGetContentCallback<HashMap<JProjects, String>>() {
            @Override
            public void resultOfDataLoading(HashMap<JProjects, String> result) {
                if (result != null) {
                    ArrayList<String> projectNames = new ArrayList<>();
                    projectNames.addAll(result.values());
                    ArrayAdapter<String> projectsAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, projectNames);
                    projectsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    mProjectNamesSpinner.setAdapter(projectsAdapter);
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
        final SpinnerProgress prioritiesSpinner = (SpinnerProgress) findViewById(R.id.spin_priority);
        prioritiesSpinner.setEnabled(false);
        prioritiesSpinner.showProgress(true);
        JiraContent.getInstance().getPrioritiesNames(new JiraGetContentCallback<HashMap<String, String>>() {
            @Override
            public void resultOfDataLoading(HashMap<String, String> result) {
                if (result != null) {
                    ArrayList<String> priorityNames = new ArrayList<>();
                    priorityNames.addAll(result.values());
                    ArrayAdapter<String> mPrioritiesAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, priorityNames);
                    mPrioritiesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    prioritiesSpinner.setAdapter(mPrioritiesAdapter);
                    prioritiesSpinner.setSelection(2);
                    prioritiesSpinner.showProgress(false);
                    prioritiesSpinner.setEnabled(true);
                }
            }
        });
        prioritiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        final SpinnerProgress versionsSpinner = (SpinnerProgress) findViewById(R.id.spin_affects_versions);
        final TextView affectTextView = (TextView)findViewById(R.id.tv_affects_versions);
        final ImageView dividerAffectVersion = (ImageView) findViewById(R.id.affect_divider);
        versionsSpinner.setEnabled(false);
        versionsSpinner.showProgress(true);
        JiraContent.getInstance().getVersionsNames(projectKey, new JiraGetContentCallback<HashMap<String, String>>() {
            @Override
            public void resultOfDataLoading(HashMap<String, String> result) {
                if (result != null&&result.size()>0) {
                    versionsSpinner.setVisibility(View.VISIBLE);
                    affectTextView.setVisibility(View.VISIBLE);
                    dividerAffectVersion.setVisibility(View.VISIBLE);
                    ArrayList<String> versionNames = new ArrayList<>();
                    versionNames.addAll(result.values());
                    ArrayAdapter<String> versionsAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, versionNames);
                    versionsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    versionsSpinner.setAdapter(versionsAdapter);
                    versionsSpinner.showProgress(false);
                    versionsSpinner.setEnabled(true);
                }else{
                    versionsSpinner.setVisibility(View.GONE);
                    affectTextView.setVisibility(View.GONE);
                    dividerAffectVersion.setVisibility(View.GONE);
                }
            }
        });
        versionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        final SpinnerProgress issueTypesSpinner = (SpinnerProgress) findViewById(R.id.spin_issue_name);
        issueTypesSpinner.setEnabled(false);
        issueTypesSpinner.showProgress(true);
        JiraContent.getInstance().getIssueTypesNames(new JiraGetContentCallback<ArrayList<String>>() {
            @Override
            public void resultOfDataLoading(ArrayList<String> result) {
                if (result != null) {
                    ArrayAdapter<String> issueTypesAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, result);
                    issueTypesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    issueTypesSpinner.setAdapter(issueTypesAdapter);
                    issueTypesSpinner.showProgress(false);
                    issueTypesSpinner.setEnabled(true);
                }
            }
        });
        issueTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        mEnvironmentEditText.setText(SystemInfoHelper.getDeviceOsInfo());
    }

    private void initCreateIssueButton() {
        Button createIssueButton = (Button) findViewById(R.id.btn_create);
        createIssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isValid = true;
                if (TextUtils.isEmpty(mSummaryEditText.getText().toString())) {
                    mSummaryEditText.requestFocus();
                    mSummaryEditText.setError(getString(R.string.enter_prefix) + getString(R.string.enter_summary));
                    isValid = false;
                    Toast.makeText(CreateIssueActivity.this, getString(R.string.enter_prefix) + getString(R.string.enter_summary), Toast.LENGTH_LONG).show();
                }
                if (isValid) {
                    showProgress(true);
                    JiraContent.getInstance().createIssue(mIssueTypeName,
                            mPriorityName, mVersionName, mSummaryEditText.getText().toString(),
                            mDescriptionEditText.getText().toString(), mEnvironmentEditText.getText().toString(),
                            mAssignableUserName, new JiraGetContentCallback<JCreateIssueResponse>() {
                                @Override
                                public void resultOfDataLoading(JCreateIssueResponse result) {
                                    if (result != null) {
                                        AttachmentService.start(CreateIssueActivity.this, listScreenshot);
                                        Toast.makeText(CreateIssueActivity.this, "Ticket success created", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        Toast.makeText(CreateIssueActivity.this, "Error", Toast.LENGTH_LONG).show();
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

    private void initAssigneeAutocompleteView() {
        mAssignableAutocompleteView = (AutocompleteProgressView) findViewById(R.id.atv_assignable_users);
        mAssignableAutocompleteView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, s), 750);
                    mAssignableUserName = s.toString();
                }
            }
        });
    }

    private void initAttachmentsView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listScreens);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CreateIssueActivity.this);
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        List<Attachment> screenArray = ScreenshotManager.getInstance().getScreenshotList(listScreenshot);
        mAdapter = new ScreenshotAdapter(screenArray, R.layout.item_screenshot, CreateIssueActivity.this);
        recyclerView.setAdapter(mAdapter);
    }

    private void setAssignableNames(String s) {
        mAssignableAutocompleteView.setEnabled(false);
        mAssignableAutocompleteView.showProgress(true);
        JiraContent.getInstance().getUsersAssignable(s, new JiraGetContentCallback<ArrayList<String>>() {
            @Override
            public void resultOfDataLoading(ArrayList<String> result) {
                if (result != null) {
                    ArrayAdapter<String> assignableUsersAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_dropdown_item, result);
                    mAssignableAutocompleteView.setThreshold(1);
                    mAssignableAutocompleteView.setAdapter(assignableUsersAdapter);
                    if(!mAssignableAutocompleteView.getText().toString().equals(assignableUsersAdapter.getItem(0))) {
                        mAssignableAutocompleteView.showDropDown();
                    }
                    mAssignableAutocompleteView.showProgress(false);
                    mAssignableAutocompleteView.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        mAdapter.removeItem(position);
    }

}
