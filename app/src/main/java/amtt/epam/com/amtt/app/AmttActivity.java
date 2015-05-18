package amtt.epam.com.amtt.app;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import amtt.epam.com.amtt.adapter.UserAdapter;
import amtt.epam.com.amtt.contentprovider.AmttUri;

/**
 * Created by Artsiom_Kaliaha on 18.05.2015.
 */
public class AmttActivity extends BaseActivity implements LoaderCallbacks<Cursor> {

    public static final String KEY_USER_ID = "key_user_id";

    private ListView mListView;
    private UserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListView = (ListView)findViewById(android.R.id.list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(KEY_USER_ID, id);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null,this);
    }

    //Callback
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        showProgress(true);
        return new CursorLoader(this, AmttUri.USER.get(), null, null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        showProgress(false);
        if (mAdapter == null) {
            mAdapter = new UserAdapter(this, null, NO_FLAGS);
            mListView.setAdapter(mAdapter);
        }
        mAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

}
