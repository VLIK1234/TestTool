package amtt.epam.com.amtt.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.File;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;

/**
 * @author Ivan_Bakach
 * @version on 30.07.2015
 */
public class GifPlayerActivity extends BaseActivity {

    public static final String GIF_IMAGE_KEY = "gifeFilePath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_player);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }

        WebView webView = (WebView) findViewById(R.id.wv_web_player);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        TextView errorMessage = (TextView) findViewById(R.id.tv_error_message);
        Bundle extra = getIntent().getExtras();
        if (extra != null && extra.getString(GIF_IMAGE_KEY) != null) {
            File gifFile = new File(extra.getString(GIF_IMAGE_KEY));
            setTitle(gifFile.getName());
            webView.loadUrl(gifFile.toURI().toString());
        } else {
            webView.setVisibility(View.GONE);
            errorMessage.setVisibility(View.VISIBLE);
        }
    }
}
