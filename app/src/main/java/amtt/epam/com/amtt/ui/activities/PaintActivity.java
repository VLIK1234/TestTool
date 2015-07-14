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

import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.table.StepsTable;
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
        OnSystemUiVisibilityChangeListener,
        OnEntireGroupCheckedChangeListener,
        IResult<List<Step>>,
        ImageLoadingListener {

    public static final String STEP_ID_PATH = "step_id_path";
    public static final int HIDDEN_UI_FLAG = View.SYSTEM_UI_FLAG_LOW_PROFILE;
    public static final int HIDE_UI_DELAY = 4000;
    public static final int HIDE_UI = 0;
    public static final int SHOW_UI = 1;

    private Step mStep;
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
            initPaintView();
            DbObjectManager.INSTANCE.query(new Step(), StepsTable.PROJECTION, new String[]{StepsTable._ID}, new String[]{String.valueOf(extra.getInt(STEP_ID_PATH))}, this);
        } else {
            setErrorState();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TopButtonService.sendActionChangeTopButtonVisibility(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_palette, menu);
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
            case R.id.action_clear:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.title_clear_notes))
                        .setMessage(R.string.message_clear_notes)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPaintView.clear();
                            }
                        })
                        .create()
                        .show();
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

        final SeekBar thicknessBar = (SeekBar) view.findViewById(R.id.sb_thickness);
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
                    thicknessBar.setProgress(mPaintView.getEraserThickness());
                    opacityBar.setEnabled(false);
                    opacityImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_opacity_disabled));
                    multilineRadioGroup.clearCheck();
                } else {
                    thicknessBar.setProgress(mPaintView.getBrushThickness());
                    opacityBar.setEnabled(true);
                    opacityImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_opacity));
                    multilineRadioGroup.restoreCheck();
                    multilineRadioGroup.setEnabled(true);
                }
            }
        });
        ((RadioButton) paintToolsGroup.findViewById(R.id.rb_pencil)).setChecked(true);

        mPaletteDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.title_drawing_customization)
                .setView(view)
                .create();
    }

    private void showSavingDialog() {
        if (hasSomethingGoneWrong) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_lose_notes)
                    .setMessage(R.string.message_lose_notes)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StepUtil.applyNotesToScreenshot(Bitmap.createBitmap(mPaintView.getDrawingCache()), mStep.getScreenshotPath(), mStep);
                            setResult(RESULT_OK);
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setResult(RESULT_CANCELED);
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

    private void setErrorState() {
        showProgress(false);
        findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
        hasSomethingGoneWrong = true;
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
                mPaintView.setThickness(progress);
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

    //Database
    @Override
    public void onResult(List<Step> result) {
        mStep = result.get(0);
        ImageLoader.getInstance().displayImage("file:///" + mStep.getScreenshotPath(), mPaintView, this);
    }

    @Override
    public void onError(Exception e) {
        setErrorState();
    }

    //ImageLoader
    @Override
    public void onLoadingStarted(String imageUri, View view) {
        showProgress(true);
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        setErrorState();
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        showProgress(false);
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {

    }

}
