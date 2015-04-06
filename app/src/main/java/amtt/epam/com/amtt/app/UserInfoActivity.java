package amtt.epam.com.amtt.app;

import amtt.epam.com.amtt.asynctask.ShowUserInfoTask;
import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.callbacks.ShowUserInfoCallback;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import amtt.epam.com.amtt.R;
import android.widget.TextView;

import java.util.ArrayList;

public class UserInfoActivity extends  BaseActivity implements ShowUserInfoCallback {

    private final String TAG = this.getClass().getSimpleName();

    private static final String USER_NAME = "username";
    private static final String PASSWORD = "password";
    private static final String URL = "url";
    private static final String NAME_SP = "data";
    private static final String VOID = "";
    private static final String COLON = " : ";


    private TextView self, name, emailAddress, displayName, timeZone, locale, size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        SharedPreferences sharedPreferences = getSharedPreferences(NAME_SP, MODE_PRIVATE);

        self = (TextView) findViewById(R.id.tv_self);
        name = (TextView) findViewById(R.id.tv_name);
        emailAddress = (TextView) findViewById(R.id.tv_email_address);
        displayName = (TextView) findViewById(R.id.tv_display_name);
        timeZone = (TextView) findViewById(R.id.tv_time_zone);
        locale = (TextView) findViewById(R.id.tv_locale);
        size = (TextView) findViewById(R.id.tv_size);
        String username = sharedPreferences.getString(USER_NAME, VOID);
        String password = sharedPreferences.getString(PASSWORD, VOID);
        String url = sharedPreferences.getString(URL, VOID);
        new ShowUserInfoTask(username, password, url, UserInfoActivity.this).execute();


    }

    @Override
    public void onShowUserInfoResult(JiraUserInfo result) {
        self.setText(getResources().getString(R.string.self) +COLON+ result.getSelf());
        name.setText(getResources().getString(R.string.user_name) +COLON+ result.getName());
        emailAddress.setText(getResources().getString(R.string.user_email)+COLON +result.getEmailAddress());
        displayName.setText(result.getDisplayName());
        timeZone.setText(getResources().getString(R.string.time_zone)+COLON +result.getTimeZone());
        locale.setText(getResources().getString(R.string.locale)+COLON +result.getLocale());
        size.setText(String.valueOf(result.getGroups().getSize()));
    }
}
