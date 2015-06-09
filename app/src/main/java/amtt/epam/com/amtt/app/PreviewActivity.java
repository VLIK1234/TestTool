package amtt.epam.com.amtt.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import amtt.epam.com.amtt.CoreApplication;
import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;

/**
 * Created by Ivan_Bakach on 09.06.2015.
 */
public class PreviewActivity extends Activity{

    public static final String FILE_PATH = "filePath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        TopButtonService.sendActionChangeVisibilityButton();
        ImageView imagePreview = (ImageView) findViewById(R.id.image_preview);
        TextView textPreview = (TextView) findViewById(R.id.text_preview);
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (8*scale + 0.5f);
        Bundle extra = getIntent().getExtras();
        if (extra!=null) {
            String filePath = extra.getString(FILE_PATH);
            Pattern p = Pattern.compile("[-_0-9a-zA-Z]*[.]\\w{0,5}");
            Matcher m = p.matcher(filePath);
            if (m.find()) {
                setTitle(m.group());
            }
            if(filePath.contains(".png")||filePath.contains(".jpg")||filePath.contains(".jpeg")){
                ImageLoader imageLoader = CoreApplication.getImageLoader();
                imageLoader.displayImage("file:///"+filePath, imagePreview);
            }else if (filePath.contains(".txt")) {
                textPreview.setPadding(dpAsPixels,dpAsPixels,dpAsPixels,dpAsPixels);
                textPreview.setText(readTextFromFile(filePath));
            }
        }
    }

    private CharSequence readTextFromFile(String filePath){
        File file = new File(filePath);
        StringBuilder text = new StringBuilder();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains("E/")){
                    append(builder, line, new ForegroundColorSpan(Color.RED), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append("\n");
                } else if (line.contains("W/")) {
                    append(builder, line, new ForegroundColorSpan(Color.BLUE), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append("\n");
                }
                else {
                    builder.append(line);
                    builder.append("\n");
                }
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }

    public SpannableStringBuilder append(SpannableStringBuilder spannableString, CharSequence text, Object what, int flags) {
        int start = spannableString.length();
        spannableString.append(text);
        spannableString.setSpan(what, start, spannableString.length(), flags);
        return spannableString;
    }
}
