package amtt.epam.com.amtt.fragment;

import android.app.Fragment;
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
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.table.UsersTable;

/**
 * Created by Artsiom_Kaliaha on 30.04.2015.
 */
public class QAsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView mListView;
    private UserAdapter mAdapter;
    private ProgressBar mProgressBar;

    private static final int CURSOR_LOADER_ID = 0;
    private static final int NO_FLAGS = 0;

    public QAsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_qas, container, false);

        mListView = (ListView) layout.findViewById(android.R.id.list);
        mAdapter = new UserAdapter(getActivity(), null, NO_FLAGS);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        mProgressBar = (ProgressBar) layout.findViewById(R.id.progress);

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        return layout;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mProgressBar.setVisibility(View.VISIBLE);
        return new CursorLoader(getActivity(), AmttContentProvider.USER_CONTENT_URI, UsersTable.PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mProgressBar.setVisibility(View.GONE);
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
