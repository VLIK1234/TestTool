package amtt.epam.com.amtt.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.AttachmentAdapter;
import amtt.epam.com.amtt.api.ContentConst;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.api.loadcontent.JiraContent;
import amtt.epam.com.amtt.bo.JCreateIssueResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.ticket.Attachment;
import amtt.epam.com.amtt.bo.ticket.Step;
import amtt.epam.com.amtt.broadcastreceiver.GlobalBroadcastReceiver;
import amtt.epam.com.amtt.database.util.LocalContent;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.helper.DialogHelper;
import amtt.epam.com.amtt.helper.SystemInfoHelper;
import amtt.epam.com.amtt.http.MimeType;
import amtt.epam.com.amtt.service.AttachmentService;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.views.AutocompleteProgressView;
import amtt.epam.com.amtt.ui.views.TextInput;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.AttachmentManager;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.GifUtil;
import amtt.epam.com.amtt.util.InputsUtil;
import amtt.epam.com.amtt.util.PreferenceUtil;
import amtt.epam.com.amtt.util.Validator;


public class CreateIssueActivity extends BaseActivity
        implements AttachmentAdapter.ViewHolder.ClickListener, AttachmentAdapter.ViewHolder.DataChangedListener,
        SharedPreferences.OnSharedPreferenceChangeListener, GifUtil.ProgressListener,
        AttachmentAdapter.ViewHolder.ScreenshotStateListener {

    private static final int PAINT_ACTIVITY_REQUEST_CODE = 0;
    public static final int ADD_ATTACHE_FILE_ACTIVITY_REQUEST_CODE = 1;
    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final String DEFAULT_PRIORITY_ID = "3";
    private static final String BUG = "Bug";
    private static final String TASK = "Task";
    private static final String TAG = CreateIssueActivity.class.getSimpleName();
    public static final String KEY_LIST_ADD_SHARED_FILE = "key_list_add_shared_file";
    public static final String KEY_START_ACTIVITY_FOR_RESULT = "key_start_activity_for_result";
    private final Queue<ContentConst> mRequestsQueue = new LinkedList<>();
    private AutocompleteProgressView mAssignableAutocompleteView;
    private TextInput mDescriptionTextInput;
    private TextInput mEnvironmentTextInput;
    private TextInput mTitleTextInput;
    private String mAssignableUserName = null;
    private String mIssueTypeName;
    private String mPriorityName;
    private String mVersionName;
    private AssigneeHandler mHandler;
    private AttachmentAdapter mAdapter;
    private Spinner mProjectNamesSpinner;
    private RecyclerView mRecyclerView;
    private Spinner mComponents;
    private Button mCreateIssueButton;
    private ProgressBar mGifProgress;
    private CheckBox mGifCheckBox;
    private List<Step> mSteps;
    private ScrollView mScrollView;
    private int[] mTitlePoint;
    private CheckBox mCreateAnotherCheckBox;
    private boolean mCreateAnotherIssue;
    private boolean mIsAssignableSelected;
    private boolean mIsSelfSigned;
    private Bundle mBundle;
    private ActiveUser mUser = ActiveUser.getInstance();
    private JiraContent mJira = JiraContent.getInstance();
    private AttachmentManager mAttachmentManager = AttachmentManager.getInstance();
    private ArrayList<String> mListSharedFile = new ArrayList<>();

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
        mBundle = getIntent().getExtras();
        initViews();
        mRequestsQueue.add(ContentConst.DESCRIPTION_RESPONSE);
        mRequestsQueue.add(ContentConst.ATTACHMENT_RESPONSE);
        initAttachLogsCheckBox();
        initDescriptionEditText();
        initAttachmentsView();
        PreferenceUtil.getPref().registerOnSharedPreferenceChangeListener(CreateIssueActivity.this);
        mHandler = new AssigneeHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setDefaultConfigs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intentLogs = new Intent();
        intentLogs.setAction(GlobalBroadcastReceiver.EXTERNAL_LOGS_TAKE);
        getBaseContext().sendBroadcast(intentLogs);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PAINT_ACTIVITY_REQUEST_CODE:
                    loadAttachments();
                    break;
                case ADD_ATTACHE_FILE_ACTIVITY_REQUEST_CODE:
                    Bundle extra = data.getExtras();
                    if (extra != null) {
                        mListSharedFile = extra.getStringArrayList(KEY_LIST_ADD_SHARED_FILE);
                        if (mListSharedFile != null) {
                            for (String filePath : mListSharedFile) {
                                mAdapter.addItem(mAdapter.getItemCount(), new Attachment(filePath));
                            }
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
            }
        }
        PreferenceUtil.getPref().unregisterOnSharedPreferenceChangeListener(CreateIssueActivity.this);
    }

    private void setDefaultConfigs() {
        if (mComponents != null && mComponents.getSelectedItem() != null) {
            String component = mJira.getComponentIdByName((String) mComponents.getSelectedItem());
            mUser.setLastComponentsIds(component);
        }
        mJira.setDefaultConfig(mUser.getId(), mUser.getUserName(), mUser.getUrl(),
                mUser.getLastProjectKey(), mUser.getLastAssignee(), mUser.getLastComponentsIds());
    }

    private void initViews() {
        mRequestsQueue.add(ContentConst.PROJECTS_RESPONSE);
        mRequestsQueue.add(ContentConst.PRIORITIES_RESPONSE);
        initCreateIssueButton();
        showProgressIfNeed();
        initProjectNamesSpinner();
        initSummaryEditText();
        initEnvironmentEditText();
        initListStepButton();
        initPrioritiesSpinner();
        initCreateAnotherCheckBox();
        initClearEnvironmentButton();
        initGifAttachmentControls();
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        initShareAttachmentButton();
    }

    private void initShareAttachmentButton() {
        Button shareFileObserver = (Button) findViewById(R.id.bt_share_file_observer);
        shareFileObserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareFileActivity = new Intent(CreateIssueActivity.this, ShareFilesActivity.class);
                shareFileActivity.putExtra(KEY_START_ACTIVITY_FOR_RESULT, "startForResult");
                startActivityForResult(shareFileActivity, ADD_ATTACHE_FILE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private void initCreateAnotherCheckBox() {
        mCreateAnotherCheckBox = (CheckBox) findViewById(R.id.chb_create_another);
        mCreateAnotherCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCreateAnotherIssue = isChecked;
            }
        });
    }

    private void reinitRelatedViews(String projectKey) {
        mRequestsQueue.add(ContentConst.ISSUE_TYPES_RESPONSE);
        mRequestsQueue.add(ContentConst.VERSIONS_RESPONSE);
        mRequestsQueue.add(ContentConst.COMPONENTS_RESPONSE);
        showProgressIfNeed();
        initIssueTypesSpinner();
        initVersionsSpinner(projectKey);
        initComponentsSpinner(projectKey);
        initAssigneeAutocompleteView();
    }

    private void initProjectNamesSpinner() {
        mProjectNamesSpinner = (Spinner) findViewById(R.id.spin_projects_name);
        mProjectNamesSpinner.setEnabled(false);
        mJira.getProjectsNames(mUser.getId(), new GetContentCallback<HashMap<JProjects, String>>() {
            @Override
            public void resultOfDataLoading(final HashMap<JProjects, String> result) {
                if (result != null) {
                    ArrayList<String> projectNames = new ArrayList<>();
                    projectNames.addAll(result.values());
                    final ArrayAdapter<String> projectsAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, projectNames);
                    projectsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    mProjectNamesSpinner.setAdapter(projectsAdapter);
                    if (mUser.getLastProjectKey() != null) {
                        mJira.getProjectNameByKey(mUser.getLastProjectKey(), new GetContentCallback<String>() {
                            @Override
                            public void resultOfDataLoading(final String result) {
                                if (result != null && projectsAdapter.getPosition(result) >= 0) {
                                    mProjectNamesSpinner.setSelection(projectsAdapter.getPosition(result));
                                }
                            }
                        });
                    } else {
                        mProjectNamesSpinner.setSelection(0);
                    }
                    mProjectNamesSpinner.setEnabled(true);
                }
                mRequestsQueue.remove(ContentConst.PROJECTS_RESPONSE);
            }
        });
        mProjectNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mJira.getProjectKeyByName((String) parent.getItemAtPosition(position), new GetContentCallback<String>() {
                    @Override
                    public void resultOfDataLoading(final String result) {
                        if (result != null) {
                            mUser.setLastProjectKey(result);
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
        mJira.getPrioritiesNames(mUser.getUrl(), new GetContentCallback<HashMap<String, String>>() {
            @Override
            public void resultOfDataLoading(final HashMap<String, String> result) {
                if (result != null) {
                    ArrayList<String> priorityNames = new ArrayList<>();
                    priorityNames.addAll(result.values());
                    ArrayAdapter<String> mPrioritiesAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, priorityNames);
                    mPrioritiesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    prioritiesSpinner.setAdapter(mPrioritiesAdapter);
                    String defaultPriority;
                    if (mBundle != null && mBundle.getString(ExpectedResultsActivity.PRIORITY) != null) {
                        defaultPriority = mBundle.getString(ExpectedResultsActivity.PRIORITY);
                        if (defaultPriority != null && mPrioritiesAdapter.getPosition(defaultPriority) >= 0) {
                            prioritiesSpinner.setSelection(mPrioritiesAdapter.getPosition(defaultPriority));
                        }
                    } else {
                        defaultPriority = mJira.getPriorityNameById(DEFAULT_PRIORITY_ID);
                        if (defaultPriority != null && mPrioritiesAdapter.getPosition(defaultPriority) >= 0) {
                            prioritiesSpinner.setSelection(mPrioritiesAdapter.getPosition(defaultPriority));
                        }
                    }
                    prioritiesSpinner.setEnabled(true);
                }
                mRequestsQueue.remove(ContentConst.PRIORITIES_RESPONSE);
                showProgressIfNeed();
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
        final TextView affectTextView = (TextView) findViewById(R.id.tv_affects_versions);
        versionsSpinner.setEnabled(false);
        mJira.getVersionsNames(projectKey, new GetContentCallback<HashMap<String, String>>() {
            @Override
            public void resultOfDataLoading(final HashMap<String, String> result) {
                if (result != null && result.size() > 0) {
                    versionsSpinner.setVisibility(View.VISIBLE);
                    affectTextView.setVisibility(View.VISIBLE);
                    ArrayList<String> versionNames = new ArrayList<>();
                    versionNames.addAll(result.values());
                    ArrayAdapter<String> versionsAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, versionNames);
                    versionsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    versionsSpinner.setAdapter(versionsAdapter);
                    versionsSpinner.setEnabled(true);
                } else {
                    versionsSpinner.setVisibility(View.GONE);
                    affectTextView.setVisibility(View.GONE);
                }
                mRequestsQueue.remove(ContentConst.VERSIONS_RESPONSE);
                showProgressIfNeed();
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
        mComponents = (Spinner) findViewById(R.id.spin_components);
        mComponents.setEnabled(false);
        mJira.getComponentsNames(projectKey, new GetContentCallback<HashMap<String, String>>() {
            @Override
            public void resultOfDataLoading(final HashMap<String, String> result) {
                if (result != null && result.size() > 0) {
                    ArrayList<String> componentsNames = new ArrayList<>();
                    componentsNames.addAll(result.values());
                    ArrayAdapter<String> componentsAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, componentsNames);
                    componentsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    componentsTextView.setVisibility(View.VISIBLE);
                    mComponents.setAdapter(componentsAdapter);
                    mComponents.setVisibility(View.VISIBLE);
                    mComponents.setEnabled(true);
                    if (mUser.getLastComponentsIds() != null
                            && mJira.getComponentNameById(mUser.getLastComponentsIds()) != null
                            && componentsAdapter.getPosition(mJira.getComponentNameById(mUser.getLastComponentsIds())) >= 0) {
                        mComponents.setSelection(componentsAdapter.getPosition(mJira.getComponentNameById(mUser.getLastComponentsIds())));
                    } else {
                        mComponents.setSelection(0);
                    }
                } else {
                    componentsTextView.setVisibility(View.GONE);
                    mComponents.setVisibility(View.GONE);
                }
                mRequestsQueue.remove(ContentConst.COMPONENTS_RESPONSE);
                showProgressIfNeed();
            }
        });
    }

    private void initIssueTypesSpinner() {
        final Spinner issueTypesSpinner = (Spinner) findViewById(R.id.spin_issue_name);
        issueTypesSpinner.setEnabled(false);
        mJira.getIssueTypesNames(new GetContentCallback<List<String>>() {
            @Override
            public void resultOfDataLoading(final List<String> result) {
                if (result != null) {
                    ArrayAdapter<String> issueTypesAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, result);
                    issueTypesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    issueTypesSpinner.setAdapter(issueTypesAdapter);
                    if (mUser.getRecord()) {
                        if (issueTypesAdapter.getPosition(BUG) != -1) {
                            issueTypesSpinner.setSelection(issueTypesAdapter.getPosition(BUG));
                        }
                    } else {
                        if (issueTypesAdapter.getPosition(TASK) != -1) {
                            issueTypesSpinner.setSelection(issueTypesAdapter.getPosition(TASK));
                        }
                    }
                    issueTypesSpinner.setEnabled(true);
                    hideKeyboard();
                }
                mRequestsQueue.remove(ContentConst.ISSUE_TYPES_RESPONSE);
                showProgressIfNeed();
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
        mDescriptionTextInput = (TextInput) findViewById(R.id.description_input);
        mDescriptionTextInput.setText(SystemInfoHelper.getDeviceOsInfo());
        getDescription();
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
        mEnvironmentTextInput = (TextInput) findViewById(R.id.environment_input);
//        mEnvironmentTextInput.setText(SystemInfoHelper.getDeviceOsInfo());
    }

    private void initCreateIssueButton() {
        mCreateIssueButton = (Button) findViewById(R.id.btn_create);
        showProgressIfNeed();
        mCreateIssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mTitleTextInput.validate()) {
                    mScrollView.smoothScrollTo(mTitlePoint[0], mTitlePoint[1]);
                    showKeyboard(mTitleTextInput.getEdit());
                    return;
                }
                if (mIssueTypeName == null) {
                    Toast.makeText(CreateIssueActivity.this, getString(R.string.error_message_host), Toast.LENGTH_LONG).show();
                    return;
                }
                showProgress(true);
                if (mComponents.getSelectedItem() != null) {
                    String components = (String) mComponents.getSelectedItem();
                    mUser.setLastComponentsIds(mJira.getComponentIdByName(components));
                }
                mJira.createIssue(mIssueTypeName, mPriorityName, mVersionName, mTitleTextInput.getText().toString(),
                        mDescriptionTextInput.getText().toString(), mEnvironmentTextInput.getText().toString(),
                        mAssignableUserName, mUser.getLastComponentsIds(), new GetContentCallback<JCreateIssueResponse>() {
                            @Override
                            public void resultOfDataLoading(final JCreateIssueResponse result) {
                                if (result != null) {
                                    if (mAdapter != null && mAdapter.getAttachmentFilePathList() != null) {
                                        AttachmentService.start(CreateIssueActivity.this, mAdapter.getAttachmentFilePathList());
                                    }
                                    Toast.makeText(CreateIssueActivity.this, R.string.ticket_created, Toast.LENGTH_LONG).show();
                                    TopButtonService.stopRecord(CreateIssueActivity.this);
                                    if (mAssignableUserName != null && !mAssignableUserName.equals("") && !mUser.getLastAssignee().equals(mAssignableUserName)) {
                                        mUser.setLastAssigneeName(mAssignableUserName);
                                    }
                                    if (mCreateAnotherIssue) {
                                        mCreateAnotherCheckBox.setChecked(false);
                                        mTitleTextInput.setText(Constants.Symbols.EMPTY);
                                        initAttachmentsView();
                                        mDescriptionTextInput.setText(Constants.Symbols.EMPTY);
                                    } else {
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(CreateIssueActivity.this, getString(R.string.create_ticket_error, mAssignableUserName), Toast.LENGTH_LONG).show();
                                }
                                showProgress(false);
                            }
                        }
                );
            }
        });
    }

    private void initSummaryEditText() {
        mTitleTextInput = (TextInput) findViewById(R.id.summary_input);
        mTitleTextInput.setValidators(new ArrayList<Validator>() {{
            add(InputsUtil.getEmptyValidator());
            add(InputsUtil.getEndStartWhitespacesValidator());
        }});
        if (mBundle != null && mBundle.getString(ExpectedResultsActivity.NAME) != null) {
            mTitleTextInput.setText(mBundle.getString(ExpectedResultsActivity.NAME));
        }
        mTitlePoint = new int[2];
        mTitleTextInput.getLocationOnScreen(mTitlePoint);
    }

    private void initAssigneeAutocompleteView() {
        mAssignableAutocompleteView = (AutocompleteProgressView) findViewById(R.id.atv_assignable_users);
        mAssignableAutocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mUser.setLastAssigneeName(mAssignableUserName);
                mIsAssignableSelected = true;
            }
        });
        mAssignableAutocompleteView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mIsAssignableSelected = false;
                if (!mIsSelfSigned) {
                    if (count > 0) {
                        if (s.length() > 2) {
                            if (InputsUtil.getWhitespacesValidator().validate(mAssignableAutocompleteView)) {
                                Toast.makeText(CreateIssueActivity.this, getString(R.string.label_tester) + getString(R.string.label_no_whitespaces), Toast.LENGTH_LONG).show();
                            } else {
                                mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
                                mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, s), 750);
                            }
                        }
                    } else if (count == 0) {
                        mAssignableAutocompleteView.dismissDropDown();
                        mAssignableAutocompleteView.setAdapter(null);
                    }
                    mAssignableUserName = s.toString();
                } else {
                    mAssignableUserName = s.toString();
                    mIsSelfSigned = false;
                }
            }
        });

        if (mUser.getLastAssignee() != null) {
            mIsAssignableSelected = true;
            if (mUser.getLastAssignee().equals(mUser.getUserName())) {
                mIsSelfSigned = true;
            }
            mAssignableAutocompleteView.setText(mUser.getLastAssignee());
        }

        initAssignSelfButton();
    }

    private void initAssignSelfButton() {
        Button mAssignSelfButton = (Button) findViewById(R.id.btn_assign_self);
        mAssignSelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsSelfSigned = true;
                String userName = mUser.getUserName();
                mAssignableAutocompleteView.setText(userName);
                mUser.setLastAssigneeName(userName);
            }
        });
    }

    private void initAttachmentsView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.listScreens);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CreateIssueActivity.this);
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        loadAttachments();
    }

    private void initClearEnvironmentButton() {
        Button clearEnvironmentButton = (Button) findViewById(R.id.btn_clear_environment);
        clearEnvironmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelper.getClearEnvironmentDialog(CreateIssueActivity.this, new DialogHelper.IDialogButtonClick() {
                    @Override
                    public void positiveButtonClick() {
                        mEnvironmentTextInput.setText(Constants.Symbols.EMPTY);
                    }

                    @Override
                    public void negativeButtonClick() {
                    }
                }).show();
            }
        });
    }

    private void initGifAttachmentControls() {
        mGifCheckBox = (CheckBox) findViewById(R.id.cb_gif_attachment);
        mGifCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                if (mSteps != null) {
//                    if (!PreferenceUtil.getBoolean(getString(R.string.key_gif_info_dialog))) {
//                        DialogHelper.getGifInfoDialog(CreateIssueActivity.this).show();
//                    }
                    int stepsArraySize = mSteps.size();
                    if (isChecked && stepsArraySize != 0) {
                        mGifProgress.setMax(stepsArraySize);
                        mGifProgress.setIndeterminate(true);
                        mGifProgress.setVisibility(View.VISIBLE);
                        GifUtil.createGif(CreateIssueActivity.this, mSteps);
                    } else {
                        GifUtil.cancelGifCreating();
                        mGifProgress.setVisibility(View.GONE);
                    }
                } else {
                    mGifCheckBox.setChecked(false);
                    Toast.makeText(getBaseContext(), "Don't have step for make gif", Toast.LENGTH_LONG).show();
                }
            }
        });
        mGifProgress = (ProgressBar) findViewById(R.id.pb_gif_attachment);
        int progressColor = getResources().getColor(R.color.accent);
        mGifProgress.getProgressDrawable().setColorFilter(progressColor, PorterDuff.Mode.SRC_IN);
        mGifProgress.getIndeterminateDrawable().setColorFilter(progressColor, PorterDuff.Mode.SRC_IN);
    }

    private void initAttachLogsCheckBox() {
        CheckBox attachLogs = (CheckBox) findViewById(R.id.cb_attach_logs);
        attachLogs.setChecked(PreferenceUtil.getBoolean((getString(R.string.key_is_attach_logs))));
        attachLogs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtil.putBoolean(getString(R.string.key_is_attach_logs), isChecked);
                initAttachmentsView();
            }
        });
    }

    private void setAssignableNames(String s) {
        if (!mIsAssignableSelected) {
            mAssignableAutocompleteView.setEnabled(false);
            mAssignableAutocompleteView.showProgress(true);
            mJira.getUsersAssignable(s, new GetContentCallback<List<String>>() {
                @Override
                public void resultOfDataLoading(final List<String> result) {
                    if (result != null) {
                        ArrayAdapter<String> assignableUsersAdapter = new ArrayAdapter<>(CreateIssueActivity.this,
                                R.layout.spinner_dropdown_item, result);
                        mAssignableAutocompleteView.setThreshold(1);
                        mAssignableAutocompleteView.setAdapter(assignableUsersAdapter);
                        if (assignableUsersAdapter.getCount() > 0
                                && !mAssignableAutocompleteView.getText().toString().equals(assignableUsersAdapter.getItem(0))) {
                            mAssignableAutocompleteView.showDropDown();
                        }
                    }
                    mAssignableAutocompleteView.showProgress(false);
                    mAssignableAutocompleteView.setEnabled(true);
                }
            });
        }
    }

    private void showProgressIfNeed() {
        if (mCreateIssueButton != null) {
            if (!mRequestsQueue.isEmpty()) {
                showProgress(true);
                mCreateIssueButton.setEnabled(false);
            } else {
                showProgress(false);
                mCreateIssueButton.setEnabled(true);
            }
        }
    }

    private void loadAttachments() {
        loadSteps();
    }

    private void loadSteps() {
        ArrayList<File> fileList = FileUtil.getListFilePaths(new File(FileUtil.getCacheLocalDir()));
        List<Attachment> screenArray = new ArrayList<>();
        mAdapter = new AttachmentAdapter(CreateIssueActivity.this, screenArray, R.layout.adapter_attachment,
                CreateIssueActivity.this, CreateIssueActivity.this);
        for (File attachmentFile : fileList) {
            screenArray.add(new Attachment(attachmentFile.getPath()));
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ArrayList<String> extraShareFiles = extras.getStringArrayList(ShareFilesActivity.KEY_EXTRA_SHARE_FILES);
            if (extraShareFiles != null) {
                for (String filePath : extraShareFiles) {
                    if (!mAdapter.contains(filePath)) {
                        screenArray.add(new Attachment(filePath));
                    }
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mAdapter);
        }
        mRequestsQueue.remove(ContentConst.ATTACHMENT_RESPONSE);
        showProgressIfNeed();
//        LocalContent
//                .getAllSteps(new GetContentCallback<List<Step>>() {
//                    @Override
//                    public void resultOfDataLoading(final List<Step> result) {
//                        mSteps = result;
//                        List<Attachment> screenArray = mAttachmentManager.stepsToAttachments(result);
//                        mAdapter = new AttachmentAdapter(CreateIssueActivity.this, screenArray, R.layout.adapter_attachment,
//                                CreateIssueActivity.this, CreateIssueActivity.this);
//                        if (mAttachmentManager.stepsDescriptionToAttachments(result) != null) {
//                            mAdapter.addItem(mAdapter.getItemCount(), new Attachment(mAttachmentManager.stepsDescriptionToAttachments(result).getPath()));
//                        }
//                        if (mRecyclerView != null) {
//                            mRecyclerView.setAdapter(mAdapter);
//                        }
//                        if (mSteps == null || mSteps.size() == 0) {
//                            mGifCheckBox.setEnabled(false);
//                        }
//                        mRequestsQueue.remove(ContentConst.ATTACHMENT_RESPONSE);
//                        showProgressIfNeed();
//                    }
//                });
    }

    @Override
    public void onReloadData() {
        loadSteps();
    }

    @Override
    public void onShowMessage() {
        new AlertDialog.Builder(CreateIssueActivity.this, R.style.Dialog)
                .setTitle(R.string.title_notes_arent_applied)
                .setMessage(R.string.message_notes_arent_applied)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void removeStepFromDatabase(int position) {
        Attachment attachment = mAdapter.getAttachments().get(position);
        int stepId = attachment.getStepId();
        FileUtil.deleteRecursive(attachment.getFilePath());
        LocalContent.removeStep(new Step(stepId));
        mAdapter.getAttachments().remove(position);
        mAdapter.notifyItemRemoved(position);
        getDescription();
    }

    private void getDescription() {
        if (mDescriptionTextInput != null) {
            if (mBundle != null && mBundle.getString(ExpectedResultsActivity.STEPS) != null &&
                    mBundle.getString(ExpectedResultsActivity.EXPECTED_RESULT) != null) {
                GEntryWorksheet testcase = new GEntryWorksheet();
                testcase.setTestStepsGSX(mBundle.getString(ExpectedResultsActivity.STEPS));
                testcase.setExpectedResultGSX(mBundle.getString(ExpectedResultsActivity.EXPECTED_RESULT));
                mDescriptionTextInput.setText(testcase.getFullTestCaseDescription());
            }
            String descriptionTemplate = PreferenceUtil.getString(getResources().getString(R.string.key_description));
            mDescriptionTextInput.setText(mDescriptionTextInput.getText() + (!TextUtils.isEmpty(descriptionTemplate) ? "\n\n" + descriptionTemplate : ""));
        }
        mRequestsQueue.remove(ContentConst.DESCRIPTION_RESPONSE);
        showProgressIfNeed();
    }

    //Recycler
    @Override
    public void onItemRemove(final int position) {
        if (FileUtil.isPicture(mAdapter.getAttachments().get(position).getFilePath())) {
            mAdapter.getAttachments().remove(position);
            mAdapter.notifyDataSetChanged();
//            if (!PreferenceUtil.getBoolean(getString(R.string.key_step_deletion_dialog))) {
//                DialogHelper.getStepDeletionDialog(this, new DialogHelper.IDialogButtonClick() {
//                    @Override
//                    public void positiveButtonClick() {
//                        mGifCheckBox.setEnabled(true);
//                        removeStepFromDatabase(position);
//                    }
//
//                    @Override
//                    public void negativeButtonClick() {
//                    }
//                }).show();
//            } else {
//                removeStepFromDatabase(position);
//            }
        } else if (FileUtil.isText(mAdapter.getAttachments().get(position).getFilePath())) {
            DialogHelper.getAreYouSureDialog(this, getString(R.string.title_delete_log_dialiog),
                    getString(R.string.label_message_delete_log_dialiog), new DialogHelper.IDialogButtonClick() {
                        @Override
                        public void positiveButtonClick() {
                            mAdapter.getAttachments().remove(position);
                            mAdapter.notifyItemRemoved(position);
                        }

                        @Override
                        public void negativeButtonClick() {
                        }
                    }).show();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.key_is_attach_logs))) {
            initAttachmentsView();
        }
    }

    @Override
    public void onItemShow(int position) {
        Intent intent = null;
        String filePath = Constants.Symbols.EMPTY;
        if (mAdapter != null && mAdapter.getAttachmentFilePathList() != null && mAdapter.getAttachmentFilePathList().size() > position) {
            filePath = mAdapter.getAttachmentFilePathList().get(position);
        }
        if (filePath.endsWith(MimeType.IMAGE_PNG.getFileExtension()) ||
                filePath.endsWith(MimeType.IMAGE_JPG.getFileExtension()) ||
                filePath.endsWith(MimeType.IMAGE_JPEG.getFileExtension())) {
            intent = new Intent(this, PaintActivity.class);
            intent.putExtra(PaintActivity.KEY_SCREEN_PATH, mAdapter.getAttachments().get(position).getFilePath());
            startActivityForResult(intent, PAINT_ACTIVITY_REQUEST_CODE);
            return;
        } else if (filePath.endsWith(MimeType.TEXT_PLAIN.getFileExtension()) || filePath.endsWith(MimeType.TEXT_HTML.getFileExtension())) {
            intent = new Intent(this, LogActivity.class);
            intent.putExtra(LogActivity.FILE_PATH, filePath);
            startActivity(intent);
        } else if (filePath.endsWith(MimeType.IMAGE_GIF.getFileExtension())) {
            intent = new Intent(CreateIssueActivity.this, GifPlayerActivity.class);
            intent.putExtra(GifPlayerActivity.GIF_IMAGE_KEY, filePath);
            startActivity(intent);
        }
        if (TextUtils.isEmpty(filePath)) {
            if (intent != null) {
                startActivity(intent);
            }
        }
    }

    //Gif processing
    @Override
    public void onProgress(final int progress) {
        if (mGifProgress != null) {
            if (mGifProgress.isIndeterminate()) {
                mGifProgress.setIndeterminate(false);
            }
            mGifProgress.setProgress(progress);
        }
    }

    @Override
    public void onGifCreated() {
        if (mGifProgress != null && mAdapter != null) {
            mGifProgress.setVisibility(View.GONE);
            mAdapter.addItem(0, new Attachment(GifUtil.FILE_PATH));
            Toast.makeText(CreateIssueActivity.this, R.string.label_gif_created, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSavingError(Throwable throwable) {
        mGifProgress.setVisibility(View.GONE);
        mGifCheckBox.setChecked(false);
        mGifCheckBox.setEnabled(false);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if (throwable instanceof IOException) {
            dialogBuilder.setTitle(R.string.title_gif_isnt_saved).setMessage(R.string.message_gif_isnt_saved);
        } else if (throwable instanceof OutOfMemoryError) {
            dialogBuilder.setTitle(R.string.title_gif_cant_be_created).setMessage(R.string.message_gif_cant_be_created);
        } else {
            dialogBuilder.setTitle(R.string.title_gif_error).setMessage(R.string.message_gif_error);
        }
        dialogBuilder.create().show();
    }

    @Override
    public void onBackPressed() {
        DialogHelper.getAreYouSureDialog(this, getString(R.string.title_exit_dialog),
                getString(R.string.label_message_exit_dialog), new DialogHelper.IDialogButtonClick() {

                    @Override
                    public void positiveButtonClick() {
                        finish();
                    }

                    @Override
                    public void negativeButtonClick() {
                    }
                }).show();
    }
}
