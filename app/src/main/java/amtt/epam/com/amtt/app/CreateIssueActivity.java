package amtt.epam.com.amtt.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.asynctask.CreateIssueTask;
import amtt.epam.com.amtt.asynctask.ShowUserDataTask;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreateIssue;
import amtt.epam.com.amtt.bo.issue.willrefactored.CreationIssueResult;
import amtt.epam.com.amtt.callbacks.CreationIssueCallback;
import amtt.epam.com.amtt.callbacks.ShowUserDataCallback;
import amtt.epam.com.amtt.util.Converter;
import amtt.epam.com.amtt.util.Logger;


public class CreateIssueActivity extends ActionBarActivity implements CreationIssueCallback, ShowUserDataCallback {

    private final String TAG = this.getClass().getSimpleName();
    private EditText etDescription, etSummary;
    private ArrayList<String> projectsNames = new ArrayList<>();
    private ArrayList<String> projectsKeys = new ArrayList<>();
    private Spinner inputProjectsKey, inputIssueTypes;
    private SharedPreferences sharedPreferences;

    //TODO the same constants used with preferences are defined twice
    private static final String USER_NAME = "username";
    private static final String PASSWORD = "password";
    private static final String URL = "url";
    private static final String NAME_SP = "data";
    private static final String VOID = "";
    private static final String PROJECTS_NAMES = "projectsNames";
    private static final String PROJECTS_KEYS = "projectsKeys";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_issue);
        sharedPreferences = getSharedPreferences(NAME_SP, MODE_PRIVATE);
        etDescription = (EditText) findViewById(R.id.et_description);
        etSummary = (EditText) findViewById(R.id.et_summary);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getProjectsNames());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputProjectsKey = (Spinner) findViewById(R.id.spin_projects_key);
        inputProjectsKey.setAdapter(adapter);
        inputProjectsKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String username, password, url;
                username = sharedPreferences.getString(USER_NAME, VOID);
                password = sharedPreferences.getString(PASSWORD, VOID);
                url = sharedPreferences.getString(URL, VOID);
                setVisibleProgress();
                new ShowUserDataTask(username, password, url, CreateIssueActivity.this).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public int getSelectedItemPositionProject() {
        return this.inputProjectsKey.getSelectedItemPosition();
    }

    public ArrayList<String> getProjectsNames() {
        projectsNames = Converter.setToArrayList(sharedPreferences.getStringSet(PROJECTS_NAMES, null));
        return projectsNames;
    }

    public ArrayList<String> getProjectsKeys() {
        projectsKeys = Converter.setToArrayList(sharedPreferences.getStringSet(PROJECTS_KEYS, null));
        return projectsKeys;
    }

    public String getProjectKey() {
        projectsKeys = getProjectsKeys();
        Logger.d(TAG, inputProjectsKey.getSelectedItem().toString());
        //TODO wtf?
        Logger.d(TAG, String.valueOf(projectsNames.size() - ((projectsNames.indexOf(inputProjectsKey.getSelectedItem().toString())) + 1)));
        return projectsKeys.get(projectsNames.size() - ((projectsNames.indexOf(inputProjectsKey.getSelectedItem().toString())) + 1));

    }

    public void onCreateIssueClick(View view) {
        CreateIssue issue = new CreateIssue();
        //TODO when it's correct use of object names?
        String mProjectKey, mIssueType, mDescription, mSummary;
        mDescription = etDescription.getText().toString();
        mSummary = etSummary.getText().toString();
        //TODO what if we click button before "new ShowUserDataTask()" will finish its work?
        mIssueType = inputIssueTypes.getSelectedItem().toString();
        mProjectKey = getProjectKey();
        //TODO we use this credentials many times in project.
        String username, password, url;
        username = sharedPreferences.getString(USER_NAME, VOID);
        password = sharedPreferences.getString(PASSWORD, VOID);
        url = sharedPreferences.getString(URL, VOID);
        setVisibleProgress();
        new CreateIssueTask(username, password, url, issue.createSimpleIssue(mProjectKey, mIssueType, mDescription, mSummary), CreateIssueActivity.this).execute();

    }

//    @Override
//    public void onCreationIssueResult(CreationIssueResult result) {
//        String resultMessage = result == CreationIssueResult.CREATION_UNSUCCESS ? getResources().getString(R.string.issue_creating_unsuccess) :
//                getResources().getString(R.string.issue_creating_success);
//        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
//        if (resultMessage.equals(getResources().getString(R.string.issue_creating_success))) {
//            setInisibleProgress();
//            finish();
//
//        }
//    }


    @Override
    public void onCreationIssueResult(String responseMessage) {

    }

    @Override
    public void onShowUserDataResult(JMetaResponse result) {
        int index = result.getProjects().size() - (getSelectedItemPositionProject() + 1);
        ArrayList<String> issueTypesNames = result.getProjects().get(index).getIssueTypesNames();
        ArrayAdapter<String> issueNames = new ArrayAdapter<>(CreateIssueActivity.this, android.R.layout.simple_spinner_item, issueTypesNames);
        issueNames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputIssueTypes = (Spinner) findViewById(R.id.spin_issue_name);
        inputIssueTypes.setAdapter(issueNames);
        //TODO misprint
        setInisibleProgress();
    }

    //TODO why not to move to common base activity?
    private void setVisibleProgress(){
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
    }
    private void setInisibleProgress(){
        findViewById(R.id.progress).setVisibility(View.GONE);
    }

}
