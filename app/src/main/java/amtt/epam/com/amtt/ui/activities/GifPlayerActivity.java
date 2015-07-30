package amtt.epam.com.amtt.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.File;

import amtt.epam.com.amtt.R;

/**
 * @author Ivan_Bakach
 * @version on 30.07.2015
 */
public class GifPlayerActivity extends Activity {

    public static final String GIF_IMAGE_KEY = "gifeFilePath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_player);

        WebView webView = (WebView) findViewById(R.id.wv_web_player);
        TextView errorMessage = (TextView) findViewById(R.id.tv_error_message);
        Bundle extra = getIntent().getExtras();
        if (extra!=null) {
            String gifFilePath = extra.getString(GIF_IMAGE_KEY);
            File gifFile = new File(gifFilePath);
            setTitle(gifFile.getName());
            webView.loadUrl(gifFile.toURI().toString());
        } else{
            webView.setVisibility(View.GONE);
            errorMessage.setVisibility(View.VISIBLE);
        }
    }
}
