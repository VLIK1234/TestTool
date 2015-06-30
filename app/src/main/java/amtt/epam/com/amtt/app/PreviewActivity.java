package amtt.epam.com.amtt.app;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.http.MimeType;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.UIUtil;
import amtt.epam.com.amtt.view.PaintView;

/**
 * Created by Ivan_Bakach on 09.06.2015.
 */
public class PreviewActivity extends BaseActivity {

    public static final String FILE_PATH = "filePath";
    public static final double SCALE_DIALOG_MARGIN_RATIO = 0.8;

    private String mScreenshotPath;

    private TextView mTextPreview;
    private PaintView mPaintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        final AlertDialog.Builder paletteDialog = new AlertDialog.Builder(PreviewActivity.this, R.style.Dialog)
//                .setMessage("mess")
//                .setTitle("title")
//                .setNegativeButton("close", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog1, int which) {
//                        dialog1.dismiss();
//                        finish();
//                    }
//                });

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            initPaintView();
            initTextPreview();

            mScreenshotPath = extra.getString(FILE_PATH);
            showPreview(mScreenshotPath);

//            new AlertDialog.Builder(this, R.style.Dialog)
//                    .setView(view)
//                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(final DialogInterface dialog) {
//                            PreviewActivity.this.finish();
//                        }
//                    })
//                    .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            PreviewActivity.this.finish();
//                        }
//                    })
//                    .setPositiveButton("Customize brush", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            startActivity(new Intent(PreviewActivity.this, PreviewActivity.class));
//                            finish();
//                        }
//                    })
//                    .show();
        } else {
//            view = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_preview_palette, null);
//            paletteDialog
//                    .setView(view)
//                    .create()
//                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TopButtonService.sendActionChangeVisibilityTopbutton(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_lose_notes)
                        .setMessage(R.string.message_lose_notes)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StepUtil.applyNotesToScreenshot(mPaintView, mScreenshotPath);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNeutralButton(R.string.label_continue_editing, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
                return true;
            case R.id.action_palette:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_choose_color)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StepUtil.applyNotesToScreenshot(mPaintView, mScreenshotPath);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNeutralButton(R.string.label_continue_editing, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initPaintView() {
        mPaintView = (PaintView) findViewById(R.id.paint_view);
        mPaintView.setDrawingCacheEnabled(true);
    }

    private void initTextPreview() {
        mTextPreview = (TextView) findViewById(R.id.tv_preview);
    }

//    private void setPaintViewFitToScreenshot() {
//        ViewGroup.LayoutParams layoutParams = mPaintView.getLayoutParams();
//        layoutParams.height = mImagePreview.getHeight();
//        layoutParams.width = mImagePreview.getWidth();
//        mPaintView.setLayoutParams(layoutParams);
//    }

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
        if (filePath.contains(MimeType.IMAGE_PNG.getFileExtension()) ||
                filePath.contains(MimeType.IMAGE_JPG.getFileExtension()) ||
                filePath.contains(MimeType.IMAGE_JPEG.getFileExtension())) {
            ImageLoader.getInstance().displayImage("file:///" + filePath, mPaintView);
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
