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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.UserAdapter;
import amtt.epam.com.amtt.api.JiraCallback;
import amtt.epam.com.amtt.api.JiraTask;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.api.result.AuthorizationResult;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.util.CredentialsManager;

/**
 * Created by Artsiom_Kaliaha on 30.04.2015.
 */
public class UserListFragment extends BaseFragment implements JiraCallback, LoaderManager.LoaderCallbacks<Cursor> {

    public static interface ListFragmentUserCallback {
        void onListItemClick(long id);
    }

    private static final int CURSOR_LOADER_FOR_USER_ID = 1;
    private static final String KEY_SELECTED_USER = "selected_user_key";
    private UserAdapter mAdapter;

    public UserListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_qas, container, false);
        initViews(layout);

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        return layout;
    }

    private void initViews(View layout) {
        ListView listView = (ListView) layout.findViewById(android.R.id.list);
        mAdapter = new UserAdapter(getActivity(), null, NO_FLAGS);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle args = new Bundle();
                args.putLong(KEY_SELECTED_USER, id);
                getLoaderManager().initLoader(CURSOR_LOADER_FOR_USER_ID, args, UserListFragment.this);
                ((ListFragmentUserCallback)getActivity()).onListItemClick(id);
            }
        });
        mProgressBar = (ProgressBar) layout.findViewById(android.R.id.progress);
    }

    //Callbacks
    @Override
    public void onJiraRequestPerformed(RestResponse restResponse) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if (id == CURSOR_LOADER_FOR_USER_ID) {
            long selectedUserId = args.getLong(KEY_SELECTED_USER);
            loader = new CursorLoader(getActivity(),
                    AmttContentProvider.USER_CONTENT_URI,
                    UsersTable.PROJECTION,
                    UsersTable._ID,
                    new String[] { String.valueOf(selectedUserId) },
                    null);
        } else if (id == CURSOR_LOADER_ID) {
            loader = new CursorLoader(getActivity(), AmttContentProvider.USER_CONTENT_URI, UsersTable.PROJECTION, null, null, null);
        }
        setProgressVisibility(View.VISIBLE);
        return loader;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == CURSOR_LOADER_FOR_USER_ID) {
            String userName = data.getString(data.getColumnIndex(UsersTable._USER_NAME));
            String password = data.getString(data.getColumnIndex(UsersTable._PASSWORD));
            CredentialsManager.getInstance().setCredentials(userName,password);
            new JiraTask.Builder<AuthorizationResult, Void>()
                    .setOperationType(JiraTask.JiraTaskType.AUTH)
                    .setCallback(UserListFragment.this)
                    .create()
                    .execute();
        } else if (loader.getId() == CURSOR_LOADER_ID) {
            setProgressVisibility(View.GONE);
            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
