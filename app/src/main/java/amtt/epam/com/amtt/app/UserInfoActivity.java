package amtt.epam.com.amtt.app;

import amtt.epam.com.amtt.CoreApplication;
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
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.Logger;
import amtt.epam.com.amtt.view.TextView;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

/**
 * Created by Artsiom_Kaliaha on 07.05.2015.
 */
@SuppressWarnings("unchecked")
public class UserInfoActivity extends BaseActivity implements JiraCallback<JUserInfo>, LoaderCallbacks<Cursor> {

    private TextView mName;
    private TextView mEmailAddress;
    private TextView mDisplayName;
    private TextView mTimeZone;
    private TextView mLocale;
    private ImageView mUserImage;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_user_info);
        TopButtonService.close(this);
        initViews();
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TopButtonService.start(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_user:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_refresh_user_info:
                showProgress(true);
                String requestSuffix = JiraApiConst.USER_INFO_PATH + ActiveUser.getInstance().getUserName() + JiraApiConst.EXPAND_GROUPS;
                RestMethod<JUserInfo> userInfoMethod = JiraApi.getInstance().buildDataSearch(requestSuffix,
                        new UserInfoProcessor(),
                        null,
                        null,
                        null);
                new JiraTask.Builder<JUserInfo>()
                        .setRestMethod(userInfoMethod)
                        .setCallback(UserInfoActivity.this)
                        .createAndExecute();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initViews() {
        mName = (TextView) findViewById(R.id.user_name);
        mEmailAddress = (TextView) findViewById(R.id.user_email);
        mDisplayName = (TextView) findViewById(R.id.user_display_name);
        mTimeZone = (TextView) findViewById(R.id.user_time_zone);
        mLocale = (TextView) findViewById(R.id.user_locale);
        mUserImage = (ImageView) findViewById(R.id.user_image);
    }

    public void populateUserInfo(JUserInfo user) {
        mName.setText(user.getName());
        mEmailAddress.setText(user.getEmailAddress());
        mDisplayName.setText(user.getDisplayName());
        mTimeZone.setText(user.getTimeZone());
        mLocale.setText(user.getLocale());
    }

    //Callback
    //Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        loader = new CursorLoader(this,
                AmttUri.USER.get(),
                null,
                UsersTable._ID + "=?",
                new String[]{ String.valueOf(ActiveUser.getInstance().getId()) },
                null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try {
            if (data != null) {
                if (data.getCount() > 0) {
                    JUserInfo userInfo = new JUserInfo(data);
                    populateUserInfo(userInfo);
                    CoreApplication.getImageLoader().displayImage(userInfo.getAvatarUrls().getAvatarUrl(), mUserImage);
                } else {
                    Logger.d(TAG, "data==0");
                }
            } else {
                Logger.d(TAG, "data==null");
            }
        } finally {
            IOUtils.close(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    //Jira
    @Override
    public void onRequestStarted() {
        showProgress(true);
    }

    @Override
    public void onRequestPerformed(RestResponse<JUserInfo> restResponse) {
        if (restResponse.getOpeartionResult() == JiraOperationResult.REQUEST_PERFORMED) {
            JUserInfo user = restResponse.getResultObject();
            populateUserInfo(user);
            showProgress(false);
        }
    }

    @Override
    public void onRequestError(AmttException e) {
        ExceptionHandler.getInstance().processError(e).showDialog(this, UserInfoActivity.this);
        showProgress(false);
    }

}
