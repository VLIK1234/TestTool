package amtt.epam.com.amtt.ui.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.SpreadsheetAdapter;
import amtt.epam.com.amtt.googleapi.database.contentprovider.GSUri;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;

/**
 * @author Iryna Monchanka
 * @version on 06.08.2015
 */

public class SpreadsheetActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String SPREADSHEET_ID_LINK = "SPREADSHEET_ID_LINK";
    private ListView mListView;
    private SpreadsheetAdapter mSpreadsheetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spreadsheet);
        TopButtonService.sendActionChangeTopButtonVisibility(false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(SPREADSHEET_ID_LINK, id);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, SpreadsheetActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TopButtonService.sendActionChangeTopButtonVisibility(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TopButtonService.sendActionChangeTopButtonVisibility(false);
    }

    //Callback
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        showProgress(true);
        return new CursorLoader(this, GSUri.SPREADSHEET.get(), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        showProgress(false);
        if (mSpreadsheetAdapter == null) {
            mSpreadsheetAdapter = new SpreadsheetAdapter(SpreadsheetActivity.this, null, NO_FLAGS);
            mListView.setAdapter(mSpreadsheetAdapter);
        }
        mSpreadsheetAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSpreadsheetAdapter.changeCursor(null);
    }

}