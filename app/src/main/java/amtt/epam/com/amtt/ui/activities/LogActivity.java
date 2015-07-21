package amtt.epam.com.amtt.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.LogAdapter;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.ReadLargeTextUtil;

/**
 @author Ivan_Bakach
 @version on 10.07.2015
 */

public class LogActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    public static final String FILE_PATH = "filePath";
    public static final int SEARCH_TOP_OFFSET = 20;
    public String mFilePath;
    public ArrayList<CharSequence> mListLogLine = new ArrayList<>();
    public RecyclerView mRecyclerView;
    public LogAdapter mLogAdapter;
    public SearchView mSearchView;
    public Button mForwardButton;
    public Button mBackwardButton;
    public ArrayList<Integer> mAllIndexes = new ArrayList<>();
    private ArrayList<CharSequence> mOriginLogList = new ArrayList<>();
    private int mCurrentIndex = 0;
    public boolean mIsDoneChangeText = false;
    public LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.log_lines);
        mLinearLayoutManager = new LinearLayoutManager(getBaseContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setScrollbarFadingEnabled(false);

        String title = Constants.Symbols.EMPTY;
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            mFilePath = extra.getString(FILE_PATH);
            title = FileUtil.getFileName(mFilePath);
            showLog(mFilePath);
        }
        setTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TopButtonService.sendActionChangeTopButtonVisibility(false);
            }
        }, 500);
    }

    public void showLog(String filePath) {
        if (FileUtil.isText(filePath)) {
            mListLogLine = readTextLogFromFile(filePath);
            mOriginLogList.addAll(mListLogLine);
            mLogAdapter = new LogAdapter(mListLogLine);
            mRecyclerView.setAdapter(mLogAdapter);
        }
    }

    private ArrayList<CharSequence> readTextLogFromFile(String filePath) {
        File file = new File(filePath);
        ReadLargeTextUtil fileReader = new ReadLargeTextUtil(file);
        try {
            fileReader.start();
            fileReader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (!fileReader.isAlive()) {
                fileReader.interrupt();
            }
        }
        return fileReader.getLines();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint(getString(R.string.search_view_hint));
        LinearLayout linearLayoutOfSearchView = (LinearLayout) mSearchView.getChildAt(0);
        LayoutInflater factory = LayoutInflater.from(getBaseContext());
        final View view = factory.inflate(R.layout.search_panel, null);
        mBackwardButton = (Button) view.findViewById(R.id.bt_backward);
        mBackwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAllIndexes.size() >= 1) {
                    if (0 < mCurrentIndex) {
                        mCurrentIndex--;
                    } else if (mCurrentIndex == 0) {
                        mCurrentIndex = mAllIndexes.size() - 1;
                    }
                    mLinearLayoutManager.scrollToPositionWithOffset(mAllIndexes.get(mCurrentIndex), SEARCH_TOP_OFFSET);
                }
            }
        });
        mForwardButton = (Button) view.findViewById(R.id.bt_forward);
        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAllIndexes.size() >= 1) {
                    if (mCurrentIndex + 1 < mAllIndexes.size()) {
                        mCurrentIndex++;
                    } else if (mCurrentIndex == mAllIndexes.size() - 1) {
                        mCurrentIndex = 0;
                    }
                    mLinearLayoutManager.scrollToPositionWithOffset(mAllIndexes.get(mCurrentIndex), SEARCH_TOP_OFFSET);
                }
            }
        });
        linearLayoutOfSearchView.addView(view);
        mSearchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        onSearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mCurrentIndex = 0;
        if (mIsDoneChangeText) {
            for (int i : mAllIndexes) {
                mListLogLine.set(i, mOriginLogList.get(i));
                mLogAdapter.notifyItemChanged(i);
            }
            mAllIndexes.clear();
        }
        mIsDoneChangeText = false;
        return true;
    }

    private void onSearch(String search){
        mIsDoneChangeText = true;
        mAllIndexes.clear();
        for (int i = 0; i < mListLogLine.size(); i++) {
            if (mListLogLine.get(i).toString().toUpperCase().contains(search.toUpperCase())) {
                mAllIndexes.add(i);
            }
        }
        if (mAllIndexes.size() >= 1) {
            for (Integer item : mAllIndexes) {
                String capsItem = mListLogLine.get(item).toString().toUpperCase();
                String capsSearch = search.toUpperCase();
                ArrayList<Integer> localIndexes = new ArrayList<>();
                for (int index = capsItem.indexOf(capsSearch); index >= 0; index = capsItem.indexOf(capsSearch, index + 1)){
                    localIndexes.add(index);
                }
                for (int i: localIndexes) {
                    SpannableStringBuilder builder = new SpannableStringBuilder(mListLogLine.get(item));
                    builder.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.highlighted_text_material_dark)), i, i + search.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mListLogLine.set(item, builder);
                }
            }
            mLogAdapter.notifyDataSetChanged();
            mLinearLayoutManager.scrollToPositionWithOffset(mAllIndexes.get(mCurrentIndex), SEARCH_TOP_OFFSET);
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.label_null_search_result), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onClose() {
        for (int i : mAllIndexes) {
            mListLogLine.set(i, mOriginLogList.get(i));
            mLogAdapter.notifyItemChanged(i);
        }
        mAllIndexes.clear();
        return false;
    }
}
