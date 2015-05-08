package amtt.epam.com.amtt.fragment;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.processing.UserInfoProcessor;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.view.TextView;

/**
 * Created by Artsiom_Kaliaha on 05.05.2015.
 */
@SuppressWarnings("unchecked")
public class UserFragment extends BaseFragment implements JiraCallback<JiraUserInfo>, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String KEY_USER = "key_user";

    private TextView mUserName;
    private TextView mEmail;
    private TextView mDisplayName;
    private TextView mTimeZone;
    private TextView mLocale;
    private TextView mGroupSize;
    private TextView mGroupsNames;

    public UserFragment() {
    }

    public static UserFragment getInstance(long userId) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_USER, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.activity_user_info, container, false);
        initViews(layout);

        Bundle args = getArguments();
        requestUserCredentialsFromDatabase(args);
        return layout;
    }

    private void initViews(View layout) {
        mUserName = (TextView) layout.findViewById(R.id.user_name);
        mEmail = (TextView) layout.findViewById(R.id.user_email);
        mDisplayName = (TextView) layout.findViewById(R.id.user_display_name);
        mTimeZone = (TextView) layout.findViewById(R.id.user_time_zone);
        mLocale = (TextView) layout.findViewById(R.id.user_locale);

        mProgressBar = (ProgressBar) layout.findViewById(android.R.id.progress);
    }

    private void requestUserInfo() {
//        String requestSuffix = JiraApiConst.USER_INFO_PATH + ActiveUser.getInstance().getUserName() + JiraApiConst.EXPAND_GROUPS;
//        RestMethod<JiraUserInfo> userInfoMethod = JiraApi.getInstance().buildDataSearch(requestSuffix, new UserInfoProcessor());
//        new JiraTask.Builder<JiraUserInfo>()
//                .setRestMethod(userInfoMethod)
//                .setCallback(UserFragment.this)
//                .createAndExecute();
    }

    private void requestUserCredentialsFromDatabase(Bundle argsWithUserId) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, argsWithUserId, UserFragment.this);
    }

    //Callbacks
    //Jira
    @Override
    public void onRequestStarted() {
        setProgressVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPerformed(RestResponse<JiraUserInfo> restResponse) {

        if (restResponse.getOpeartionResult() == JiraOperationResult.REQUEST_PERFORMED) {
            JiraUserInfo user = restResponse.getResultObject();
            mUserName.setText(getResources().getString(R.string.label_user_name) + Constants.Str.COLON + user.getName());
            mEmail.setText(getResources().getString(R.string.label_email) + Constants.Str.COLON + user.getEmailAddress());
            mDisplayName.setText(user.getDisplayName());
            mTimeZone.setText(getResources().getString(R.string.label_time_zone) + Constants.Str.COLON + user.getTimeZone());
            mLocale.setText(getResources().getString(R.string.label_locale) + Constants.Str.COLON + user.getLocale());
            mGroupSize.setText(getResources().getString(R.string.label_size) + Constants.Str.COLON + String.valueOf(user.getGroups().getSize()));
            String groups = "";
            for (int i = 0; i < user.getGroups().getItems().size(); i++) {
                groups += user.getGroups().getItems().get(i).getName() + Constants.Str.NEW_LINE;
            }
            mGroupsNames.setText(getResources().getString(R.string.label_names_groups) + Constants.Str.COLON + groups);
            setProgressVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestError(AmttException e) {
        ExceptionHandler.getInstance().processError(e).showDialog(getActivity(), UserFragment.this);
        setProgressVisibility(View.GONE);
    }

    //Loader


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        long userId = args.getLong(KEY_USER);
        return new CursorLoader(getActivity(),
                AmttContentProvider.USER_CONTENT_URI,
                UsersTable.PROJECTION,
                UsersTable._ID + "=?",
                new String[]{String.valueOf(userId)},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        String userName = data.getString(data.getColumnIndex(UsersTable._USER_NAME));
        //String password = data.getString(data.getColumnIndex(UsersTable._PASSWORD));
        //ActiveUser.getInstance().setCredentials(userName, password);
        requestUserInfo();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
