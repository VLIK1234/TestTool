package amtt.epam.com.amtt.app;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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
import amtt.epam.com.amtt.view.MultilineRadioGroup;
import amtt.epam.com.amtt.view.PaintView;
import amtt.epam.com.amtt.view.PaletteItem;

/**
 * Created by Ivan_Bakach on 09.06.2015.
 */
public class PaintActivity extends BaseActivity implements OnSeekBarChangeListener, Handler.Callback, OnSystemUiVisibilityChangeListener {

    public static final String FILE_PATH = "filePath";
    public static final int HIDDEN_UI_FLAG = View.SYSTEM_UI_FLAG_LOW_PROFILE;
    public static final int HIDE_UI_DELAY = 4000;
    public static final int HIDE_UI = 0;
    public static final int SHOW_UI = 1;

    private String mScreenshotPath;
    private TextView mTextPreview;
    private PaintView mPaintView;
    private AlertDialog mPaletteDialog;
    private boolean hasSomethingGoneWrong;

    private Handler mHandler;
    private View mDecorView;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersiveUiComponents();
        setContentView(R.layout.activity_paint);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            initPaintView();
            initTextPreview();

            mScreenshotPath = extra.getString(FILE_PATH);
            showPreview(mScreenshotPath);
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
                showSavingDialog();
                return true;
            case R.id.action_palette:
                if (mPaletteDialog == null) {
                    initPaletteDialog();
                }
                mPaletteDialog.show();
                return true;
            case R.id.action_undo:
                mPaintView.undo();
                return true;
            case R.id.action_redo:
                mPaintView.redo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        showSavingDialog();
    }

    private void initPaintView() {
        mPaintView = (PaintView) findViewById(R.id.paint_view);
        mPaintView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mHandler.sendEmptyMessage(HIDE_UI);
                        break;
                    case MotionEvent.ACTION_UP:
                        mHandler.sendEmptyMessage(SHOW_UI);
                        break;
                }
                return true;
            }
        });

        mPaintView.setBrushColor(getResources().getColor(R.color.red_paint));
    }

    private void initPaletteDialog() {
        final View view = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_palette, null);

        SeekBar thicknessBar = (SeekBar) view.findViewById(R.id.sb_thickness);
        thicknessBar.setProgress(PaintView.DEFAULT_BRUSH_THICKNESS);
        thicknessBar.setOnSeekBarChangeListener(this);

        final SeekBar opacityBar = (SeekBar) view.findViewById(R.id.sb_opacity);
        opacityBar.setMax(mPaintView.DEFAULT_OPACITY);
        opacityBar.setProgress(mPaintView.DEFAULT_OPACITY);
        opacityBar.setOnSeekBarChangeListener(this);

        final ImageView opacityImage = (ImageView) view.findViewById(R.id.iv_opacity);

        final MultilineRadioGroup multilineRadioGroup = (MultilineRadioGroup) view.findViewById(R.id.multi_line_radio_group);
        multilineRadioGroup.setOnEntireGroupCheckedListener(new MultilineRadioGroup.OnEntireGroupCheckedChangeListener() {
            @Override
            public void onCheckedChanged(PaletteItem paletteItem) {
                mPaintView.setBrushColor(paletteItem.getColor());
            }
        });
        RadioGroup paintToolsGroup = (RadioGroup) view.findViewById(R.id.rg_paint_tools);
        paintToolsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean isEraseMode = checkedId == R.id.rb_eraser;
                mPaintView.setEraseMode(isEraseMode);
                if (isEraseMode) {
                    opacityBar.setEnabled(false);
                    opacityImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_opacity_disabled));
                    multilineRadioGroup.clearCheck();
                } else {
                    opacityBar.setEnabled(true);
                    opacityImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_opacity));
                    multilineRadioGroup.restoreCheck();
                }
            }
        });
        ((RadioButton) paintToolsGroup.findViewById(R.id.rb_pencil)).setChecked(true);

        mPaletteDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.title_choose_color)
                .setView(view)
                .create();
    }

    private void initTextPreview() {
        mTextPreview = (TextView) findViewById(R.id.tv_preview);
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
        if (filePath.contains(MimeType.IMAGE_PNG.getFileExtension()) ||
                filePath.contains(MimeType.IMAGE_JPG.getFileExtension()) ||
                filePath.contains(MimeType.IMAGE_JPEG.getFileExtension())) {
            ImageLoader.getInstance().displayImage("file:///" + filePath, mPaintView, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    showProgress(true);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    showProgress(false);
                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
                    hasSomethingGoneWrong = true;
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    showProgress(false);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        } else if (filePath.contains(MimeType.TEXT_PLAIN.getFileExtension())) {
            int dpSize = UIUtil.getInDp(8);
            mTextPreview.setPadding(dpSize, dpSize, dpSize, dpSize);
            mTextPreview.setText(readLogFromFile(filePath));
            mTextPreview.setVisibility(View.VISIBLE);
        }
    }

    private void showSavingDialog() {
        if (hasSomethingGoneWrong) {
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_lose_notes)
                    .setMessage(R.string.message_lose_notes)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final Bitmap drawingCache = mPaintView.getDrawingCache();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    StepUtil.applyNotesToScreenshot(drawingCache, mScreenshotPath);
                                }
                            }).start();
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
        }
    }

    private void setImmersiveUiComponents() {
        mDecorView = getWindow().getDecorView();
        mDecorView.setOnSystemUiVisibilityChangeListener(this);
        mActionBar = getSupportActionBar();
        mActionBar.setShowHideAnimationEnabled(true);
        mHandler = new Handler(this);
        mHandler.sendEmptyMessageDelayed(HIDE_UI, HIDE_UI_DELAY);
    }

    public SpannableStringBuilder append(SpannableStringBuilder stringBuilder, CharSequence text, Object what, int flags) {
        int start = stringBuilder.length();
        stringBuilder.append(text);
        stringBuilder.setSpan(what, start, stringBuilder.length(), flags);
        return stringBuilder;
    }

    //Callbacks
    //SeekBar
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_opacity:
                mPaintView.setBrushOpacity(progress);
                break;
            case R.id.sb_thickness:
                mPaintView.setBrushThickness(progress);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //Handler
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case HIDE_UI:
                mDecorView.setSystemUiVisibility(HIDDEN_UI_FLAG);
                break;
            case SHOW_UI:
                mDecorView.setSystemUiVisibility(NO_FLAGS);
                break;
        }
        return true;
    }

    //Immersive UI
    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        if (visibility == HIDDEN_UI_FLAG) {
            mActionBar.hide();
        } else {
            mHandler.sendEmptyMessageDelayed(HIDE_UI, HIDE_UI_DELAY);
            mActionBar.show();
        }
    }

}
