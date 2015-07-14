package amtt.epam.com.amtt.ui.activities;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.api.loadcontent.JiraContent;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.excel.api.loadcontent.XMLContent;
import amtt.epam.com.amtt.excel.bo.GoogleSpreadsheet;
import amtt.epam.com.amtt.exception.ExceptionType;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.Constants.Symbols;
import amtt.epam.com.amtt.util.DialogUtils;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.InputsUtil;
import amtt.epam.com.amtt.util.Logger;
import amtt.epam.com.amtt.util.Validator;
import amtt.epam.com.amtt.ui.views.TextInput;

/**
 * @author Artsiom_Kaliaha
 * @version on 07.05.2015
 */
public class LoginActivity extends BaseActivity implements Callback<JUserInfo>, LoaderCallbacks<Cursor> {

    private static final int SINGLE_USER_CURSOR_LOADER_ID = 1;

    private TextInput mUserNameTextInput;
    private TextInput mPasswordTextInput;
    private TextInput mUrlTextInput;

    private final String TAG = this.getClass().getSimpleName();
    private Button mLoginButton;
    private String mRequestUrl;
    private boolean mIsUserInDatabase;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        if (isNewUserAdditionFromUserInfo()) {
            JiraApi.get().signOut();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showKeyboard(mUserNameTextInput.getEdit());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initViews() {
        mUserNameTextInput = (TextInput) findViewById(R.id.username_input);
        mUserNameTextInput.setValidators(new ArrayList<Validator>() {{
            add(InputsUtil.getEmptyValidator());
            add(InputsUtil.getWhitespacesValidator());
            add(InputsUtil.getNoEmailValidator());
        }});
        mPasswordTextInput = (TextInput) findViewById(R.id.password_input);
        mPasswordTextInput.setValidators(new ArrayList<Validator>() {{
            add(InputsUtil.getEmptyValidator());
        }});
        mUrlTextInput = (TextInput) findViewById(R.id.url_input);
        mUrlTextInput.setValidators(new ArrayList<Validator>() {{
            add(InputsUtil.getEmptyValidator());
            add(InputsUtil.getEpamUrlValidator());
            add(InputsUtil.getCorrectUrlValidator());
        }});
        //showKeyboard(mUserNameTextInput.getEditText());
        mLoginButton = (Button) findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFields();
                hideKeyboard();
            }
        });
    }

    private void sendAuthRequest() {
        String userName = mUserNameTextInput.getText().toString();
        mRequestUrl = mUrlTextInput.getText().toString();
        String password = mPasswordTextInput.getText().toString();
        //getClient user info and perform auth in one request
        String requestSuffix = JiraApiConst.USER_INFO_PATH + mUserNameTextInput.getText().toString();
        JiraApi.get().searchData(requestSuffix, UserInfoProcessor.NAME, userName, password, mRequestUrl, this);
    }

    private void insertUserToDatabase(final JUserInfo user) {
        DbObjectManager.INSTANCE.add(user, new IResult<Integer>() {
            @Override
            public void onResult(Integer result) {
                ActiveUser.getInstance().setId(result);
                finish();
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    private void checkFields() {
        if (!mUserNameTextInput.validate() | !mPasswordTextInput.validate() | !mUrlTextInput.validate()) {
            return;
        }
        showProgress(true);
        mLoginButton.setEnabled(false);
        StepUtil.checkUser(mUserNameTextInput.getText().toString(), new IResult<List<JUserInfo>>() {
            @Override
            public void onResult(List<JUserInfo> result) {
                mIsUserInDatabase = result.size() > 0;
                ActiveUser.getInstance().clearActiveUser();
                sendAuthRequest();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void setActiveUser() {
        ActiveUser.getInstance().clearActiveUser();
        final ActiveUser activeUser = ActiveUser.getInstance();
        final String userName = mUserNameTextInput.getText().toString();
        final String password = mPasswordTextInput.getText().toString();
        activeUser.setCredentials(userName, password, mRequestUrl);
        activeUser.setUserName(userName);
        activeUser.setUrl(mUrlTextInput.getText().toString());
        ScheduledExecutorService worker =
                Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            public void run() {
                TopButtonService.start(getBaseContext());
                JiraContent.getInstance().getPrioritiesNames(new GetContentCallback<HashMap<String, String>>() {
                    @Override
                    public void resultOfDataLoading(HashMap<String, String> result) {
                        if (result != null) {
                            Logger.d(TAG, "Loading priority finish");
                        }
                    }
                });
                JiraContent.getInstance().getProjectsNames(new GetContentCallback<HashMap<JProjects, String>>() {
                    @Override
                    public void resultOfDataLoading(HashMap<JProjects, String> result) {
                        if (result != null) {
                            Logger.d(TAG, "Loading projects finish");
                        }
                    }
                });
                XMLContent.getInstance().getSpreadsheet(new GetContentCallback<GoogleSpreadsheet>() {
                    @Override
                    public void resultOfDataLoading(GoogleSpreadsheet result) {
                        if (result != null) {
                            Logger.d(TAG, "Loading spreadsheet finish");
                        }
                    }
                });
            }
        };
        worker.schedule(task, 1, TimeUnit.SECONDS);
    }

    private boolean isNewUserAdditionFromUserInfo() {
        return ActiveUser.getInstance().getUrl() != null;
    }

    //Callbacks
    //Jira
    @Override
    public void onLoadStart() {

    }

    @Override
    public void onLoadExecuted(JUserInfo user) {
        showProgress(false);
        if (user != null && !mIsUserInDatabase) {
            user.setUrl(mUrlTextInput.getText().toString());
            setActiveUser();
            user.setCredentials(ActiveUser.getInstance().getCredentials());
            insertUserToDatabase(user);
            Toast.makeText(this, R.string.auth_passed, Toast.LENGTH_SHORT).show();
        } else {
            setActiveUser();
            Toast.makeText(this, R.string.auth_passed, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onLoadError(Exception e) {
        DialogUtils.createDialog(this, ExceptionType.valueOf(e)).show();
        showProgress(false);
        mLoginButton.setEnabled(true);
    }

    //Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if (id == SINGLE_USER_CURSOR_LOADER_ID) {
            loader = new CursorLoader(LoginActivity.this,
                    AmttUri.USER.get(),
                    null,
                    UsersTable._ID + "=?",
                    new String[]{String.valueOf(args.getLong(AmttActivity.KEY_USER_ID))},
                    null);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try {
            switch (loader.getId()) {
                case SINGLE_USER_CURSOR_LOADER_ID:
                    JUserInfo user = new JUserInfo(data);
                    mUserNameTextInput.setText(user.getName());
                    mUrlTextInput.setText(user.getUrl());
                    mPasswordTextInput.setText(Symbols.EMPTY);
                    break;
            }
        } finally {
            IOUtils.close(data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

}


