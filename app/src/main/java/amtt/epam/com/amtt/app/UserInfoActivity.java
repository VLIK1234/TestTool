package amtt.epam.com.amtt.app;

import android.os.Bundle;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.JiraTask.JiraSearchType;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.UserDataResult;
import amtt.epam.com.amtt.bo.user.JiraUserInfo;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.view.TextView;

public class UserInfoActivity extends BaseActivity implements JiraCallback<UserDataResult,JiraUserInfo> {

    private TextView name, emailAddress, displayName, timeZone, locale, size, namesGroups;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        name = (TextView) findViewById(R.id.tv_name);
        emailAddress = (TextView) findViewById(R.id.tv_email_address);
        displayName = (TextView) findViewById(R.id.tv_display_name);
        timeZone = (TextView) findViewById(R.id.tv_time_zone);
        locale = (TextView) findViewById(R.id.tv_locale);
        size = (TextView) findViewById(R.id.tv_size);
        namesGroups = (TextView) findViewById(R.id.tv_names);
        showProgress(true);
        new JiraTask.Builder<UserDataResult,JiraUserInfo>()
                .setOperationType(JiraTask.JiraTaskType.SEARCH)
                .setSearchType(JiraSearchType.USER_INFO)
                .setCallback(UserInfoActivity.this)
                .create()
                .execute();

    }

    @Override
    public void onJiraRequestPerformed(RestResponse<UserDataResult, JiraUserInfo> restResponse) {
        JiraUserInfo user = restResponse.getResultObject();
        name.setText(getResources().getString(R.string.label_user_name) + Constants.SharedPreferenceKeys.COLON + user.getName());
        emailAddress.setText(getResources().getString(R.string.label_email) + Constants.SharedPreferenceKeys.COLON + user.getEmailAddress());
        displayName.setText(user.getDisplayName());
        timeZone.setText(getResources().getString(R.string.label_time_zone) + Constants.SharedPreferenceKeys.COLON + user.getTimeZone());
        locale.setText(getResources().getString(R.string.label_locale) + Constants.SharedPreferenceKeys.COLON + user.getLocale());
        size.setText(getResources().getString(R.string.label_size) + Constants.SharedPreferenceKeys.COLON + String.valueOf(user.getGroups().getSize()));
        String groups = "";
        for (int i = 0; i < user.getGroups().getItems().size(); i++) {
            groups += user.getGroups().getItems().get(i).getName() + Constants.DialogKeys.NEW_LINE;
        }
        namesGroups.setText(getResources().getString(R.string.label_names_groups) + Constants.SharedPreferenceKeys.COLON + groups);
        showProgress(false);
    }

}
