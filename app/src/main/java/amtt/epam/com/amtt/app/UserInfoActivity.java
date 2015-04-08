package amtt.epam.com.amtt.app;

import android.os.Bundle;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.asynctask.ShowUserInfoTask;
import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.callbacks.ShowUserInfoCallback;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.CredentialsManager;

public class UserInfoActivity extends BaseActivity implements ShowUserInfoCallback {

    private final String TAG = this.getClass().getSimpleName();
    private TextView name, emailAddress, displayName, timeZone, locale, size;

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
        String username = CredentialsManager.getInstance().getUserName(UserInfoActivity.this);
        String password = CredentialsManager.getInstance().getPassword(UserInfoActivity.this);
        String url = CredentialsManager.getInstance().getUrl(UserInfoActivity.this);
        showProgress(true, R.id.progress);
        new ShowUserInfoTask(username, password, url, Constants.typeSearchInfo, UserInfoActivity.this).execute();


    }

    @Override
    public void onShowUserInfoResult(JiraUserInfo result) {
        name.setText(getResources().getString(R.string.user_name) + Constants.COLON + result.getName());
        emailAddress.setText(getResources().getString(R.string.user_email) + Constants.COLON + result.getEmailAddress());
        displayName.setText(result.getDisplayName());
        timeZone.setText(getResources().getString(R.string.time_zone) + Constants.COLON + result.getTimeZone());
        locale.setText(getResources().getString(R.string.locale) + Constants.COLON + result.getLocale());
        size.setText(getResources().getString(R.string.size) + Constants.COLON + String.valueOf(result.getGroups().getSize()));
        showProgress(false, R.id.progress);
    }
}
