package amtt.epam.com.amtt.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.UIUtil;
import amtt.epam.com.amtt.view.PaintView;

/**
 * Created by Ivan_Bakach on 09.06.2015.
 */
public class PreviewActivity extends Activity {

    public static final String FILE_PATH = "filePath";
    public static final double SCALE_DIALOG_MARGIN_RATIO = 0.8;

    private ImageView mImagePreview;
    private TextView mTextPreview;
    private PaintView mPaintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_preview, null);
        initImagePreview(view);
        initTextPreview(view);
        initPaintView(view);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            final String screenshotPath = extra.getString(FILE_PATH);
            showPreview(screenshotPath);

            new AlertDialog.Builder(this, R.style.Dialog)
                    .setView(view)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(final DialogInterface dialog) {
                            PreviewActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PreviewActivity.this.finish();
                        }
                    })
                    .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StepUtil.applyNotesToScreenshot(mPaintView, mImagePreview, screenshotPath);
                            PreviewActivity.this.finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TopButtonService.sendActionChangeVisibilityTopbutton(false);
    }

    private void initImagePreview(View view) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        mImagePreview = (ImageView) view.findViewById(R.id.iv_preview);
        mImagePreview.getLayoutParams().width = (int) (width * SCALE_DIALOG_MARGIN_RATIO);
        mImagePreview.getLayoutParams().height = (int) (height * SCALE_DIALOG_MARGIN_RATIO);
    }

    private void initTextPreview(View view) {
        mTextPreview = (TextView) view.findViewById(R.id.tv_preview);
    }

    private void initPaintView(View view) {
        mPaintView = (PaintView) view.findViewById(R.id.paint_view);
        mPaintView.setDrawingCacheEnabled(true);
    }

    private CharSequence readLogFromFile(String filePath) {
        File file = new File(filePath);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("E/")) {
                    append(builder, line, new ForegroundColorSpan(Color.RED), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append("\n");
                } else if (line.contains("W/")) {
                    append(builder, line, new ForegroundColorSpan(Color.BLUE), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append("\n");
                } else {
                    builder.append(line);
                    builder.append("\n");
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }

    public void showPreview(String filePath) {
        if (filePath.contains(".png") || filePath.contains(".jpg") || filePath.contains(".jpeg")) {
            ImageLoader.getInstance().displayImage("file:///" + filePath, mImagePreview);
        } else if (filePath.contains(".txt")) {
            int dpSize = UIUtil.getInDp(8);
            mTextPreview.setPadding(dpSize, dpSize, dpSize, dpSize);
            mTextPreview.setText(readLogFromFile(filePath));
        }
    }


    public SpannableStringBuilder append(SpannableStringBuilder spannableString, CharSequence text, Object what, int flags) {
        int start = spannableString.length();
        spannableString.append(text);
        spannableString.setSpan(what, start, spannableString.length(), flags);
        return spannableString;
    }

}
