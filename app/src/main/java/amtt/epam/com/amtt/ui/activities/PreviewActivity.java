package amtt.epam.com.amtt.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.File;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.LogUtils;
import amtt.epam.com.amtt.util.ReadLargeTextUtil;

/**
 * Created by Ivan_Bakach on 09.06.2015.
 */
public class PreviewActivity extends AppCompatActivity {

    public static final String FILE_PATH = "filePath";
    private TextView textPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        textPreview = (TextView) findViewById(R.id.text_preview);

        String title = "";
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String filePath = extra.getString(FILE_PATH);
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

    public void showPreview(String filePath) {
        if (FileUtil.isText(filePath)) {
            textPreview.setText(readLogFromFile(filePath));
        }
    }
}
