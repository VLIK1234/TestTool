package amtt.epam.com.amtt.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.LogUtils;
import amtt.epam.com.amtt.util.ReadLargeTextUtil;

/**
 * Created by Ivan_Bakach on 09.06.2015.
 */
public class PreviewActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener{

    public static final String FILE_PATH = "filePath";
    private TextView mTextPreview;
    public ScrollView mRootView;
    public String filePath;
    public String logText;
    public SearchView searchView;
    public Button forwardButton;
    public Button backwardButton;
    public ArrayList<Integer> allIndexes;
    private int currentIndex = 0;
    public CharSequence spannedLog;
    public boolean isDoneChangeText = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        mRootView = (ScrollView) findViewById(R.id.scroll_view);
        mTextPreview = (TextView) findViewById(R.id.text_preview);

        String title = "";
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            filePath = extra.getString(FILE_PATH);
            title = FileUtil.getFileName(filePath);
            showPreview(filePath);
        }
        setTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TopButtonService.sendActionChangeTopButtonVisibility(false);
    }

    private CharSequence readLogFromFile(String filePath) {
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
        return LogUtils.getFormatLog(fileReader.getLines());
    }

//    private String readTextLogFromFile(String filePath) {
//        File file = new File(filePath);
//        ReadLargeTextUtil fileReader = new ReadLargeTextUtil(file);
//        try {
//            fileReader.start();
//            fileReader.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            if (!fileReader.isAlive()) {
//                fileReader.interrupt();
//            }
//        }
//        return LogUtils.getTextLog(fileReader.getLines());
//    }

    public void showPreview(String filePath) {
        if (FileUtil.isText(filePath)) {
            mTextPreview.setText(readLogFromFile(filePath), TextView.BufferType.NORMAL);
            spannedLog = mTextPreview.getText();
//            logText = readTextLogFromFile(filePath);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search");
        LinearLayout linearLayoutOfSearchView = (LinearLayout) searchView.getChildAt(0);
        LayoutInflater factory = LayoutInflater.from(getBaseContext());
        final View view = factory.inflate(R.layout.search_panel, null);
        backwardButton = (Button) view.findViewById(R.id.bt_backward);
        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allIndexes!=null) {
                    if (0 < currentIndex) {
                        currentIndex--;
                        goToIndexPostion(currentIndex);
                    }else if (currentIndex==0) {
                        currentIndex = allIndexes.size()-1;
                        goToIndexPostion(currentIndex);
                    }
                }
            }
        });
        forwardButton = (Button) view.findViewById(R.id.bt_forward);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allIndexes!=null) {
                    if (allIndexes.size() > currentIndex + 1) {
                        currentIndex++;
                        goToIndexPostion(currentIndex);
                    }else if (currentIndex == allIndexes.size()-1) {
                        currentIndex = 0;
                        goToIndexPostion(currentIndex);
                    }
                }
            }
        });
        linearLayoutOfSearchView.addView(view);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            onSearch(query);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        currentIndex = 0;
        if (isDoneChangeText) {
            mTextPreview.setText(spannedLog, TextView.BufferType.NORMAL);
        }
        isDoneChangeText = false;
        return true;
    }

    private void onSearch(String search) throws UnsupportedEncodingException {
        isDoneChangeText = true;
        String capsLogText = logText.toUpperCase();
        CharSequence tempLogWithSelection = spannedLog;
        int index = capsLogText.indexOf(search.toUpperCase());
        allIndexes = new ArrayList<>();
        SpannableStringBuilder builder = new SpannableStringBuilder(tempLogWithSelection);
        if (index==-1) {
            Toast.makeText(getBaseContext(), "Nothing Found",Toast.LENGTH_SHORT).show();
        }
        while (index >= 0) {
            allIndexes.add(index);
            builder.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.light_blue_selection)), index, index+search.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            index = capsLogText.indexOf(search.toUpperCase(), index + 1);
        }
        goToIndexPostion(currentIndex);
        mTextPreview.setText(builder);
    }
    private void goToIndexPostion(int position) {
        if (allIndexes.size()>0&&position<allIndexes.size()) {
            int offset = allIndexes.get(position);
            int line = mTextPreview.getLayout().getLineForOffset(offset);
            double ratioLineInScrollPoint = mRootView.getChildAt(0).getHeight() / mTextPreview.getLayout().getLineCount();
            mRootView.smoothScrollTo(0, line * (int) ratioLineInScrollPoint);
        }
    }

    @Override
    public boolean onClose() {
        mTextPreview.setText(spannedLog);
        return false;
    }
}
