package amtt.epam.com.amtt.app;

import android.os.Bundle;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.JiraTask.JiraSearchType;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.UserDataResult;
import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.util.Constants;

public class UserInfoActivity extends BaseActivity implements JiraCallback<UserDataResult,JiraUserInfo> {

    private TextView name, emailAddress, displayName, timeZone, locale, size, namesGroups;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_info);
        name = (TextView) findViewById(R.id.user_name);
        emailAddress = (TextView) findViewById(R.id.user_email);
        displayName = (TextView) findViewById(R.id.user_display_name);
        timeZone = (TextView) findViewById(R.id.user_time_zone);
        locale = (TextView) findViewById(R.id.user_locale);
        size = (TextView) findViewById(R.id.user_group_size);
        namesGroups = (TextView) findViewById(R.id.user_groups_names);
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
        name.setText(getResources().getString(R.string.user_name) + Constants.SharedPreferenceKeys.COLON + user.getName());
        emailAddress.setText(getResources().getString(R.string.user_email) + Constants.SharedPreferenceKeys.COLON + user.getEmailAddress());
        displayName.setText(user.getDisplayName());
        timeZone.setText(getResources().getString(R.string.time_zone) + Constants.SharedPreferenceKeys.COLON + user.getTimeZone());
        locale.setText(getResources().getString(R.string.locale) + Constants.SharedPreferenceKeys.COLON + user.getLocale());
        size.setText(getResources().getString(R.string.size) + Constants.SharedPreferenceKeys.COLON + String.valueOf(user.getGroups().getSize()));
        String groups = "";
        for (int i = 0; i < user.getGroups().getItems().size(); i++) {
            groups += user.getGroups().getItems().get(i).getName() + Constants.DialogKeys.NEW_LINE;
        }
        namesGroups.setText(getResources().getString(R.string.names_groups) + Constants.SharedPreferenceKeys.COLON + groups);
        showProgress(false);
    }

}
