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
import amtt.epam.com.amtt.database.DbClearTask;
import amtt.epam.com.amtt.database.DbSavingCallback;
import amtt.epam.com.amtt.database.DbSavingResult;
import amtt.epam.com.amtt.database.DbSavingTask;
import amtt.epam.com.amtt.image.ImageSavingResult;
import amtt.epam.com.amtt.image.ImageSavingTask;
import amtt.epam.com.amtt.service.TopButtonService;
import amtt.epam.com.amtt.step.StepSavingCallback;
import amtt.epam.com.amtt.step.StepSavingResult;
import amtt.epam.com.amtt.step.StepSavingTask;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends BaseActivity implements DbSavingCallback, StepSavingCallback {

    private int mScreenNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        startService(new Intent(this, TopButtonService.class));
        TopButtonService.show(this);
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
        Button screenButton = (Button) findViewById(R.id.save_image_button);
        screenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = getWindow().getDecorView();
                rootView.setDrawingCacheEnabled(true);
                Bitmap bitmap = rootView.getDrawingCache();
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

                new ImageSavingTask(MainActivity.this, bitmap, rect, getCacheDir().getPath()).execute();
            }
        });

        Button activityInfoButton = (Button) findViewById(R.id.save_activity_info_button);
        activityInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DbSavingTask(MainActivity.this, MainActivity.this.getComponentName()).execute();
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

                new StepSavingTask(MainActivity.this, MainActivity.this, bitmap, rect, MainActivity.this.getComponentName()).execute();
            }
        });

        Button clearDbbutton = (Button) findViewById(R.id.clear_db_button);
        clearDbbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DbClearTask(MainActivity.this).execute();
            }
        });
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
    public void onImageSaved(ImageSavingResult result) {
        mScreenNumber++;
        int resultMessage = result == ImageSavingResult.ERROR ? R.string.image_saving_error : R.string.image_saving_success;
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getScreenNumber() {
        return mScreenNumber;
    }

    @Override
    public void onDbInfoSaved(DbSavingResult result) {
        int resultMessage = result == DbSavingResult.ERROR ? R.string.db_saving_error : R.string.db_saving_success;
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSaved(StepSavingResult result) {
        int resultMessage = result == StepSavingResult.ERROR ? R.string.step_saving_error : R.string.step_saving_success;
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void incrementScreenNumber() {
        mScreenNumber++;
    }

    public void onClickShow(View view) {
        TopButtonService.show(this);
    }

    public void onClickStop(View view) {
        TopButtonService.close(this);
    }

    public void onClickSecond(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

}
