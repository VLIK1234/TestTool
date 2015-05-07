package amtt.epam.com.amtt.app;

import android.os.Bundle;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.exception.ExceptionHandler;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.JiraOperationResult;
import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.view.TextView;

/**
 * Created by Artsiom_Kaliaha on 07.05.2015.
 */
public class UserInfoActivity extends  BaseActivity implements JiraCallback<JiraUserInfo> {

    private TextView mName;
    private TextView mEmailAddress;
    private TextView mDisplayName;
    private TextView mTimeZone;
    private TextView mLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mName = (TextView) findViewById(R.id.user_name);
        mEmailAddress = (TextView) findViewById(R.id.user_email);
        mDisplayName = (TextView) findViewById(R.id.user_display_name);
        mTimeZone = (TextView) findViewById(R.id.user_time_zone);
        mLocale = (TextView) findViewById(R.id.user_locale);
        executeAsynchronously();
    }

    @SuppressWarnings("unchecked")
    private void executeAsynchronously() {
        String requestSuffix = JiraApiConst.USER_INFO_PATH + CredentialsManager.getInstance().getUserName() + JiraApiConst.EXPAND_GROUPS;
        RestMethod<JiraUserInfo> userInfoMethod = JiraApi.getInstance().buildDataSearch(requestSuffix, new UserInfoProcessor());
        new JiraTask.Builder<JiraUserInfo>()
                .setRestMethod(userInfoMethod)
                .setCallback(UserInfoActivity.this)
                .createAndExecute();
    }

    @Override
    public void onRequestStarted() {
        showProgress(true);
    }

    @Override
    public void onRequestPerformed(RestResponse<JiraUserInfo> restResponse) {

        if (restResponse.getOpeartionResult() == JiraOperationResult.REQUEST_PERFORMED) {
            JiraUserInfo user = restResponse.getResultObject();
            mName.setText(getResources().getString(R.string.label_user_name) + Constants.Str.COLON + user.getName());
            mEmailAddress.setText(getResources().getString(R.string.label_email) + Constants.Str.COLON + user.getEmailAddress());
            mDisplayName.setText(user.getDisplayName());
            mTimeZone.setText(getResources().getString(R.string.label_time_zone) + Constants.Str.COLON + user.getTimeZone());
            mLocale.setText(getResources().getString(R.string.label_locale) + Constants.Str.COLON + user.getLocale());
            showProgress(false);

        }
    }

    @Override
    public void onRequestError(AmttException e) {
        ExceptionHandler.getInstance().processError(e).showDialog(this, UserInfoActivity.this);
        showProgress(false);
    }

}
