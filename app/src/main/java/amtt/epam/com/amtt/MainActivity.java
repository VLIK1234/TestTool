package amtt.epam.com.amtt;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;


public class MainActivity extends ActionBarActivity {

    private int mScreenNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button screenButton = (Button) findViewById(R.id.screen_button);
        screenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageAsyncSaving().execute();
            }
        });

    }

    private class ImageAsyncSaving extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            View rootView = MainActivity.this.getWindow().getDecorView();
            rootView.setDrawingCacheEnabled(true);
            Bitmap bitmap = rootView.getDrawingCache();
            Rect rect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            bitmap = Bitmap.createBitmap(bitmap, 0, rect.top, rect.width(), rect.height());

            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(getCacheDir().getPath() + "/screen " + mScreenNumber + ".png"));
                mScreenNumber++;
            } catch (Exception e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, R.string.saving_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

}
