package amtt.epam.com.amtt.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.ExpectedResultsAdapter;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.googleapi.api.loadcontent.GSpreadsheetContent;
import amtt.epam.com.amtt.googleapi.bo.GEntryWorksheet;
import amtt.epam.com.amtt.googleapi.bo.GTag;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.views.MultyAutocompleteProgressView;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.Logger;

/**
 * @author Iryna Monchanka
 * @version on 03.06.2015
 */

public class ExpectedResultsActivity extends BaseActivity implements ExpectedResultsAdapter.ViewHolder.ClickListener {

    //region Variables
    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final int SPREADSHEET_ACTIVITY_REQUEST_CODE = 55;
    private static final int NEW_SPREADSHEET_ACTIVITY_REQUEST_CODE = 66;
    private static final String TAG = ExpectedResultsActivity.class.getSimpleName();
    private static final String LINK = "Link";
    public static final String PRIORITY = "PRIORITY";
    public static final String NAME = "NAME";
    public static final String STEPS = "STEPS";
    public static final String EXPECTED_RESULT = "EXPECTED_RESULT";
    private ExpectedResultsAdapter mResultsAdapter;
    private RecyclerView mRecyclerView;
    private MultyAutocompleteProgressView mTagsAutocompleteTextView;
    private List<GTag> mTags;
    private TagsHandler mHandler;
    //endregion

    public static class TagsHandler extends Handler {

        private final WeakReference<ExpectedResultsActivity> mActivity;

        TagsHandler(ExpectedResultsActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ExpectedResultsActivity service = mActivity.get();
            service.setTags(msg.obj.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expected_results);
        mHandler = new TagsHandler(this);
        TopButtonService.sendActionChangeTopButtonVisibility(false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initViews();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_expected_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add: {
                startActivityForResult(new Intent(ExpectedResultsActivity.this, NewSpreadsheetActivity.class), NEW_SPREADSHEET_ACTIVITY_REQUEST_CODE);
            }
            return true;
            case R.id.action_list: {
                startActivityForResult(new Intent(ExpectedResultsActivity.this, SpreadsheetActivity.class), SPREADSHEET_ACTIVITY_REQUEST_CODE);
            }
            return true;
            case android.R.id.home: {
                TopButtonService.start(this);
                finish();
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SPREADSHEET_ACTIVITY_REQUEST_CODE:
                    if (data != null) {
                        getAllTestcases();
                    }
                    break;
                case NEW_SPREADSHEET_ACTIVITY_REQUEST_CODE: {
                    getAllTestcases();
                }
            }
        } else if (resultCode == RESULT_CANCELED) {

        }
    }

    @Override
    public void onShowCard(final int position) {
        getExtras(position, DetailActivity.class);
    }

    @Override
    public void onShowCreationTicket(int position) {
        getExtras(position, CreateIssueActivity.class);
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExpectedResultsActivity.this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        getAllTestcases();
        initTagsAutocompleteTextView();
    }

    private void initTagsAutocompleteTextView() {
        mTagsAutocompleteTextView = (MultyAutocompleteProgressView) findViewById(R.id.tv_tags);
        mTagsAutocompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard();
                String[] str = mTagsAutocompleteTextView.getText().toString().split(", ");
                ArrayList<String> links = new ArrayList<>();
                if (str.length == 1 && mTags != null) {
                    for (int i = 0; i < mTags.size(); i++) {
                        if (mTags.get(i).getName().equals((String) parent.getItemAtPosition(position))) {
                            links.add(mTags.get(i).getIdLinkTestCase());
                        }
                    }
                    getTagsByLinksTestcases(links);
                    getTestcasesByLinksTestcases(links);
                } else if (str.length > 1 && mTags != null) {
                    for (String aStr : str) {
                        for (int i = 0; i < mTags.size(); i++) {
                            if (aStr.equals(mTags.get(i).getName())) {
                                links.add(mTags.get(i).getIdLinkTestCase());
                            }
                        }
                    }
                    getTagsByLinksTestcases(links);
                    getTestcasesByLinksTestcases(links);
                }
            }
        });
        mTagsAutocompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before > count) {
                    mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, s), 750);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setTags(String text) {
        String[] str = text.split(", ");
        ArrayList<String> links = new ArrayList<>();
        if (mTags != null) {
            for (String aStr : str) {
                for (int i = 0; i < mTags.size(); i++) {
                    if (aStr.equals(mTags.get(i).getName())) {
                        links.add(mTags.get(i).getIdLinkTestCase());
                    }
                }
            }
            if (links.isEmpty()) {
                getAllTestcases();
            } else {
                getTagsByLinksTestcases(links);
            }
        } else {
            getAllTestcases();
        }
    }

    private void getExtras(int position, final Class<?> activity) {
        GSpreadsheetContent.getInstance().getTestcaseByIdLink(mResultsAdapter.getIdTestcaseList().get(position), new GetContentCallback<GEntryWorksheet>() {
            @Override
            public void resultOfDataLoading(final GEntryWorksheet result) {
                ExpectedResultsActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (result != null) {
                            Intent intent = new Intent(ExpectedResultsActivity.this, activity);
                            intent.putExtra(NAME, result.getTestCaseNameGSX());
                            intent.putExtra(PRIORITY, result.getPriorityGSX());
                            intent.putExtra(STEPS, result.getTestStepsGSX());
                            intent.putExtra(EXPECTED_RESULT, result.getExpectedResultGSX());
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    private void getTagsByLinksTestcases(final ArrayList<String> links) {
        if (links != null && ActiveUser.getInstance().getSpreadsheetLink() != null) {
            mTagsAutocompleteTextView.showProgress(true);
            showProgress(true);
            GSpreadsheetContent.getInstance().getTagsByIdLinksTestcases(ActiveUser.getInstance().getSpreadsheetLink(), links, new GetContentCallback<List<GTag>>() {
                @Override
                public void resultOfDataLoading(List<GTag> result) {
                    if (result != null && !result.isEmpty()) {
                        refreshTagsAdapter(result);
                    } else {
                        Logger.d(TAG, "Tags not found");
                        mTagsAutocompleteTextView.showProgress(false);
                        showProgress(false);
                    }
                }
            });
        }
    }

    private void getTestcasesByLinksTestcases(ArrayList<String> links) {
        if (links != null && ActiveUser.getInstance().getSpreadsheetLink() != null) {
            showProgress(true);
            GSpreadsheetContent.getInstance().getTestcasesByIdLinksTestcases(ActiveUser.getInstance().getSpreadsheetLink(), links, new GetContentCallback<List<GEntryWorksheet>>() {
                @Override
                public void resultOfDataLoading(List<GEntryWorksheet> result) {
                    if (result != null && !result.isEmpty()) {
                        refreshSteps(result);
                    } else {
                        showProgress(false);
                    }
                }
            });
        }
    }

    private void getAllTags() {
        if (ActiveUser.getInstance().getSpreadsheetLink() != null) {
            mTagsAutocompleteTextView.showProgress(true);
            showProgress(true);
            GSpreadsheetContent.getInstance().getAllTags(ActiveUser.getInstance().getSpreadsheetLink(), new GetContentCallback<List<GTag>>() {
                @Override
                public void resultOfDataLoading(List<GTag> result) {
                    if (result != null && !result.isEmpty()) {
                        mTags = result;
                        refreshTagsAdapter(result);
                    } else {
                        mTagsAutocompleteTextView.showProgress(false);
                        Logger.d(TAG, "Error loading tags");
                    }
                }
            });
        } else {
            Logger.d(TAG, "Error loading tags, SpreadsheetLink == null");
        }
    }

    private void getAllTestcases() {
        if (ActiveUser.getInstance().getSpreadsheetLink() != null && !ActiveUser.getInstance().getSpreadsheetLink().equals("")) {
            showProgress(true);
            GSpreadsheetContent.getInstance().getAllTestCases(ActiveUser.getInstance().getSpreadsheetLink(), new GetContentCallback<List<GEntryWorksheet>>() {
                @Override
                public void resultOfDataLoading(List<GEntryWorksheet> result) {
                    if (result != null && !result.isEmpty()) {
                        refreshSteps(result);
                        getAllTags();
                    } else {
                        showProgress(false);
                        Logger.d(TAG, "Error loading testcases");
                    }
                }
            });
        } else {
            Logger.d(TAG, "Error loading testcases, SpreadsheetLink == null");
        }
    }

    private void refreshTagsAdapter(List<GTag> result) {
        hideKeyboard();
        if (result != null && !result.isEmpty()) {
            HashSet<String> namesTags = new HashSet<>();
            for (int i = 1; i < result.size(); i++) {
                if (result.get(i) != null) {
                    namesTags.add(result.get(i).getName());
                }
            }
            List<String> tagsNames = new ArrayList<String>(namesTags);
            ArrayAdapter<String> mTagsAdapter = new ArrayAdapter<>(ExpectedResultsActivity.this, R.layout.spinner_dropdown_item, tagsNames);
            mTagsAutocompleteTextView.setThreshold(1);
            mTagsAutocompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            mTagsAutocompleteTextView.setAdapter(mTagsAdapter);
            mTagsAutocompleteTextView.showProgress(false);
            showProgress(false);
        } else {
            Logger.d(TAG, "Tags not found");
            mTagsAutocompleteTextView.showProgress(false);
            showProgress(false);
        }
    }

    private void refreshSteps(final List<GEntryWorksheet> result) {
        if (result != null && !result.isEmpty()) {
            mResultsAdapter = null;
            mResultsAdapter = new ExpectedResultsAdapter(result, R.layout.adapter_expected_results, ExpectedResultsActivity.this);
            if (mRecyclerView != null) {
                mRecyclerView.setAdapter(mResultsAdapter);
            }
            showProgress(false);
        } else {
            Logger.d(TAG, "List TestCases = null");
            showProgress(false);
        }
    }
}
