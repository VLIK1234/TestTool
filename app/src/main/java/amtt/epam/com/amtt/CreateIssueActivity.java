package amtt.epam.com.amtt;

import amtt.epam.com.amtt.bo.CreateIssue;
import amtt.epam.com.amtt.bo.CreationIssueResult;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.util.CreateMetaUtil;
import amtt.epam.com.amtt.callbacks.CreationIssueCallback;
import amtt.epam.com.amtt.callbacks.ShowUserDataCallback;
import amtt.epam.com.amtt.loaders.ProjectsLoader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class CreateIssueActivity extends ActionBarActivity implements CreationIssueCallback, ShowUserDataCallback, LoaderManager.LoaderCallbacks<JMetaResponse> {

    private EditText etUsername, etPassword, etDescription, etSummary;
    private ArrayList<String> projectsNames;
    private ArrayList<String> issueTypesNames;
    private Spinner etProjectKey, etIssyeType;
    private ArrayAdapter<String> adapter3;
    private static final String EXTRA_USER_LOGIN = "user";
    private static final String EXTRA_USER_PASSWORD = "password";
    private static final String EXTRA_URL = "url";
    private SharedPreferences sPref;

    public static final int SIGN_IN_LOADER_ID = 0x0000001;
    public static final int USER_BY_ID_LOADER_ID = 0x0000002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signIn();
        setContentView(R.layout.activity_create_issue);
        sPref = getSharedPreferences("data", MODE_PRIVATE);
        Set<String> pNames = new HashSet();
        pNames = sPref.getStringSet("projectsNames", null);
        if (pNames != null) {
            for (String name : pNames) {

                projectsNames.add(name);

            }
            Log.i("SIZE", String.valueOf(projectsNames.size()));
        }

        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        etDescription = (EditText) findViewById(R.id.et_description);
        etSummary = (EditText) findViewById(R.id.et_summary);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, projectsNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etProjectKey = (Spinner) findViewById(R.id.et_projectkey);
        etProjectKey.setAdapter(adapter);
        etProjectKey.setPrompt("Project");
        etProjectKey.setSelection(0);
        etProjectKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter3 = new ArrayAdapter<String>(CreateIssueActivity.this, android.R.layout.simple_spinner_item, issueTypesNames);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                etIssyeType = (Spinner) findViewById(R.id.et_issue_name);
                etIssyeType.setAdapter(adapter3);
                etIssyeType.setPrompt("Issue");
                etIssyeType.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


    }

    public int getSelectedItemPositionCountry() {
        return this.etProjectKey.getSelectedItemPosition();
    }


    public void onCreateIssueClick(View view) {
        CreateIssue issue = new CreateIssue();
        String mProjectKey, mIssyeType, mDescription, mSummary;
        //  mProjectKey = etProjectKey.getText().toString();
        //  mIssyeType = etIssyeType.getText().toString();
        mDescription = etDescription.getText().toString();
        mSummary = etSummary.getText().toString();

        mIssyeType = etIssyeType.getSelectedItem().toString();


        // String[] choose = getResources().getStringArray(R.array.cities);
        // userCountry =  choose[getSelectedItemPositionCountry()];

        //  new CreateIssueTask(etUsername.getText().toString(), etPassword.getText().toString(), issue.createSimpleIssue(mProjectKey, mIssyeType, mDescription, mSummary), CreateIssueActivity.this).execute();

    }

    @Override
    public void onCreationIssueResult(CreationIssueResult result) {
        String resultMessage = result == CreationIssueResult.CREATION_UNSUCCESS ? getResources().getString(R.string.issue_creating_unsuccess) :
            getResources().getString(R.string.issue_creating_success);
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowUserDataResult(JMetaResponse result) {

        CreateMetaUtil createMetaUtil = new CreateMetaUtil();

        projectsNames = createMetaUtil.getProjectsNames(result);
        issueTypesNames = createMetaUtil.getIssueTypesNames(result.getProjects().get(getSelectedItemPositionCountry()));
    }

    private void signIn() {

        sPref = getSharedPreferences("data", MODE_PRIVATE);
        String username, password, url;
        username = sPref.getString("username", "");
        password = sPref.getString("password", "");
        url = sPref.getString("url", "");
        performInLoader(username, password, url);
    }

    @Override
    public Loader<JMetaResponse> onCreateLoader(int id, Bundle bundle) {
        return new ProjectsLoader(this, bundle.getString(EXTRA_USER_LOGIN), bundle.getString(EXTRA_USER_LOGIN), bundle.getString(EXTRA_URL));
    }

    @Override
    public void onLoadFinished(Loader<JMetaResponse> loader, JMetaResponse userData) {
        CreateMetaUtil createMetaUtil = new CreateMetaUtil();
        projectsNames = createMetaUtil.getProjectsNames(userData);
        issueTypesNames = createMetaUtil.getIssueTypesNames(userData.getProjects().get(getSelectedItemPositionCountry()));
    }

    @Override
    public void onLoaderReset(Loader<JMetaResponse> loader) {
        //do nothing
    }

    private void performInLoader(String etUsername, String etPassword, String url) {
        if (etUsername == null) {
            return;
        }
        if (etPassword == null) {
            return;
        }
        if (url == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USER_LOGIN, etUsername);
        bundle.putString(EXTRA_USER_PASSWORD, etPassword);
        bundle.putString(EXTRA_URL, url);
        Loader loader = getSupportLoaderManager().getLoader(SIGN_IN_LOADER_ID);
        if (loader == null) {
            getSupportLoaderManager().initLoader(SIGN_IN_LOADER_ID, bundle, this);
        } else {
            getSupportLoaderManager().restartLoader(SIGN_IN_LOADER_ID, bundle, this);
        }
    }

}
