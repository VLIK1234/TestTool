package amtt.epam.com.amtt.ui.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.ExpectedResultsAdapter;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.googleapi.api.GoogleApiConst;
import amtt.epam.com.amtt.googleapi.api.loadcontent.GSpreadsheetContent;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.googleapi.bo.GTag;
import amtt.epam.com.amtt.googleapi.database.contentprovider.GSUri;
import amtt.epam.com.amtt.googleapi.database.table.TagsTable;
import amtt.epam.com.amtt.googleapi.database.table.TestcaseTable;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.views.MultyAutocompleteProgressView;
import amtt.epam.com.amtt.util.ConverterUtil;
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
    private static final int TAGS_LOADER_BY_LINK_ID = 3;
    private static final int TESTCASES_LOADER_BY_LINK_ID = 4;
    private static final String TAG = ExpectedResultsActivity.class.getSimpleName();
    public static final String LINK = "Link";
    private ExpectedResultsAdapter mResultsAdapter;
    private RecyclerView mRecyclerView;
    private MultyAutocompleteProgressView mTagsAutocompleteTextView;
    private List<GTag> mTags;
    private Bundle bundle;
    //endregion

    @Override
    public void onShowCard(int position) {
        Intent detail = new Intent(ExpectedResultsActivity.this, DetailActivity.class);
        detail.putExtra(GoogleApiConst.LINK_TAG, mResultsAdapter.getIdTestcaseList().get(position));
        startActivity(detail);
        finish();
    }

    @Override
    public void onShowCreationTicket(int position) {
        Intent creationTicket = new Intent(ExpectedResultsActivity.this, CreateIssueActivity.class);
        creationTicket.putExtra(GoogleApiConst.LINK_TAG, mResultsAdapter.getIdTestcaseList().get(position));
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
            TopButtonService.sendActionChangeTopButtonVisibility(true);
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExpectedResultsActivity.this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        getLoaderManager().initLoader(TESTCASES_LOADER_ID, null, ExpectedResultsActivity.this);
        initTagsAutocompleteTextView();
    }

    private void initTagsAutocompleteTextView() {
        mTagsAutocompleteTextView = (MultyAutocompleteProgressView) findViewById(R.id.tv_tags);
        mTagsAutocompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTagsAutocompleteTextView.showProgress(true);
                showProgress(true);
                String[] str = mTagsAutocompleteTextView.getText().toString().split(", ");
                Logger.e(TAG, Arrays.toString(str));
                bundle = new Bundle();
                ArrayList<String> links = new ArrayList<>();
                if (str.length == 1) {
                    for (int i = 0; i < mTags.size(); i++) {
                        if (mTags.get(i).getName().equals((String) parent.getItemAtPosition(position))) {
                            links.add(mTags.get(i).getIdLinkTestCase());
                            Logger.e(TAG, mTags.get(i).getIdLinkTestCase());
                        }
                    }
                } else {
                    for (String aStr : str) {
                        for (int i = 0; i < mTags.size(); i++) {
                            if (mTags.get(i).getName() == aStr && mTags.get(i).getIdLinkTestCase() == mTags.get(0).getIdLinkTestCase()) {
                                links.add(mTags.get(i).getIdLinkTestCase());
                            }
                        }
                    }
                }
                bundle.putStringArrayList(LINK, links);
                getLoaderManager().initLoader(TAGS_LOADER_BY_LINK_ID, bundle, ExpectedResultsActivity.this);
            }
        });
    }

    private void refreshTagsAdapter(List<GTag> result) {
        showProgress(true);
        Logger.e(TAG, "refreshTagsAdapter");
        mTagsAutocompleteTextView.showProgress(true);
        if (result != null && !result.isEmpty()) {
            ArrayList<String> tagsNames = new ArrayList<>();
            for (int i = 1; i < result.size(); i++) {
                if (result.get(i) != null) {
                    tagsNames.add(result.get(i).getName());
                }
            }
            ArrayAdapter<String> mTagsAdapter = new ArrayAdapter<>(ExpectedResultsActivity.this, R.layout.spinner_dropdown_item, tagsNames);
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
        Logger.e(TAG, "refreshSteps");
        if (result != null && !result.isEmpty()) {
            mResultsAdapter = null;
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
        }else if(id == TAGS_LOADER_BY_LINK_ID){
            if (args.getStringArrayList(LINK) != null) {
                String selection = getSelection(TagsTable._TESTCASE_ID_LINK, args.getStringArrayList(LINK));
                loader = new CursorLoader(ExpectedResultsActivity.this, GSUri.TAGS.get(), null, selection,
                        ConverterUtil.arrayListToArray(args.getStringArrayList(LINK)), null);
            }
        } else if (id == TESTCASES_LOADER_BY_LINK_ID) {
            if (args.getStringArrayList(LINK) != null) {
                String selection = getSelection(TestcaseTable._TESTCASE_ID_LINK, args.getStringArrayList(LINK));
                loader = new CursorLoader(ExpectedResultsActivity.this, GSUri.TESTCASE.get(), null, selection,
                        ConverterUtil.arrayListToArray(args.getStringArrayList(LINK)), null);
            }
        }
        return loader;
    }

    @NonNull
    private String getSelection(String columnName, ArrayList<String> args) {
        String selection =  columnName + "=?";
        if (args.size() > 1) {
            for (int i = 0; i < args.size(); i++) {
                selection = selection.concat(" OR ?");
            }
        }
        return selection;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        try {
            switch (loader.getId()) {
                case TESTCASES_LOADER_ID:
                    if (data != null && data.getCount() > 0) {
                        refreshSteps(getTestcasesFromCursor(data));
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
                    getLoaderManager().initLoader(TAGS_LOADER_ID, null, ExpectedResultsActivity.this);
                    break;
                case TAGS_LOADER_ID:
                    if (data != null && data.getCount() > 0) {
                        mTags = getTagsFromCursor(data);
                        refreshTagsAdapter(getTagsFromCursor(data));
                    } else {
                        Logger.e(TAG, "Error loading tags");
                        mTagsAutocompleteTextView.showProgress(false);
                    }
                    break;
                case TAGS_LOADER_BY_LINK_ID:
                    if (data != null && data.getCount() > 0) {
                        refreshTagsAdapter(getTagsFromCursor(data));
                    } else {
                        Logger.e(TAG, "Error loading tags");
                        mTagsAutocompleteTextView.showProgress(false);
                    }
                    getLoaderManager().initLoader(TESTCASES_LOADER_BY_LINK_ID, bundle, ExpectedResultsActivity.this);
                    break;
                case TESTCASES_LOADER_BY_LINK_ID:
                    refreshSteps(getTestcasesFromCursor(data));
                    break;
            }
        } finally {
            IOUtils.close(data);
        }
    }

    @NonNull
    private List<GTag> getTagsFromCursor(Cursor data) {
        final List<GTag> listObject = new ArrayList<>();
        if (data.moveToFirst()) {
            do {
                try {
                    listObject.add(new GTag().parse(data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (data.moveToNext());
        }
        return listObject;
    }

    private List<GEntryWorksheet> getTestcasesFromCursor(Cursor data) {
        List<GEntryWorksheet> listObject = null;
        if (data != null && data.getCount() > 0) {
            listObject = new ArrayList<>();
            if (data.moveToFirst()) {
                do {
                    try {
                        listObject.add(new GEntryWorksheet().parse(data));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (data.moveToNext());
            }
        }
        return listObject;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Logger.d(TAG, "onLoaderReset");
    }
}
