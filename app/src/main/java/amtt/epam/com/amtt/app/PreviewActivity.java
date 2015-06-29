package amtt.epam.com.amtt.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.http.MimeType;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
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
        View view;

        final AlertDialog.Builder paletteDialog = new AlertDialog.Builder(PreviewActivity.this, R.style.Dialog)
                .setMessage("mess")
                .setTitle("title")
                .setNegativeButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.dismiss();
                        finish();
                    }
                });

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            view = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_preview, null);
            initImagePreview(view);
            initTextPreview(view);
            initPaintView(view);

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
                    .setPositiveButton("Customize brush", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(PreviewActivity.this, PreviewActivity.class));
                            finish();
                        }
                    })
                    .show();
        } else {
            view = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_preview_palette, null);
            paletteDialog
                    .setView(view)
                    .create()
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TopButtonService.sendActionChangeVisibilityTopbutton(false);
    }

    private void initImagePreview(View view) {
        //mImagePreview = (ImageView) view.findViewById(R.id.iv_preview);
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
                    append(builder, line, new ForegroundColorSpan(Color.RED), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE).append("\n");
                } else if (line.contains("W/")) {
                    append(builder, line, new ForegroundColorSpan(Color.BLUE), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE).append("\n");
                } else {
                    builder.append(line).append("\n");
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder;
    }

    public void showPreview(String filePath) {
        if (filePath.contains(MimeType.IMAGE_PNG.getFileExtension()) || filePath.contains(MimeType.IMAGE_JPG.getFileExtension()) || filePath.contains(MimeType.IMAGE_JPEG.getFileExtension())) {
            //ImageLoader.getInstance().displayImage("file:///" + filePath, mImagePreview);
        } else if (filePath.contains(MimeType.TEXT_PLAIN.getFileExtension())) {
            int dpSize = UIUtil.getInDp(8);
            mTextPreview.setPadding(dpSize, dpSize, dpSize, dpSize);
            mTextPreview.setText(readLogFromFile(filePath));
            mTextPreview.setVisibility(View.VISIBLE);
        }
    }


    public SpannableStringBuilder append(SpannableStringBuilder stringBuilder, CharSequence text, Object what, int flags) {
        int start = stringBuilder.length();
        stringBuilder.append(text);
        stringBuilder.setSpan(what, start, stringBuilder.length(), flags);
        return stringBuilder;
    }

}
