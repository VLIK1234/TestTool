package amtt.epam.com.amtt.ui.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.ExpectedResultsAdapter;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.googleapi.api.loadcontent.GSpreadsheetContent;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.googleapi.bo.GTag;
import amtt.epam.com.amtt.googleapi.database.contentprovider.GSUri;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.views.MultyAutocompleteProgressView;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 03.06.2015
 */

public class ExpectedResultsActivity extends BaseActivity implements ExpectedResultsAdapter.ViewHolder.ClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    //region Variables
    private static final int TESTCASES_LOADER_ID = 1;
    private static final int TAGS_LOADER_ID = 2;
    private static final String TAG = ExpectedResultsActivity.class.getSimpleName();
    private ExpectedResultsAdapter mResultsAdapter;
    private RecyclerView mRecyclerView;
    private MultyAutocompleteProgressView mTagsAutocompleteTextView;
    private ArrayAdapter mTagsAdapter;
    private Boolean mIsShowDetail = false;
    //endregion

    @Override
    public void onShowCard(int position) {
        Intent detail = new Intent(ExpectedResultsActivity.this, DetailActivity.class);
        GSpreadsheetContent.getInstance().setLastTestcaseId(mResultsAdapter.getIdTestcaseList().get(position));
        mIsShowDetail = true;
        startActivity(detail);
        finish();
    }

    @Override
    public void onShowCreationTicket(int position) {
        Intent creationTicket = new Intent(ExpectedResultsActivity.this, CreateIssueActivity.class);
        GSpreadsheetContent.getInstance().setLastTestcaseId(mResultsAdapter.getIdTestcaseList().get(position));
        startActivity(creationTicket);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expected_results);
        TopButtonService.sendActionChangeTopButtonVisibility(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mIsShowDetail) {
            TopButtonService.sendActionChangeTopButtonVisibility(true);
        }
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExpectedResultsActivity.this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        getLoaderManager().initLoader(TESTCASES_LOADER_ID, null, this);
        initTagsAutocompleteTextView();
    }

    private void initTagsAutocompleteTextView() {
        mTagsAutocompleteTextView = (MultyAutocompleteProgressView) findViewById(R.id.tv_tags);
        getLoaderManager().initLoader(TAGS_LOADER_ID, null, this);
        mTagsAutocompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void refreshTagsAdapter(List<GTag> result) {
        showProgress(true);
        mTagsAutocompleteTextView.showProgress(true);
        if (result != null && !result.isEmpty()) {
            ArrayList<String> tagsNames = new ArrayList<>();
            for (int i = 1; i < result.size(); i++) {
                if (result.get(i) != null) {
                    tagsNames.add(result.get(i).getName());
                }
            }
            mTagsAdapter = new ArrayAdapter<>(ExpectedResultsActivity.this, R.layout.spinner_dropdown_item, tagsNames);
            mTagsAutocompleteTextView.setThreshold(1);
            mTagsAutocompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            mTagsAutocompleteTextView.setAdapter(mTagsAdapter);
            mTagsAutocompleteTextView.showProgress(false);
        } else {
            Logger.e(TAG, "Tags not found");
            mTagsAutocompleteTextView.showProgress(false);
        }
    }


    private void refreshSteps(final List<GEntryWorksheet> result) {
        showProgress(true);
        if (result != null && !result.isEmpty()) {
            mResultsAdapter = new ExpectedResultsAdapter(result, R.layout.adapter_expected_results, ExpectedResultsActivity.this);
            mRecyclerView.setAdapter(mResultsAdapter);
            showProgress(false);
        } else {
            Logger.e(TAG, "List TestCases = null");
            showProgress(false);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if (id == TESTCASES_LOADER_ID) {
            loader = new CursorLoader(ExpectedResultsActivity.this, GSUri.TESTCASE.get(), null, null, null, null);
        } else if (id == TAGS_LOADER_ID) {
            loader = new CursorLoader(ExpectedResultsActivity.this, GSUri.TAGS.get(), null, null, null, null);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try {
            switch (loader.getId()) {
                case TESTCASES_LOADER_ID:
                    if (data != null && data.getCount() > 0) {
                        final List<GEntryWorksheet> listObject = new ArrayList<>();
                        if (data.moveToFirst()) {
                            do {
                                try {
                                    listObject.add(GEntryWorksheet.parse(data));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } while (data.moveToNext());
                        }
                        refreshSteps(listObject);
                    } else {
                        GSpreadsheetContent.getInstance().getAllTestCases(new GetContentCallback<List<GEntryWorksheet>>() {
                            @Override
                            public void resultOfDataLoading(List<GEntryWorksheet> result) {
                                if (result != null && !result.isEmpty()) {
                                    refreshSteps(result);
                                } else {
                                    Logger.e(TAG, "Error loading testcases");
                                }
                            }
                        });
                    }
                    break;
                case TAGS_LOADER_ID:
                    if (data != null && data.getCount() > 0) {
                        final List<GTag> listObject = new ArrayList<>();
                        if (data.moveToFirst()) {
                            do {
                                try {
                                    listObject.add(GTag.parse(data));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } while (data.moveToNext());
                        }
                        refreshTagsAdapter(listObject);
                    } else {
                        Logger.e(TAG, "Error loading tags");
                    }
                    break;
            }
        } finally {
            IOUtils.close(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Logger.d(TAG, "onLoaderReset");
    }
}
