package amtt.epam.com.amtt.ui.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.LogUtils;
import amtt.epam.com.amtt.util.ReadLargeTextUtil;

/**
 * Created by Ivan_Bakach on 09.06.2015.
 */
public class PreviewActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final String FILE_PATH = "filePath";
    private TextView mTextPreview;
    public ScrollView mRootView;
    public int mOffset;
    public int mLine;
    public String filePath;
    public String logText;
    public SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        mRootView = (ScrollView) findViewById(R.id.scroll_view);
        mRootView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int scrollX = mRootView.getScrollX();
                int scrollY = mRootView.getScrollY();
                Log.d("TAG", scrollX + " : " + scrollY);
            }
        });
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

    private String readTextLogFromFile(String filePath) {
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
        return LogUtils.getTextLog(fileReader.getLines());
    }

    public void showPreview(String filePath) {
        if (FileUtil.isText(filePath)) {
            mTextPreview.setText(readLogFromFile(filePath));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_forward:
                Toast.makeText(getBaseContext(), "Forward", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_search_backward:
                Toast.makeText(getBaseContext(), "Backward", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        return true;
    }

    private void onSearch(String search) throws UnsupportedEncodingException {
        logText = readTextLogFromFile(filePath);
        mOffset = logText.indexOf(search);
        mLine = mTextPreview.getLayout().getLineForOffset(mOffset+1);
//        mLine = mTextPreview.getLayout().getLineTop(Integer.valueOf(search));
        mRootView.smoothScrollTo(0, mLine * 24);
        Toast.makeText(getBaseContext(), mRootView.getVerticalScrollbarPosition() +" mLine: " + mLine + " mOffset: " + mOffset, Toast.LENGTH_LONG).show();
    }
}
