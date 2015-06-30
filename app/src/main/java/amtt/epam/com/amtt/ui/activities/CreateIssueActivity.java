package amtt.epam.com.amtt.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.AttachmentAdapter;
import amtt.epam.com.amtt.api.JiraContentConst;
import amtt.epam.com.amtt.api.JiraGetContentCallback;
import amtt.epam.com.amtt.api.loadcontent.JiraContent;
import amtt.epam.com.amtt.bo.JCreateIssueResponse;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.ticket.Attachment;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.helper.SystemInfoHelper;
import amtt.epam.com.amtt.service.AttachmentService;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.views.AutocompleteProgressView;
import amtt.epam.com.amtt.ui.views.ComponentPickerAdapter;
import amtt.epam.com.amtt.ui.views.MultiAutoCompleteView;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.AttachmentManager;
import amtt.epam.com.amtt.util.InputsUtil;


public class CreateIssueActivity extends BaseActivity implements AttachmentAdapter.ViewHolder.ClickListener{

    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final String DEFAULT_PRIORITY_ID = "3";
    private AutocompleteProgressView mAssignableAutocompleteView;
    private EditText mDescriptionEditText;
    private EditText mEnvironmentEditText;
    private EditText mSummaryEditText;
    private String mAssignableUserName = null;
    private String mIssueTypeName;
    private String mPriorityName;
    private String mVersionName;
    private AssigneeHandler mHandler;
    private AttachmentAdapter mAdapter;
    public Spinner mProjectNamesSpinner;
    private RecyclerView recyclerView;
    private InputMethodManager mInputManager;
    private MultiAutoCompleteView mComponents;
    private Queue<JiraContentConst> mQueueRequests = new LinkedList<>();
    private Button mCreateIssueButton;

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
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mQueueRequests.add(JiraContentConst.DESCRIPTION_RESPONSE);
        mQueueRequests.add(JiraContentConst.ATTACHMENT_RESPONSE);
        showProgressIfNeed();
        initAttachmentsView();
        initDescriptionEditText();
        TopButtonService.sendActionChangeVisibilityTopbutton(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TopButtonService.sendActionChangeVisibilityTopbutton(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        setDefaultConfigs();
    }

    private void setDefaultConfigs() {
        if (mComponents.getSelectedItems() != null) {
            List<String> components = mComponents.getSelectedItems();
            if (components.size()!= 0) {
                ArrayList<String> componentsList = new ArrayList<>();
                for (int i = 1; i < components.size(); i++) {
                    componentsList.add(JiraContent.getInstance().getComponentIdByName(components.get(i)));
                }
                ActiveUser.getInstance().setLastComponentsIds(componentsList);
            }
        }
        JiraContent.getInstance().setDefaultConfig(ActiveUser.getInstance().getLastProjectKey(),
                ActiveUser.getInstance().getLastAssignee(), ActiveUser.getInstance().getLastComponentsIds());
    }

    private void initViews() {
        mQueueRequests.add(JiraContentConst.PROJECTS_RESPONSE);
        mQueueRequests.add(JiraContentConst.PRIORITIES_RESPONSE);
        initCreateIssueButton();
        initProjectNamesSpinner();
        initSummaryEditText();
        initEnvironmentEditText();
        initListStepButton();
        initPrioritiesSpinner();
        mInputManager = (InputMethodManager) CreateIssueActivity.this.getSystemService(INPUT_METHOD_SERVICE);
    }

    private void reinitRelatedViews(String projectKey) {
        mQueueRequests.add(JiraContentConst.ISSUE_TYPES_RESPONSE);
        mQueueRequests.add(JiraContentConst.VERSIONS_RESPONSE);
        mQueueRequests.add(JiraContentConst.COMPONENTS_RESPONSE);
        showProgressIfNeed();
        initIssueTypesSpinner();
        initVersionsSpinner(projectKey);
        initComponentsSpinner(projectKey);
        initAssigneeAutocompleteView();
    }

    private void initProjectNamesSpinner() {
        mProjectNamesSpinner = (Spinner) findViewById(R.id.spin_projects_name);
        mProjectNamesSpinner.setEnabled(false);
        JiraContent.getInstance().getProjectsNames(new JiraGetContentCallback<HashMap<JProjects, String>>() {
            @Override
            public void resultOfDataLoading(final HashMap<JProjects, String> result) {
                if (result != null) {
                    CreateIssueActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            ArrayList<String> projectNames = new ArrayList<>();
                            projectNames.addAll(result.values());
                            final ArrayAdapter<String> projectsAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, projectNames);
                            projectsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            mProjectNamesSpinner.setAdapter(projectsAdapter);
                            if (ActiveUser.getInstance().getLastProjectKey() != null) {
                                JiraContent.getInstance().getProjectNameByKey(ActiveUser.getInstance().getLastProjectKey(), new JiraGetContentCallback<String>() {
                                    @Override
                                    public void resultOfDataLoading(String result) {
                                        if (result != null) {
                                            mProjectNamesSpinner.setSelection(projectsAdapter.getPosition(result));
                                        }
                                    }
                                });
                            }else{
                                mProjectNamesSpinner.setSelection(0);
                            }
                            mQueueRequests.remove(JiraContentConst.PROJECTS_RESPONSE);
                            mProjectNamesSpinner.setEnabled(true);
                        }
                    });
                }
            }
        });
        mProjectNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JiraContent.getInstance().getProjectKeyByName((String) parent.getItemAtPosition(position), new JiraGetContentCallback<String>() {
                    @Override
                    public void resultOfDataLoading(String result) {
                        if (result != null) {
                            reinitRelatedViews(result);
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initPrioritiesSpinner() {
        final Spinner prioritiesSpinner = (Spinner) findViewById(R.id.spin_priority);
        prioritiesSpinner.setEnabled(false);
        JiraContent.getInstance().getPrioritiesNames(new JiraGetContentCallback<HashMap<String, String>>() {
            @Override
            public void resultOfDataLoading(final HashMap<String, String> result) {
                if (result != null) {
                    CreateIssueActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            ArrayList<String> priorityNames = new ArrayList<>();
                            priorityNames.addAll(result.values());
                            ArrayAdapter<String> mPrioritiesAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, priorityNames);
                            mPrioritiesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            prioritiesSpinner.setAdapter(mPrioritiesAdapter);
                            String defaultPriority = JiraContent.getInstance().getPriorityNameById(DEFAULT_PRIORITY_ID);
                            if (defaultPriority != null) {
                                prioritiesSpinner.setSelection(mPrioritiesAdapter.getPosition(defaultPriority));
                            }
                            mQueueRequests.remove(JiraContentConst.PRIORITIES_RESPONSE);
                            showProgressIfNeed();
                            prioritiesSpinner.setEnabled(true);
                        }
                    });
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
        final Spinner versionsSpinner = (Spinner) findViewById(R.id.spin_affects_versions);
        final TextView affectTextView = (TextView)findViewById(R.id.tv_affects_versions);
        versionsSpinner.setEnabled(false);
        JiraContent.getInstance().getVersionsNames(projectKey, new JiraGetContentCallback<HashMap<String, String>>() {
            @Override
            public void resultOfDataLoading(HashMap<String, String> result) {
                if (result != null && result.size() > 0) {
                    versionsSpinner.setVisibility(View.VISIBLE);
                    affectTextView.setVisibility(View.VISIBLE);
                    ArrayList<String> versionNames = new ArrayList<>();
                    versionNames.addAll(result.values());
                    ArrayAdapter<String> versionsAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, versionNames);
                    versionsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    versionsSpinner.setAdapter(versionsAdapter);
                    versionsSpinner.setEnabled(true);
                    mQueueRequests.remove(JiraContentConst.VERSIONS_RESPONSE);
                    showProgressIfNeed();
                } else {
                    versionsSpinner.setVisibility(View.GONE);
                    affectTextView.setVisibility(View.GONE);
                    mQueueRequests.remove(JiraContentConst.VERSIONS_RESPONSE);
                    showProgressIfNeed();
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

    private void initComponentsSpinner(String projectKey) {
        final TextView componentsTextView = (TextView) findViewById(R.id.tv_components);
        mComponents = (MultiAutoCompleteView) findViewById(R.id.editText);
        mComponents.setEnabled(false);
        JiraContent.getInstance().getComponentsNames(projectKey, new JiraGetContentCallback<HashMap<String, String>>() {
            @Override
            public void resultOfDataLoading(HashMap<String, String> result) {
                if (result != null && result.size() > 0) {
                    componentsTextView.setVisibility(View.VISIBLE);
                    mComponents.setVisibility(View.VISIBLE);
                    ArrayList<String> componentsNames = new ArrayList<>();
                    componentsNames.addAll(result.values());
                    ArrayAdapter<String> componentsAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, componentsNames);
                    componentsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    ComponentPickerAdapter componentPickerAdapter = new ComponentPickerAdapter(CreateIssueActivity.this, R.layout.spinner_layout, componentsNames);
                    mComponents.setAdapter(componentPickerAdapter);
                    if (ActiveUser.getInstance().getLastComponentsIds() != null) {
                        List<String> components = ActiveUser.getInstance().getLastComponentsIds();
                        ArrayList<String> componentsList = new ArrayList<>();
                        if (components.size()!= 0) {
                            for (int i = 0; i < components.size(); i++) {
                                componentsList.add(JiraContent.getInstance().getComponentNameById(components.get(i)));
                            }
                        }
                        mComponents.setSelectedItems(componentsList);
                    }
                    mQueueRequests.remove(JiraContentConst.COMPONENTS_RESPONSE);
                    showProgressIfNeed();
                    mComponents.setEnabled(true);
                } else {
                    mQueueRequests.remove(JiraContentConst.COMPONENTS_RESPONSE);
                    showProgressIfNeed();
                    componentsTextView.setVisibility(View.GONE);
                    mComponents.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initIssueTypesSpinner() {
        final Spinner issueTypesSpinner = (Spinner) findViewById(R.id.spin_issue_name);
        issueTypesSpinner.setEnabled(false);
        JiraContent.getInstance().getIssueTypesNames(new JiraGetContentCallback<ArrayList<String>>() {
            @Override
            public void resultOfDataLoading(final ArrayList<String> result) {
                if(result != null)
                {
                CreateIssueActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        ArrayAdapter<String> issueTypesAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, result);
                        issueTypesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        issueTypesSpinner.setAdapter(issueTypesAdapter);
                        mQueueRequests.remove(JiraContentConst.ISSUE_TYPES_RESPONSE);
                        showProgressIfNeed();
                        issueTypesSpinner.setEnabled(true);
                        hideKeyboard();
                    }
                });
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
        JiraContent.getInstance().getDescription(new JiraGetContentCallback<Spanned>() {
            @Override
            public void resultOfDataLoading(final Spanned result) {
                if (result != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDescriptionEditText.setText(result);
                        }
                    });
                }
                mQueueRequests.remove(JiraContentConst.DESCRIPTION_RESPONSE);
                showProgressIfNeed();
            }
        });
    }

    private void initListStepButton() {
        Button btnListStep = (Button) findViewById(R.id.btn_list_step);
        btnListStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), StepsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void initEnvironmentEditText() {
        mEnvironmentEditText = (EditText) findViewById(R.id.et_environment);
        mEnvironmentEditText.setText(SystemInfoHelper.getDeviceOsInfo());
    }

    private void initCreateIssueButton() {
        mCreateIssueButton = (Button) findViewById(R.id.btn_create);
        showProgressIfNeed();
        mCreateIssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isValid = true;
                if (TextUtils.isEmpty(mSummaryEditText.getText().toString())) {
                    mSummaryEditText.requestFocus();
                    mSummaryEditText.setError(getString(R.string.enter_prefix) + getString(R.string.enter_summary));
                    showKeyboard(mSummaryEditText);
                    isValid = false;
                    Toast.makeText(CreateIssueActivity.this, getString(R.string.enter_prefix) + getString(R.string.enter_summary), Toast.LENGTH_LONG).show();
                } else if (InputsUtil.hasWhitespaceMargins(mSummaryEditText.getText().toString())) {
                    mSummaryEditText.requestFocus();
                    mSummaryEditText.setError(getString(R.string.label_summary) + getString(R.string.label_cannot_whitespaces));
                    isValid = false;
                    Toast.makeText(CreateIssueActivity.this, getString(R.string.label_summary) + getString(R.string.label_cannot_whitespaces), Toast.LENGTH_LONG).show();
                }
                if (mIssueTypeName == null) {
                    isValid = false;
                    Toast.makeText(CreateIssueActivity.this, getString(R.string.error_message_host), Toast.LENGTH_LONG).show();
                }
                if (isValid) {
                    showProgress(true);
                    if (mComponents.getSelectedItems() != null) {
                        List<String> components = mComponents.getSelectedItems();
                        if (components.size()!= 0) {
                            ArrayList<String> componentsList = new ArrayList<>();
                            for (int i = 1; i < components.size(); i++) {
                                componentsList.add(JiraContent.getInstance().getComponentIdByName(components.get(i)));
                            }
                            ActiveUser.getInstance().setLastComponentsIds(componentsList);
                        }
                    }
                    JiraContent.getInstance().createIssue(mIssueTypeName,
                            mPriorityName, mVersionName, mSummaryEditText.getText().toString(),
                            mDescriptionEditText.getText().toString(), mEnvironmentEditText.getText().toString(),
                            mAssignableUserName, ActiveUser.getInstance().getLastComponentsIds(), new JiraGetContentCallback<JCreateIssueResponse>() {
                                @Override
                                public void resultOfDataLoading(JCreateIssueResponse result) {
                                    if (result != null) {
                                        AttachmentService.start(CreateIssueActivity.this, mAdapter.getAttachmentFilePathList());
                                        Toast.makeText(CreateIssueActivity.this, R.string.ticket_created, Toast.LENGTH_LONG).show();
                                        TopButtonService.stopRecord(CreateIssueActivity.this);
                                        finish();
                                    } else {
                                        Toast.makeText(CreateIssueActivity.this, R.string.error, Toast.LENGTH_LONG).show();
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
                if (s.length() > 2 && before <= count) {
                    if (InputsUtil.haveWhitespaces(s.toString())) {
                        Toast.makeText(CreateIssueActivity.this, getString(R.string.label_tester) + getString(R.string.label_no_whitespaces), Toast.LENGTH_LONG).show();
                    } else {
                        mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, s), 750);
                        mAssignableUserName = s.toString();
                    }
                }
            }
        });
        if (ActiveUser.getInstance().getLastAssignee() != null) {
            mAssignableAutocompleteView.setText(ActiveUser.getInstance().getLastAssignee());
        }
    }

    private void initAttachmentsView() {
        recyclerView = (RecyclerView) findViewById(R.id.listScreens);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CreateIssueActivity.this);
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DbObjectManager.INSTANCE.getAll(new Step(), new IResult<List<DatabaseEntity>>() {
            @Override
            public void onResult(final List<DatabaseEntity> result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null) {
                            ArrayList<Attachment> screenArray = AttachmentManager.getInstance().
                                getAttachmentList(result);
                            mAdapter = new AttachmentAdapter(screenArray, R.layout.item_screenshot, CreateIssueActivity.this);
                            recyclerView.setAdapter(mAdapter);
                        }
                        mQueueRequests.remove(JiraContentConst.ATTACHMENT_RESPONSE);
                        showProgressIfNeed();
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });

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
                    if (assignableUsersAdapter.getCount() > 0) {
                        if (!mAssignableAutocompleteView.getText().toString().equals(assignableUsersAdapter.getItem(0))) {
                            mAssignableAutocompleteView.showDropDown();
                        }else{
                            ActiveUser.getInstance().setLastAssigneeName(mAssignableAutocompleteView.getText().toString());
                        }
                    }
                    mAssignableAutocompleteView.showProgress(false);
                    mAssignableAutocompleteView.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onItemRemove(int position) {
        mAdapter.removeItem(position);
    }

    @Override
    public void onItemShow(int position) {
        Intent preview = new Intent(CreateIssueActivity.this, PreviewActivity.class);
        preview.putExtra(PreviewActivity.FILE_PATH, mAdapter.getAttachmentFilePathList().get(position));
        startActivity(preview);
    }

    private void hideKeyboard() {
        View view = CreateIssueActivity.this.getCurrentFocus();
        if (view != null) {
            mInputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void showKeyboard(View view){
        if (view != null) {
            mInputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
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
