package amtt.epam.com.amtt.fragment;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.AuthorizationResult;
import amtt.epam.com.amtt.service.TopButtonService;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.util.Logger;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements JiraCallback<AuthorizationResult,Void> {

    private EditText mUserName;
    private EditText mPassword;
    private EditText mUrl;
    private String mToastText = Constants.SharedPreferenceKeys.VOID;
    private Button mLoginButton;
    private ProgressBar mProgress;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_login, container, false);

        mProgress = (ProgressBar)layout.findViewById(android.R.id.progress);
        mUserName = (EditText) layout.findViewById(R.id.user_name);
        mPassword = (EditText) layout.findViewById(R.id.password);
        mUrl = (EditText) layout.findViewById(R.id.jira_url);

        mUrl.setText("https://amttpr.atlassian.net");
        mUserName.setText("iryna_monchanka");

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
                    showProgress(true);
                    CredentialsManager.getInstance().setUrl(mUrl.getText().toString());
                    CredentialsManager.getInstance().setCredentials(mUserName.getText().toString(), mPassword.getText().toString());
                    new JiraTask.Builder<AuthorizationResult, Void>()
                            .setOperationType(JiraTask.JiraTaskType.AUTH)
                            .setCallback(LoginFragment.this)
                            .create()
                            .execute();

                    mLoginButton.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(mToastText)) {
                    Toast.makeText(getActivity(), mToastText, Toast.LENGTH_LONG).show();
                }
                mToastText = "";
            }
        });

        return layout;
    }

    @Override
    public void onJiraRequestPerformed(RestResponse<AuthorizationResult,Void> restResponse) {
        Toast.makeText(getActivity(), restResponse.getMessage(), Toast.LENGTH_SHORT).show();
        showProgress(false);
        mLoginButton.setVisibility(View.VISIBLE);
        if (restResponse.getResult() == AuthorizationResult.SUCCESS) {
            CredentialsManager.getInstance().setUserName(mUserName.getText().toString());
            CredentialsManager.getInstance().setUrl(mUrl.getText().toString());
            CredentialsManager.getInstance().setAccess(true);
            TopButtonService.authSuccess(getActivity());
        }
    }

    private void showProgress(boolean show) {
        if (mProgress != null) {
            mProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

}
