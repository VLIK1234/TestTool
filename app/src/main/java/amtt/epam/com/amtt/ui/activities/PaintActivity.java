package amtt.epam.com.amtt.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.bo.ticket.Step;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.database.util.ContentFromDatabase;
import amtt.epam.com.amtt.database.util.LocalContent;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.dialog.DialogPalette;
import amtt.epam.com.amtt.ui.views.DragTextView;
import amtt.epam.com.amtt.ui.views.MultilineRadioGroup;
import amtt.epam.com.amtt.ui.views.MultilineRadioGroup.OnEntireGroupCheckedChangeListener;
import amtt.epam.com.amtt.ui.views.PaletteItem;
import amtt.epam.com.amtt.ui.views.paintview.ITextDialogButtonClick;
import amtt.epam.com.amtt.ui.views.paintview.PaintMode;
import amtt.epam.com.amtt.ui.views.paintview.PaintView;
import amtt.epam.com.amtt.util.Logger;
import amtt.epam.com.amtt.util.UIUtil;

/**
 @author Artsiom_Kaliaha
 @version on 09.06.2015
 */

public class PaintActivity extends BaseActivity
                            implements OnSeekBarChangeListener, Handler.Callback, OnSystemUiVisibilityChangeListener,
                            OnEntireGroupCheckedChangeListener, ImageLoadingListener, ITextDialogButtonClick, DragTextView.IDrawCallback, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = PaintActivity.class.getSimpleName();
    public static final String KEY_STEP_ID = "key_step_id";
    private static final int HIDDEN_UI_FLAG = View.SYSTEM_UI_FLAG_LOW_PROFILE;
    private static final int HIDE_UI_DELAY = 4000;
    private static final int HIDE_UI = 0;
    private static final int SHOW_UI = 1;

    private Step mStep;
    private PaintView mPaintView;
    private boolean hasSomethingGoneWrong;

    private Handler mHandler;
    private View mDecorView;
    private ActionBar mActionBar;
    public TextView mTextValueThickness;
    public TextView mTextValueOpacity;

    private WindowManager mWindowManager;
    private DragTextView mDragTextView;
    private Paint mPaint;
    private DialogPalette mDialogPalette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersiveUiComponents();
        setContentView(R.layout.activity_paint);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            initPaintView();
            ContentFromDatabase.getStepById(extra.getInt(KEY_STEP_ID), new Callback<List<Step>>() {
                @Override
                public void onLoadStart() {}

                @Override
                public void onLoadExecuted(List<Step> steps) {
                    mStep = steps.get(0);
                    ImageLoader.getInstance().displayImage("file:///" + mStep.getScreenshotPath(), mPaintView, PaintActivity.this);
                }

                @Override
                public void onLoadError(Exception e) {
                    Logger.e(TAG, e.getMessage(), e);
                    setErrorState();
                }
            });
        } else {
            setErrorState();
        }
        mDialogPalette = new DialogPalette(PaintActivity.this, this, this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TopButtonService.sendActionChangeTopButtonVisibility(false);
        mHandler.sendEmptyMessageDelayed(HIDE_UI, HIDE_UI_DELAY);
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
//                if (mPaletteDialog == null) {
//                }
                mDialogPalette.show();
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
        mPaintView.setITextDialogButtonClick(this);
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
                            LocalContent.applyNotesToScreenshot(Bitmap.createBitmap(mPaintView.getDrawingCache()), mStep.getScreenshotPath(), mStep);
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
        if (mActionBar != null) {
            mActionBar.setShowHideAnimationEnabled(true);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mHandler = new Handler(this);
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
            case R.id.sb_thickness:
                mPaintView.setThickness(progress);
//                mDialogPalette.setThickness(progress);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

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
    public void onLoadingCancelled(String imageUri, View view) {}

    private WindowManager.LayoutParams initMainLayoutParams(int x, int y) {
        int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FORMAT_CHANGED;

        WindowManager.LayoutParams mainLayout = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, x, y, WindowManager.LayoutParams.TYPE_APPLICATION,
                flags, PixelFormat.TRANSLUCENT);
        mainLayout.gravity = Gravity.TOP | Gravity.START;
        return mainLayout;
    }

    @Override
    public void onDrawClick(String drawValue, int x, int y, int width, int height, int rightY, Paint paint) {
        mPaintView.drawText(drawValue, x, y, width, height, rightY, paint);

    }

    @Override
    public void onRemoveClick(View view) {
        mWindowManager.removeView(view);
    }

    @Override
    public void onTapClick(final String drawStringValue, final Paint paintText) {
        final View view = ((LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_draw_text, null);
        final EditText editDrawText = (EditText) view.findViewById(R.id.et_draw_text);
        editDrawText.setText(drawStringValue);

        new AlertDialog.Builder(PaintActivity.this)
                .setTitle(getResources().getString(R.string.label_title_draw_text_dialog))
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDragTextView.setDragText(editDrawText.getText().toString(), paintText);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }

    @Override
    public void CreateDragViewCallback(String valueDrawText, Paint textPaint, int xPositionDragView, int yPosiotionDragView) {
        mDragTextView = new DragTextView(getBaseContext(), valueDrawText, textPaint, xPositionDragView, yPosiotionDragView, this);
        mWindowManager.addView(mDragTextView, initMainLayoutParams(xPositionDragView, yPosiotionDragView));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_eraser:
                mPaintView.setPaintMode(PaintMode.ERASE);
                break;
            case R.id.rb_pencil:
                mPaintView.setPaintMode(PaintMode.DRAW);
                break;
            case R.id.rb_text:
                mPaintView.setPaintMode(PaintMode.TEXT);
                break;
            default:
                break;
        }
    }
}
