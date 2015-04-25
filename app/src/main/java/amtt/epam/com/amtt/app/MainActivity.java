package amtt.epam.com.amtt.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.crash.AmttExceptionHandler;
import amtt.epam.com.amtt.fragment.CrashDialogFragment;
import amtt.epam.com.amtt.util.IOUtils;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends BaseActivity {

    private String mCrashFilePath;
    private static final String CRASH_DIALOG_TAG = "crash_dialog_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        Button crashButton = (Button) findViewById(R.id.crash_button);
        crashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new IllegalStateException("stakkato caused crash");
            }
        });

        Thread.currentThread().setUncaughtExceptionHandler(new AmttExceptionHandler(this));

        final Button showCrashInfoButton = (Button) findViewById(R.id.show_crash_info);
        showCrashInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rawString = loadCrashData();
                if (rawString != null) {
                    CrashDialogFragment crashDialogFragment = CrashDialogFragment.newInstance(rawString, mCrashFilePath);
                    crashDialogFragment.show(getFragmentManager(), CRASH_DIALOG_TAG);
                }
            }
        });

        final Button settingsButton = (Button) findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        mCrashFilePath = getFilesDir().getPath() + "/crash.txt";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }


    private String loadCrashData() {
        String rawString = null;
        try {
            rawString = IOUtils.loadStringFromInternalStorage(mCrashFilePath);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, R.string.lack_of_crashes_text, Toast.LENGTH_SHORT).show();
        }
        return rawString;
    }

}
