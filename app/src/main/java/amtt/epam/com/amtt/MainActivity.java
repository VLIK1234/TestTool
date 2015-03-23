package amtt.epam.com.amtt;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import amtt.epam.com.amtt.database.DbSavingCallback;
import amtt.epam.com.amtt.database.DbSavingResult;
import amtt.epam.com.amtt.database.DbSavingTask;
import amtt.epam.com.amtt.image.ImageSavingCallback;
import amtt.epam.com.amtt.image.ImageSavingResult;
import amtt.epam.com.amtt.image.ImageSavingTask;


public class MainActivity extends ActionBarActivity implements ImageSavingCallback, DbSavingCallback {

    private int mScreenNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button screenButton = (Button) findViewById(R.id.save_image_button);
        screenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = getWindow().getDecorView();
                rootView.setDrawingCacheEnabled(true);
                Bitmap bitmap = rootView.getDrawingCache();
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

                new ImageSavingTask(MainActivity.this,bitmap,rect,getCacheDir().getPath()).execute();
            }
        });

        Button activityInfoButton = (Button)findViewById(R.id.save_activity_info_button);
        activityInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DbSavingTask(MainActivity.this, MainActivity.this.getComponentName()).execute();
            }
        });
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

}
