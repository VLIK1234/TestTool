package amtt.epam.com.amtt.app;

import android.os.Bundle;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.JiraTask.JiraSearchType;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.JiraOperationResult;
import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.util.UtilConstants;

public class UserInfoActivity extends BaseActivity implements JiraCallback<JiraUserInfo> {

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
        new JiraTask.Builder<JiraUserInfo>()
                .setOperationType(JiraTask.JiraTaskType.SEARCH)
                .setSearchType(JiraSearchType.USER_INFO)
                .setCallback(UserInfoActivity.this)
                .create()
                .execute();

    }

    @Override
    public void onJiraRequestPerformed(RestResponse< JiraUserInfo> restResponse) {
        if (restResponse.getOpeartionResult() == JiraOperationResult.PERFORMED) {
            JiraUserInfo user = restResponse.getResultObject();
            name.setText(getResources().getString(R.string.user_name) + UtilConstants.SharedPreference.COLON + user.getName());
            emailAddress.setText(getResources().getString(R.string.user_email) + UtilConstants.SharedPreference.COLON + user.getEmailAddress());
            displayName.setText(user.getDisplayName());
            timeZone.setText(getResources().getString(R.string.time_zone) + UtilConstants.SharedPreference.COLON + user.getTimeZone());
            locale.setText(getResources().getString(R.string.locale) + UtilConstants.SharedPreference.COLON + user.getLocale());
            size.setText(getResources().getString(R.string.size) + UtilConstants.SharedPreference.COLON + String.valueOf(user.getGroups().getSize()));
            String groups = "";
            for (int i = 0; i < user.getGroups().getItems().size(); i++) {
                groups += user.getGroups().getItems().get(i).getName() + UtilConstants.Dialog.NEW_LINE;
            }
            namesGroups.setText(getResources().getString(R.string.names_groups) + UtilConstants.SharedPreference.COLON + groups);
            showProgress(false);
        }
    }

}
