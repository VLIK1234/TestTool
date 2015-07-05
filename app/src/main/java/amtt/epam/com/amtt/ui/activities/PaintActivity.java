package amtt.epam.com.amtt.ui.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.views.MultilineRadioGroup;
import amtt.epam.com.amtt.ui.views.MultilineRadioGroup.OnEntireGroupCheckedChangeListener;
import amtt.epam.com.amtt.ui.views.PaintView;
import amtt.epam.com.amtt.ui.views.PaletteItem;

/**
 * Created by Artsiom_Kaliaha on 09.06.2015.
 */
public class PaintActivity extends BaseActivity implements OnSeekBarChangeListener,
        Handler.Callback,
        OnSystemUiVisibilityChangeListener, OnEntireGroupCheckedChangeListener {

    public static final String FILE_PATH = "filePath";
    public static final int HIDDEN_UI_FLAG = View.SYSTEM_UI_FLAG_LOW_PROFILE;
    public static final int HIDE_UI_DELAY = 4000;
    public static final int HIDE_UI = 0;
    public static final int SHOW_UI = 1;

    private String mScreenshotPath;
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

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            mScreenshotPath = extra.getString(FILE_PATH);
            initPaintView();
            ImageLoader.getInstance().displayImage("file:///" + mScreenshotPath, mPaintView, new ImageLoadingListener() {
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
        opacityBar.setMax(PaintView.DEFAULT_OPACITY);
        opacityBar.setProgress(PaintView.DEFAULT_OPACITY);
        opacityBar.setOnSeekBarChangeListener(this);

        final ImageView opacityImage = (ImageView) view.findViewById(R.id.iv_opacity);

        final MultilineRadioGroup multilineRadioGroup = (MultilineRadioGroup) view.findViewById(R.id.multi_line_radio_group);
        multilineRadioGroup.setOnEntireGroupCheckedListener(this);

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
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mHandler = new Handler(this);
        mHandler.sendEmptyMessageDelayed(HIDE_UI, HIDE_UI_DELAY);
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

    //Palette items (colors)
    @Override
    public void onCheckedChanged(PaletteItem paletteItem) {
        mPaintView.setBrushColor(paletteItem.getColor());
    }

}
