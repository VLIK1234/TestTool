package amtt.epam.com.amtt.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.ticket.Attachment;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.googleapi.api.GoogleApiConst;
import amtt.epam.com.amtt.googleapi.api.loadcontent.GSpreadsheetContent;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
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
import amtt.epam.com.amtt.util.TestUtil;
import amtt.epam.com.amtt.util.Validator;


public class CreateIssueActivity extends BaseActivity
        implements AttachmentAdapter.ViewHolder.ClickListener,
        IResult<List<DatabaseEntity>>,
        SharedPreferences.OnSharedPreferenceChangeListener,
        GifUtil.ProgressListener {

    private static final int PAINT_ACTIVITY_REQUEST_CODE = 0;
    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final String DEFAULT_PRIORITY_ID = "3";
    public static final String BUG = "Bug";
    public static final String TASK = "Task";
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
    public Spinner mProjectNamesSpinner;
    private RecyclerView mRecyclerView;
    private Spinner mComponents;
    private Queue<ContentConst> mRequestsQueue = new LinkedList<>();
    private Button mCreateIssueButton;
    private ProgressBar mGifProgress;
    private List<Step> mSteps;
    private ScrollView mScrollView;
    private int[] mTitlePoint;
    private CheckBox mCreateAnotherCheckBox;
    private boolean mCreateAnotherIssue;
    private LayoutInflater mLayoutInflater;
    private boolean mDoesTopButtonShouldBeShown;
    private GEntryWorksheet mTestcase;

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
        TopButtonService.sendActionChangeTopButtonVisibility(false);
        PreferenceUtil.getPref().registerOnSharedPreferenceChangeListener(CreateIssueActivity.this);
        mHandler = new AssigneeHandler(this);
        initViews();
        mRequestsQueue.add(ContentConst.DESCRIPTION_RESPONSE);
        mRequestsQueue.add(ContentConst.ATTACHMENT_RESPONSE);
        showProgressIfNeed();
        initAttachmentsView();
        initAttachLogsCheckBox();
        initDescriptionEditText();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setDefaultConfigs();
        if (mDoesTopButtonShouldBeShown) {
            TopButtonService.sendActionChangeTopButtonVisibility(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TopButtonService.sendActionChangeTopButtonVisibility(false);
        mDoesTopButtonShouldBeShown = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PAINT_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    loadAttachments();
                }
                break;
        }
        PreferenceUtil.getPref().unregisterOnSharedPreferenceChangeListener(CreateIssueActivity.this);
    }

    private void setDefaultConfigs() {
        if (mComponents != null && mComponents.getSelectedItem() != null) {
            String component = JiraContent.getInstance().getComponentIdByName((String) mComponents.getSelectedItem());
            ActiveUser.getInstance().setLastComponentsIds(component);
        }
        JiraContent.getInstance().setDefaultConfig(ActiveUser.getInstance().getLastProjectKey(),
                ActiveUser.getInstance().getLastAssignee(), ActiveUser.getInstance().getLastComponentsIds());
    }

    private void initViews() {
        mRequestsQueue.add(ContentConst.PROJECTS_RESPONSE);
        mRequestsQueue.add(ContentConst.PRIORITIES_RESPONSE);
        initCreateIssueButton();
        initProjectNamesSpinner();
        initSummaryEditText();
        initEnvironmentEditText();
        initListStepButton();
        initPrioritiesSpinner();
        initCreateIssueButton();
        initCreateAnotherCheckBox();
        initClearEnvironmentButton();
        initGifAttachmentControls();
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        Bundle extra = getIntent().getExtras();
        if(extra!=null){
            mTestcase = GSpreadsheetContent.getInstance().getTestcaseByIdGSX(extra.getString(GoogleApiConst.LINK_TAG));
        }
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
        JiraContent.getInstance().getProjectsNames(new GetContentCallback<HashMap<JProjects, String>>() {
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
                                JiraContent.getInstance().getProjectNameByKey(ActiveUser.getInstance().getLastProjectKey(), new GetContentCallback<String>() {
                                    @Override
                                    public void resultOfDataLoading(String result) {
                                        if (result != null) {
                                            mProjectNamesSpinner.setSelection(projectsAdapter.getPosition(result));
                                        }
                                    }
                                });
                            } else {
                                mProjectNamesSpinner.setSelection(0);
                            }
                        }
                    });
                    mProjectNamesSpinner.setEnabled(true);
                }
                mRequestsQueue.remove(ContentConst.PROJECTS_RESPONSE);

            }
        });
        mProjectNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                JiraContent.getInstance().getProjectKeyByName((String) parent.getItemAtPosition(position), new GetContentCallback<String>() {
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
        JiraContent.getInstance().getPrioritiesNames(new GetContentCallback<HashMap<String, String>>() {
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
                            String defaultPriority;
                            if (mTestcase!=null) {
                                defaultPriority = mTestcase.getPriorityGSX();
                                if (defaultPriority != null) {
                                    prioritiesSpinner.setSelection(mPrioritiesAdapter.getPosition(defaultPriority));
                                }
                            } else {
                                defaultPriority = JiraContent.getInstance().getPriorityNameById(DEFAULT_PRIORITY_ID);
                                if (defaultPriority != null) {
                                    prioritiesSpinner.setSelection(mPrioritiesAdapter.getPosition(defaultPriority));
                                }
                            }
                            prioritiesSpinner.setEnabled(true);
                        }
                    });
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
        JiraContent.getInstance().getVersionsNames(projectKey, new GetContentCallback<HashMap<String, String>>() {
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
        JiraContent.getInstance().getComponentsNames(projectKey, new GetContentCallback<HashMap<String, String>>() {
            @Override
            public void resultOfDataLoading(HashMap<String, String> result) {
                if (result != null && result.size() > 0) {
                    ArrayList<String> componentsNames = new ArrayList<>();
                    componentsNames.addAll(result.values());
                    ArrayAdapter<String> componentsAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, componentsNames);
                    componentsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    componentsTextView.setVisibility(View.VISIBLE);
                    mComponents.setAdapter(componentsAdapter);
                    mComponents.setVisibility(View.VISIBLE);
                    mComponents.setEnabled(true);
                    if (ActiveUser.getInstance().getLastComponentsIds() != null) {
                        mComponents.setSelection(componentsAdapter.getPosition(JiraContent.getInstance().getComponentNameById(ActiveUser.getInstance().getLastComponentsIds())));
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
        JiraContent.getInstance().getIssueTypesNames(new GetContentCallback<List<String>>() {
            @Override
            public void resultOfDataLoading(final List<String> result) {
                if (result != null) {
                    CreateIssueActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            ArrayAdapter<String> issueTypesAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_layout, result);
                            issueTypesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            issueTypesSpinner.setAdapter(issueTypesAdapter);
                            if (ActiveUser.getInstance().getRecord()) {
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
                    });
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
        mEnvironmentTextInput.setText(SystemInfoHelper.getDeviceOsInfo());
    }

    private void initCreateIssueButton() {
        mCreateIssueButton = (Button) findViewById(R.id.btn_create);
        showProgressIfNeed();
        mCreateIssueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestUtil.restartTest();
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
                    ActiveUser.getInstance().setLastComponentsIds(JiraContent.getInstance().getComponentIdByName(components));

                }
                JiraContent.getInstance().createIssue(mIssueTypeName, mPriorityName, mVersionName, mTitleTextInput.getText().toString(),
                        mDescriptionTextInput.getText().toString(), mEnvironmentTextInput.getText().toString(),
                        mAssignableUserName, ActiveUser.getInstance().getLastComponentsIds(), new GetContentCallback<JCreateIssueResponse>() {
                            @Override
                            public void resultOfDataLoading(JCreateIssueResponse result) {
                                if (result != null) {
                                    AttachmentService.start(CreateIssueActivity.this, mAdapter.getAttachmentFilePathList());
                                    Toast.makeText(CreateIssueActivity.this, R.string.ticket_created, Toast.LENGTH_LONG).show();
                                    TopButtonService.stopRecord(CreateIssueActivity.this);
                                    if (mCreateAnotherIssue) {
                                        mCreateAnotherCheckBox.setChecked(false);
                                        mTitleTextInput.setText(Constants.Symbols.EMPTY);
                                        initAttachmentsView();
                                        mDescriptionTextInput.setText(Constants.Symbols.EMPTY);
                                    } else {
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(CreateIssueActivity.this, R.string.error, Toast.LENGTH_LONG).show();
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
        if (mTestcase != null) {
            mTitleTextInput.setText(mTestcase.getTestCaseNameGSX());
        }
        mTitlePoint = new int[2];
        mTitleTextInput.getLocationOnScreen(mTitlePoint);
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
                    if (InputsUtil.getWhitespacesValidator().validate(mAssignableAutocompleteView)) {
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
        initAssignSelfButton();
    }

    private void initAssignSelfButton() {
        Button mAssignSelfButton = (Button) findViewById(R.id.btn_assign_self);
        mAssignSelfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAssignableAutocompleteView.setText(ActiveUser.getInstance().getUserName());
            }
        });
    }

    private void initAttachmentsView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.listScreens);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CreateIssueActivity.this);
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
                new AlertDialog.Builder(CreateIssueActivity.this)
                        .setTitle(R.string.label_clear_environment)
                        .setMessage(R.string.message_clear_environment)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mEnvironmentTextInput.setText(Constants.Symbols.EMPTY);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void initGifAttachmentControls() {
        CheckBox gifCheckBox = (CheckBox) findViewById(R.id.cb_gif_attachment);
        gifCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (!PreferenceUtil.getBoolean(getString(R.string.key_gif_info_dialog))) {
                    new AlertDialog.Builder(CreateIssueActivity.this)
                            .setTitle(R.string.title_gif_info)
                            .setMessage(R.string.message_gif_info)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PreferenceUtil.putBoolean(getString(R.string.key_gif_info_dialog), true);
                                    dialog.dismiss();
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    PreferenceUtil.putBoolean(getString(R.string.key_gif_info_dialog), true);
                                }
                            })
                            .create()
                            .show();
                }

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
        mAssignableAutocompleteView.setEnabled(false);
        mAssignableAutocompleteView.showProgress(true);
        JiraContent.getInstance().getUsersAssignable(s, new GetContentCallback<List<String>>() {
            @Override
            public void resultOfDataLoading(List<String> result) {
                if (result != null) {
                    ArrayAdapter<String> assignableUsersAdapter = new ArrayAdapter<>(CreateIssueActivity.this, R.layout.spinner_dropdown_item, result);
                    mAssignableAutocompleteView.setThreshold(1);
                    mAssignableAutocompleteView.setAdapter(assignableUsersAdapter);
                    if (assignableUsersAdapter.getCount() > 0) {
                        if (!mAssignableAutocompleteView.getText().toString().equals(assignableUsersAdapter.getItem(0))) {
                            mAssignableAutocompleteView.showDropDown();
                        } else {
                            ActiveUser.getInstance().setLastAssigneeName(mAssignableAutocompleteView.getText().toString());
                        }
                    }

                }
                mAssignableAutocompleteView.showProgress(false);
                mAssignableAutocompleteView.setEnabled(true);
            }
        });
    }

    public void showProgressIfNeed() {
        if (!mRequestsQueue.isEmpty()) {
            showProgress(true);
            mCreateIssueButton.setEnabled(false);
        } else {
            showProgress(false);
            mCreateIssueButton.setEnabled(true);
        }
    }

    private void loadAttachments() {
        DbObjectManager.INSTANCE.getAll(new Step(), this);
    }

    private void removeStepFromDatabase(int position) {
        Attachment attachment = mAdapter.getAttachments().get(position);
        int stepId = attachment.mStepId;
        FileUtil.delete(attachment.mFilePath);
        DbObjectManager.INSTANCE.remove(new Step(stepId));
        mAdapter.getAttachments().remove(position);
        mAdapter.notifyItemRemoved(position);
        getDescription();
    }

    private void getDescription() {
        JiraContent.getInstance().getDescription(new GetContentCallback<Spanned>() {
            @Override
            public void resultOfDataLoading(final Spanned result) {
                if (result != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mDescriptionTextInput != null) {
                                if (mTestcase != null) {
                                    Spanned fullDescription = mTestcase.getFullTestCaseDescription(result);
                                    mDescriptionTextInput.setText(fullDescription);
                                } else {
                                    mDescriptionTextInput.setText(result);
                                }
                            }
                        }
                    });
                }
                mRequestsQueue.remove(ContentConst.DESCRIPTION_RESPONSE);
                showProgressIfNeed();
            }
        });
    }

    //Callbacks
    //IResult for attachments
    @Override
    public void onResult(final List<DatabaseEntity> result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result != null) {
                    mSteps = (List) result;
                    List<Attachment> screenArray = AttachmentManager.getInstance().getAttachmentList(result);
                    File externalCache = new File(Environment.getExternalStorageDirectory(), "Amtt_cache");
                    String template = externalCache.getPath() + "/%s";
                    String pathLogCommon = String.format(template, "log_common.txt");
                    String pathLogException = String.format(template, "log_exception.txt");
                    final File fileLogCommon = new File(pathLogCommon);
                    final File fileLogException = new File(pathLogException);
                    final Attachment attachLogCommon = new Attachment(pathLogCommon);
                    final Attachment attachLogException = new Attachment(pathLogException);
                    if (PreferenceUtil.getBoolean(getString(R.string.key_is_attach_logs))) {
                        if (fileLogCommon.exists() && fileLogException.exists()) {
                            screenArray.add(attachLogCommon);
                            screenArray.add(attachLogException);
                        }
                    }
                    mAdapter = new AttachmentAdapter(CreateIssueActivity.this, screenArray, R.layout.adapter_attachment);
                    mRecyclerView.setAdapter(mAdapter);
                }
                mRequestsQueue.remove(ContentConst.ATTACHMENT_RESPONSE);
                showProgressIfNeed();
            }
        });
    }

    @Override
    public void onError(Exception e) {

    }

    //Recycler
    @Override
    public void onItemRemove(final int position) {
        if (mLayoutInflater == null) {
            mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (FileUtil.isPicture(mAdapter.getAttachments().get(position).mFilePath)) {
            if (!PreferenceUtil.getBoolean(getString(R.string.key_step_deletion_dialog))) {
                View dialogView = mLayoutInflater.inflate(R.layout.dialog_step_deletion, null);
                CheckBox doNotShowAgain = (CheckBox) dialogView.findViewById(R.id.cb_do_not_show_again);
                doNotShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        PreferenceUtil.putBoolean(getString(R.string.key_step_deletion_dialog), isChecked);
                    }
                });

                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_step_deletion)
                        .setMessage(R.string.message_step_deletion)
                        .setView(dialogView)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeStepFromDatabase(position);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            } else {
                removeStepFromDatabase(position);
            }
        } else if (FileUtil.isText(mAdapter.getAttachments().get(position).mFilePath)) {
            removeStepFromDatabase(position);
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
        mDoesTopButtonShouldBeShown = false;
        Intent intent = null;
        String filePath = mAdapter.getAttachmentFilePathList().get(position);

        if (filePath.contains(MimeType.IMAGE_PNG.getFileExtension()) ||
                filePath.contains(MimeType.IMAGE_JPG.getFileExtension()) ||
                filePath.contains(MimeType.IMAGE_JPEG.getFileExtension())) {
            intent = new Intent(this, PaintActivity.class);
            intent.putExtra(PaintActivity.KEY_STEP_ID, mAdapter.getStepId(position));
            startActivityForResult(intent, PAINT_ACTIVITY_REQUEST_CODE);
            return;
        } else if (filePath.contains(MimeType.TEXT_PLAIN.getFileExtension())) {
            intent = new Intent(this, LogActivity.class);
            intent.putExtra(LogActivity.FILE_PATH, filePath);
            startActivity(intent);
        } else if (filePath.contains(MimeType.IMAGE_GIF.getFileExtension())) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file:///" + filePath), MimeType.IMAGE_GIF.getType());
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    //Gif processing
    @Override
    public void onProgress(int progress) {
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
        }
    }

    @Override
    public void onSavingError() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_gif_isnt_saved)
                .setMessage(R.string.message_gif_isnt_saved)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

}
