package amtt.epam.com.amtt.app;

import android.os.Bundle;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.asynctask.ShowUserInfoTask;
import amtt.epam.com.amtt.bo.issue.TypeSearchedData;
import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.callbacks.ShowUserInfoCallback;
import amtt.epam.com.amtt.util.Constants;

public class UserInfoActivity extends BaseActivity implements ShowUserInfoCallback {

    private final String TAG = this.getClass().getSimpleName();
    private TextView name, emailAddress, displayName, timeZone, locale, size, namesGroups;

    @Override
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
        new ShowUserInfoTask(TypeSearchedData.SEARCH_USER_INFO, UserInfoActivity.this).execute();


    }

    @Override
    public void onShowUserInfoResult(JiraUserInfo result) {
        name.setText(getResources().getString(R.string.user_name) + Constants.SharedPreferenceKeys.COLON + result.getName());
        emailAddress.setText(getResources().getString(R.string.user_email) + Constants.SharedPreferenceKeys.COLON + result.getEmailAddress());
        displayName.setText(result.getDisplayName());
        timeZone.setText(getResources().getString(R.string.time_zone) + Constants.SharedPreferenceKeys.COLON + result.getTimeZone());
        locale.setText(getResources().getString(R.string.locale) + Constants.SharedPreferenceKeys.COLON + result.getLocale());
        size.setText(getResources().getString(R.string.size) + Constants.SharedPreferenceKeys.COLON + String.valueOf(result.getGroups().getSize()));
        String groups = "";
        for (int i = 0; i < result.getGroups().getItems().size(); i++) {
            groups += result.getGroups().getItems().get(i).getName() + Constants.DialogKeys.NEW_LINE;
        }
        namesGroups.setText(getResources().getString(R.string.names_groups) + Constants.SharedPreferenceKeys.COLON + groups);
        showProgress(false);
    }
}
