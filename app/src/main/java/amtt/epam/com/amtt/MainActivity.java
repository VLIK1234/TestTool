package amtt.epam.com.amtt;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import amtt.epam.com.amtt.image.ImageSavingCallback;
import amtt.epam.com.amtt.image.ImageSavingResult;
import amtt.epam.com.amtt.image.ImageSavingTask;


public class MainActivity extends ActionBarActivity implements ImageSavingCallback {

    private int mScreenNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button screenButton = (Button) findViewById(R.id.screen_button);
        screenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageSavingTask().execute(MainActivity.this);
            }
        });

    }


    @Override
    public void onImageSaved(ImageSavingResult result) {
        int resultMessage = result == ImageSavingResult.ERROR ? R.string.saving_error : R.string.saving_success;
        Toast.makeText(this, resultMessage, Toast.LENGTH_SHORT).show();
    }


    public int getScreenNumber() {
        return mScreenNumber;
    }

    public void incrementScreenNumber() {
        mScreenNumber++;
    }

}
