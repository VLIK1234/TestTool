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
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.AuthorizationResult;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.service.TopButtonService;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.CredentialsManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements JiraCallback<AuthorizationResult, Void>, LoaderManager.LoaderCallbacks<Cursor> {

    public static interface FragmentLoginCallback {

        void onUserLoggedIn();

    }

    private EditText mUserName;
    private EditText mPassword;
    private EditText mUrl;
    private String mToastText = Constants.SharedPreferenceKeys.VOID;
    private Button mLoginButton;

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
        mUserName = (EditText) layout.findViewById(R.id.user_name);
        mPassword = (EditText) layout.findViewById(R.id.password);
        mUrl = (EditText) layout.findViewById(R.id.jira_url);

        mUrl.setText("https://amtt02.atlassian.net");
        mUserName.setText("artsiom_kaliaha");

        mLoginButton = (Button) layout.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mUserName.getText().toString())) {
                    mToastText += (Constants.DialogKeys.INPUT_USERNAME + Constants.DialogKeys.NEW_LINE);
                }
                if (TextUtils.isEmpty(mPassword.getText().toString())) {
                    mToastText += (Constants.DialogKeys.INPUT_PASSWORD + Constants.DialogKeys.NEW_LINE);
                }
                if (TextUtils.isEmpty(mUrl.getText().toString())) {
                    mToastText += (Constants.DialogKeys.INPUT_URL);
                } else {
                    setProgressVisibility(View.VISIBLE);
                    CredentialsManager.getInstance().setUrl(mUrl.getText().toString());
                    CredentialsManager.getInstance().setCredentials(mUserName.getText().toString(), mPassword.getText().toString());

                    getLoaderManager().initLoader(CURSOR_LOADER_ID, null, LoginFragment.this);
                    mLoginButton.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(mToastText)) {
                    Toast.makeText(getActivity(), mToastText, Toast.LENGTH_LONG).show();
                }
                mToastText = "";
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void sendAuthRequest() {
        new JiraTask.Builder<AuthorizationResult, Void>()
                .setOperationType(JiraTask.JiraTaskType.AUTH)
                .setCallback(LoginFragment.this)
                .create()
                .execute();
    }

    private void insertUserToDatabase() {
        ContentValues userValues = new ContentValues();
        userValues.put(UsersTable._USER_NAME, mUserName.getText().toString());
        userValues.put(UsersTable._PASSWORD, mPassword.getText().toString());
        getActivity().getContentResolver().insert(AmttContentProvider.USER_CONTENT_URI, userValues);
        ((FragmentLoginCallback) getActivity()).onUserLoggedIn();
    }

    //Callbacks
    @Override
    public void onJiraRequestPerformed(RestResponse<AuthorizationResult, Void> restResponse) {
        Toast.makeText(getActivity(), restResponse.getMessage(), Toast.LENGTH_SHORT).show();
        setProgressVisibility(View.GONE);
        mLoginButton.setVisibility(View.VISIBLE);
        if (restResponse.getResult() == AuthorizationResult.SUCCESS) {
            CredentialsManager.getInstance().setUserName(mUserName.getText().toString());
            CredentialsManager.getInstance().setUrl(mUrl.getText().toString());
            CredentialsManager.getInstance().setAccess(true);
            TopButtonService.authSuccess(getActivity());
            insertUserToDatabase();
        }
    }

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
