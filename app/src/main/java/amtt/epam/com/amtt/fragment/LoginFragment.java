package amtt.epam.com.amtt.fragment;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;

import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.view.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.exception.ExceptionHandler;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.service.TopButtonService;
import amtt.epam.com.amtt.util.CredentialsManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements JiraCallback<String>, LoaderManager.LoaderCallbacks<Cursor> {

    public static interface FragmentLoginCallback {

        void onUserLoggedIn();

    }

    private EditText mUserName;
    private EditText mPassword;
    private EditText mUrl;
    private String mToastText = Constants.Str.EMPTY;
    private Button mLoginButton;
    private CheckBox mEpamJira;

    private InputMethodManager mInputManager;

    public LoginFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(layout);
        mInputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mInputManager.hideSoftInputFromWindow(mUserName.getWindowToken(), NO_FLAGS);
    }

    private void initViews(View layout) {
        mProgressBar = (ProgressBar) layout.findViewById(android.R.id.progress);
        mUserName = (amtt.epam.com.amtt.view.EditText) layout.findViewById(R.id.user_name);
        mPassword = (EditText) layout.findViewById(R.id.password);
        mUrl = (EditText) layout.findViewById(R.id.jira_url);
        mEpamJira = (CheckBox) layout.findViewById(R.id.epam_jira_checkbox);

        mUrl.setText("https://amtt02.atlassian.net");
        mUserName.setText("artsiom_kaliaha");

        mLoginButton = (Button) layout.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                checkFields();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void sendAuthRequest() {
        String requestUrl = mEpamJira.isChecked() ? mUrl.getText().toString() + JiraApiConst.EPAM_JIRA_SUFFIX : mUrl.getText().toString();
        RestMethod<String> authMethod = JiraApi.getInstance().buildAuth(mUserName.getText().toString(), mPassword.getText().toString(), requestUrl);
        new JiraTask.Builder<String>()
                .setRestMethod(authMethod)
                .setCallback(LoginFragment.this)
                .createAndExecute();
    }

    private void isUserAlreadyInDatabase() {
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, LoginFragment.this);
    }

    private void insertUserToDatabase() {
        ContentValues userValues = new ContentValues();
        userValues.put(UsersTable._USER_NAME, mUserName.getText().toString());
        userValues.put(UsersTable._PASSWORD, mPassword.getText().toString());
        getActivity().getContentResolver().insert(AmttContentProvider.USER_CONTENT_URI, userValues);
        ((FragmentLoginCallback) getActivity()).onUserLoggedIn();
    }

    private void checkFields() {
        if (TextUtils.isEmpty(mUserName.getText().toString())) {
            mToastText = getActivity().getString(R.string.enter_prefix) + getActivity().getString(R.string.enter_username);
        }
        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            if (TextUtils.isEmpty(mToastText)) {
                mToastText = getActivity().getString(R.string.enter_prefix) + getActivity().getString(R.string.enter_password);
            } else {
                mToastText += Constants.Str.COMMA + getActivity().getString(R.string.enter_password);
            }
        }
        if (TextUtils.isEmpty(mUrl.getText().toString())) {
            if (TextUtils.isEmpty(mToastText)) {
                mToastText = getActivity().getString(R.string.enter_prefix) + getActivity().getString(R.string.enter_url);
            } else {
                mToastText += Constants.Str.COMMA + getActivity().getString(R.string.enter_url);
            }
        }
        if (!TextUtils.isEmpty(mToastText)) {
            Toast.makeText(getActivity(), mToastText, Toast.LENGTH_LONG).show();
        } else {
            isUserAlreadyInDatabase();
        }
        mToastText = Constants.Str.EMPTY;
    }

    //Callbacks
    //Jira
    @Override
    public void onRequestStarted() {
        setProgressVisibility(View.VISIBLE);
        mLoginButton.setEnabled(false);
    }

    @Override
    public void onRequestPerformed(RestResponse<String> restResponse) {
        setProgressVisibility(View.GONE);
        mLoginButton.setEnabled(true);
        String resultMessage = restResponse.getResultObject();
        CredentialsManager.getInstance().setUrl(mUrl.getText().toString());
        CredentialsManager.getInstance().setCredentials(mUserName.getText().toString(), mPassword.getText().toString());
        CredentialsManager.getInstance().setAccess(true);
        TopButtonService.authSuccess(getActivity());
        Toast.makeText(getActivity(), resultMessage, Toast.LENGTH_SHORT).show();
        insertUserToDatabase();
        TopButtonService.authSuccess(getActivity());
    }

    @Override
    public void onRequestError(AmttException e) {
        ExceptionHandler.getInstance().processError(e).showDialog(getActivity(), LoginFragment.this);
        setProgressVisibility(View.GONE);
        mLoginButton.setEnabled(true);
    }

    //Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                AmttContentProvider.USER_CONTENT_URI,
                UsersTable.PROJECTION,
                UsersTable._USER_NAME + "=?",
                new String[] {mUserName.getText().toString()},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0) {
            sendAuthRequest();
        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.user_already_exists), Toast.LENGTH_SHORT).show();
            setProgressVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
