package amtt.epam.com.amtt.ui.activities;

import android.os.Bundle;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.LogAdapter;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.ReadLargeTextUtil;

/**
 * Created by Ivan_Bakach on 10.07.2015.
 */
public class LogActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    public static final String FILE_PATH = "filePath";
    public static final int SEARCH_TOP_OFFSET = 20;
    public String filePath;
    public ArrayList<CharSequence> listLogLine = new ArrayList<>();
    public RecyclerView recyclerView;
    public LogAdapter logAdapter;
    public SearchView searchView;
    public Button forwardButton;
    public Button backwardButton;
    public ArrayList<Integer> allIndexes;
    private ArrayList<CharSequence> originLogList = new ArrayList<>();
    private int currentIndex = 0;
    public boolean isDoneChangeText = false;
    public LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        recyclerView = (RecyclerView) findViewById(R.id.log_lines);
        linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setScrollbarFadingEnabled(false);

        String title = "";
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            filePath = extra.getString(FILE_PATH);
            title = FileUtil.getFileName(filePath);
            showLog(filePath);
        }
        setTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TopButtonService.sendActionChangeTopButtonVisibility(false);
    }

    public void showLog(String filePath) {
        if (FileUtil.isText(filePath)) {
            listLogLine = readTextLogFromFile(filePath);
            originLogList.addAll(listLogLine);
            logAdapter = new LogAdapter(listLogLine);
            recyclerView.setAdapter(logAdapter);
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
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.search_view_hint));
        LinearLayout linearLayoutOfSearchView = (LinearLayout) searchView.getChildAt(0);
        LayoutInflater factory = LayoutInflater.from(getBaseContext());
        final View view = factory.inflate(R.layout.search_panel, null);
        backwardButton = (Button) view.findViewById(R.id.bt_backward);
        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allIndexes.size() >= 1) {
                    if (0 < currentIndex) {
                        currentIndex--;
                    } else if (currentIndex == 0) {
                        currentIndex = allIndexes.size() - 1;
                    }
                    linearLayoutManager.scrollToPositionWithOffset(allIndexes.get(currentIndex), SEARCH_TOP_OFFSET);
                }
            }
        });
        forwardButton = (Button) view.findViewById(R.id.bt_forward);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allIndexes.size() >= 1) {
                    if (currentIndex + 1 < allIndexes.size()) {
                        currentIndex++;
                    } else if (currentIndex == allIndexes.size() - 1) {
                        currentIndex = 0;
                    }
                    linearLayoutManager.scrollToPositionWithOffset(allIndexes.get(currentIndex), SEARCH_TOP_OFFSET);
                }
            }
        });
        linearLayoutOfSearchView.addView(view);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        onSearch(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        currentIndex = 0;
        if (isDoneChangeText) {
            for (int i : allIndexes) {
                listLogLine.set(i, originLogList.get(i));
                logAdapter.notifyItemChanged(i);
            }
            allIndexes.clear();
        }
        isDoneChangeText = false;
        return true;
    }

    private void onSearch(String search){
        isDoneChangeText = true;
        allIndexes = new ArrayList<>();
        for (int i = 0; i < listLogLine.size(); i++) {
            if (listLogLine.get(i).toString().toUpperCase().contains(search.toUpperCase())) {
                allIndexes.add(i);
            }
        }
        if (allIndexes.size() >= 1) {
            for (Integer item : allIndexes) {
                String capsItem = listLogLine.get(item).toString().toUpperCase();
                String capsSearch = search.toUpperCase();
                ArrayList<Integer> localIndexes = new ArrayList<>();
                for (int index = capsItem.indexOf(capsSearch); index >= 0; index = capsItem.indexOf(capsSearch, index + 1)){
                    localIndexes.add(index);
                }
                for (int i: localIndexes) {
                    SpannableStringBuilder builder = new SpannableStringBuilder(listLogLine.get(item));
                    builder.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.highlighted_text_material_dark)), i, i + search.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    listLogLine.set(item, builder);
                }
                logAdapter.notifyItemChanged(item);
            }
            linearLayoutManager.scrollToPositionWithOffset(allIndexes.get(currentIndex), SEARCH_TOP_OFFSET);
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.label_null_search_result), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onClose() {
        for (int i : allIndexes) {
            listLogLine.set(i, originLogList.get(i));
            logAdapter.notifyItemChanged(i);
        }
        allIndexes.clear();
        return false;
    }
}
