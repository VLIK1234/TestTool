package amtt.epam.com.amtt.app;

import android.os.Bundle;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.exception.ExceptionHandler;
import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.bo.user.JiraUserInfo;
import amtt.epam.com.amtt.api.result.JiraOperationResult;
import amtt.epam.com.amtt.view.TextView;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.util.CredentialsManager;
import amtt.epam.com.amtt.util.UtilConstants;

public class UserInfoActivity extends BaseActivity implements JiraCallback<JiraUserInfo> {

    private TextView mName;
    private TextView mEmailAddress;
    private TextView mDisplayName;
    private TextView mTimeZone;
    private TextView mLocale;
    private TextView mSize;
    private TextView mNamesGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mName = (TextView) findViewById(R.id.tv_name);
        mEmailAddress = (TextView) findViewById(R.id.tv_email_address);
        mDisplayName = (TextView) findViewById(R.id.tv_display_name);
        mTimeZone = (TextView) findViewById(R.id.tv_time_zone);
        mLocale = (TextView) findViewById(R.id.tv_locale);
        mSize = (TextView) findViewById(R.id.tv_size);
        mNamesGroups = (TextView) findViewById(R.id.tv_names);
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
            mName.setText(getResources().getString(R.string.label_user_name) + UtilConstants.SharedPreference.COLON + user.getName());
            mEmailAddress.setText(getResources().getString(R.string.label_email) + UtilConstants.SharedPreference.COLON + user.getEmailAddress());
            mDisplayName.setText(user.getDisplayName());
            mTimeZone.setText(getResources().getString(R.string.label_time_zone) + UtilConstants.SharedPreference.COLON + user.getTimeZone());
            mLocale.setText(getResources().getString(R.string.label_locale) + UtilConstants.SharedPreference.COLON + user.getLocale());
            mSize.setText(getResources().getString(R.string.label_size) + UtilConstants.SharedPreference.COLON + String.valueOf(user.getGroups().getSize()));
            String groups = "";
            for (int i = 0; i < user.getGroups().getItems().size(); i++) {
                groups += user.getGroups().getItems().get(i).getName() + UtilConstants.Dialog.NEW_LINE;
            }
            mNamesGroups.setText(getResources().getString(R.string.label_names_groups) + UtilConstants.SharedPreference.COLON + groups);
            showProgress(false);

        }
    }

    @Override
    public void onRequestError(AmttException e) {
        ExceptionHandler.getInstance().processError(e).showDialog(this, UserInfoActivity.this);
        showProgress(false);
    }
}
