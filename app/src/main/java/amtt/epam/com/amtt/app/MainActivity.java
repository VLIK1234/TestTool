package amtt.epam.com.amtt.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.crash.AmttExceptionHandler;
import amtt.epam.com.amtt.database.task.DataBaseOperationType;
import amtt.epam.com.amtt.database.task.DataBaseTask;
import amtt.epam.com.amtt.database.task.DataBaseTaskResult;
import amtt.epam.com.amtt.database.task.StepSavingCallback;
import amtt.epam.com.amtt.fragment.CrashDialogFragment;
import amtt.epam.com.amtt.util.IOUtils;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends BaseActivity implements StepSavingCallback {

    private int mScreenNumber = 1;
    private String mCrashFilePath;
    private static final String CRASH_DIALOG_TAG = "crash_dialog_tag";
    private static int sStepNumber;
    private DataBaseTask mDataBaseClearTask;

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


        mDataBaseClearTask = new DataBaseTask.Builder()
                .setOperationType(DataBaseOperationType.CLEAR)
                .setContext(MainActivity.this)
                .create();
        mDataBaseClearTask.execute();

        Button clearDbButton = (Button) findViewById(R.id.clear_db_button);
        clearDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sStepNumber = 0;
                mDataBaseClearTask.execute();
            }
        });

        Button stepButton = (Button) findViewById(R.id.step_button);
        stepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = getWindow().getDecorView();
                rootView.setDrawingCacheEnabled(true);
                Bitmap bitmap = rootView.getDrawingCache();
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                sStepNumber++;

                new DataBaseTask.Builder()
                        .setOperationType(DataBaseOperationType.SAVE_STEP)
                        .setContext(MainActivity.this)
                        .setBitmap(bitmap)
                        .setRect(rect)
                        .setComponentName(MainActivity.this.getComponentName())
                        .setStepNumber(sStepNumber)
                        .create()
                        .execute();
            }
        });

        Button showStepsButton = (Button) findViewById(R.id.show_steps_button);
        showStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StepsActivity.class));
            }
        });

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

    @Override
    public int getScreenNumber() {
        return mScreenNumber;
    }


    @Override
    public void onDataBaseActionDone(DataBaseTaskResult result) {
        int resultMessage = result == DataBaseTaskResult.ERROR ? R.string.data_base_action_error : R.string.data_base_action_done;
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void incrementScreenNumber() {
        mScreenNumber++;
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
