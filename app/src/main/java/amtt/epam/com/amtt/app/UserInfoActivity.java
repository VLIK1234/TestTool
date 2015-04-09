package amtt.epam.com.amtt.app;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.asynctask.ShowUserInfoTask;
import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.callbacks.ShowUserInfoCallback;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.CredentialsManager;
import android.os.Bundle;
import android.widget.TextView;

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
        String credentials = CredentialsManager.getInstance().getCredentials(UserInfoActivity.this);
        String url = CredentialsManager.getInstance().getUrl(UserInfoActivity.this);
        showProgress(true);
        new ShowUserInfoTask(username, credentials, url, Constants.typeSearchInfo, UserInfoActivity.this).execute();


    }

    @Override
    public void onShowUserInfoResult(JiraUserInfo result) {
        name.setText(getResources().getString(R.string.user_name) + Constants.Keys.COLON + result.getName());
        emailAddress.setText(getResources().getString(R.string.user_email) + Constants.Keys.COLON + result.getEmailAddress());
        displayName.setText(result.getDisplayName());
        timeZone.setText(getResources().getString(R.string.time_zone) + Constants.Keys.COLON + result.getTimeZone());
        locale.setText(getResources().getString(R.string.locale) + Constants.Keys.COLON + result.getLocale());
        size.setText(getResources().getString(R.string.size) + Constants.Keys.COLON + String.valueOf(result.getGroups().getSize()));
        showProgress(false);
    }
}
