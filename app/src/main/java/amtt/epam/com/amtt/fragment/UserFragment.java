package amtt.epam.com.amtt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.UserDataResult;
import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.util.Constants;

/**
 * Created by Artsiom_Kaliaha on 05.05.2015.
 */
public class UserFragment extends BaseFragment implements JiraCallback<UserDataResult,JiraUserInfo> {

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
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_user_info, container, false);
        initViews(layout);
        requestUserInfo();
        return layout;
    }

    @SuppressWarnings("unchecked")
    private void requestUserInfo() {
        new JiraTask.Builder<UserDataResult,JiraUserInfo>()
                .setOperationType(JiraTask.JiraTaskType.SEARCH)
                .setSearchType(JiraTask.JiraSearchType.USER_INFO)
                .setCallback(UserFragment.this)
                .create()
                .execute();
    }

    private void initViews(View layout) {
        mUserName = (TextView)layout.findViewById(R.id.user_name);
        mEmail = (TextView)layout.findViewById(R.id.user_email);
        mDisplayName = (TextView)layout.findViewById(R.id.user_display_name);
        mTimeZone = (TextView)layout.findViewById(R.id.user_time_zone);
        mLocale = (TextView)layout.findViewById(R.id.user_locale);
        mGroupSize = (TextView)layout.findViewById(R.id.user_group_size);
        mGroupsNames = (TextView)layout.findViewById(R.id.user_groups_names);

        mProgressBar = (ProgressBar)layout.findViewById(android.R.id.progress);
    }



    @Override
    public void onJiraRequestPerformed(RestResponse<UserDataResult, JiraUserInfo> restResponse) {
        JiraUserInfo user = restResponse.getResultObject();
        mUserName.setText(getResources().getString(R.string.user_name) + Constants.SharedPreferenceKeys.COLON + user.getName());
        mEmail.setText(getResources().getString(R.string.user_email) + Constants.SharedPreferenceKeys.COLON + user.getEmailAddress());
        mDisplayName.setText(user.getDisplayName());
        mTimeZone.setText(getResources().getString(R.string.time_zone) + Constants.SharedPreferenceKeys.COLON + user.getTimeZone());
        mLocale.setText(getResources().getString(R.string.locale) + Constants.SharedPreferenceKeys.COLON + user.getLocale());
        mGroupSize.setText(getResources().getString(R.string.size) + Constants.SharedPreferenceKeys.COLON + String.valueOf(user.getGroups().getSize()));
        String groups = "";
        for (int i = 0; i < user.getGroups().getItems().size(); i++) {
            groups += user.getGroups().getItems().get(i).getName() + Constants.DialogKeys.NEW_LINE;
        }
        mGroupsNames.setText(getResources().getString(R.string.names_groups) + Constants.SharedPreferenceKeys.COLON + groups);
        setProgressVisibility(View.GONE);
    }

}
