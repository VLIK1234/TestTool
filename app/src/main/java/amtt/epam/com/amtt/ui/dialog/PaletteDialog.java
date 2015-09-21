package amtt.epam.com.amtt.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.ui.views.MultilineRadioGroup;
import amtt.epam.com.amtt.ui.views.PaletteItem;
import amtt.epam.com.amtt.ui.views.RadioGroupLine;
import amtt.epam.com.amtt.ui.views.paintview.PaintMode;
import amtt.epam.com.amtt.ui.views.paintview.PaintView;
import amtt.epam.com.amtt.util.PreferenceUtil;

/**
 * @author Ivan Bakach
 * @version on 13.09.2015
 */
public class PaletteDialog extends AlertDialog implements SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener,
        MultilineRadioGroup.OnEntireGroupCheckedChangeListener {

    public static final int _50_PERCENT_ALPHA = 128;
    public static final int PREVIEW_MARKER_TEXT_SIZE = 130;
    private Paint mPaint = new Paint();
    private PaintMode mPaintMode;
    private TextView mTextValueThickness;
    private AlertDialog mAlertDialog;
    private SeekBar mThicknessBar;
    private ImageView mPreviewImage;
    private MultilineRadioGroup mMultilineRadioGroup;
    private RadioGroup mPaintToolsGroup;
    private OnDismissListener mDismissListener;

    public PaletteDialog(Context context, OnDismissListener dismissListener) {
        super(context);
        mDismissListener = dismissListener;
        initPaletteDialog(context, mDismissListener);

    }

    private void initPaletteDialog(Context context, OnDismissListener listener) {
        View view = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_palette, null);
        mPreviewImage = (ImageView) view.findViewById(R.id.iv_preview);
        mTextValueThickness = (TextView) view.findViewById(R.id.tv_thickness_value);
        mThicknessBar = (SeekBar) view.findViewById(R.id.sb_thickness);
        mMultilineRadioGroup = (MultilineRadioGroup) view.findViewById(R.id.multi_line_radio_group);
        mPaintToolsGroup = (RadioGroup) view.findViewById(R.id.rg_paint_tools);

        mThicknessBar.setMax(PaintView.DEFAULT_THICKNESS);
        mThicknessBar.setProgress(PreferenceUtil.getInt(context.getString(R.string.key_thickness_progress_dialog_palette), 40));
        mThicknessBar.setOnSeekBarChangeListener(this);
        mTextValueThickness.setText(Integer.toString(mThicknessBar.getProgress()));

        mMultilineRadioGroup.setOnEntireGroupCheckedListener(this);
        for (int i = 0; i < mMultilineRadioGroup.getChildCount(); i++) {
            RadioGroupLine line = ((RadioGroupLine) mMultilineRadioGroup.getChildAt(i));
            for (int j = 0; j < line.getChildCount(); j++) {
                if (PreferenceUtil.getInt(getContext().getString(R.string.key_checked_palette_item),
                        ((PaletteItem)line.getChildAt(j)).getColor())==((PaletteItem)line.getChildAt(j)).getColor()) {
                    ((PaletteItem) line.getChildAt(j)).setChecked(true);break;
                }
            }
        }

        mPaintToolsGroup.setOnCheckedChangeListener(this);
        for (int i = 0; i<mPaintToolsGroup.getChildCount(); i++) {
            if (PreferenceUtil.getInt(getContext().getString(R.string.key_instrument_for_draw_dialog_palette),
                    mPaintToolsGroup.getChildAt(i).getId())==mPaintToolsGroup.getChildAt(i).getId()) {
                ((RadioButton) mPaintToolsGroup.getChildAt(i)).setChecked(true);break;
            }
        }
        mPaint = getCurrentPaint();
        mPreviewImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mPaint = getCurrentPaint();
                drawPreview(getCurrentPaint());
                return true;
            }
        });

        mAlertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.title_drawing_customization)
                .setView(view)
                .setOnDismissListener(listener)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    public void show(){
        initPaletteDialog(getContext(), mDismissListener);
        mAlertDialog.show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_thickness:
                mTextValueThickness.setText(Integer.toString(progress));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        PreferenceUtil.putInt(getContext().getString(R.string.key_thickness_progress_dialog_palette), seekBar.getProgress());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        PreferenceUtil.putInt(getContext().getString(R.string.key_instrument_for_draw_dialog_palette), checkedId);
        switch (checkedId) {
            case R.id.rb_eraser:
                mPaintMode = PaintMode.ERASE;
                break;
            case R.id.rb_brush:
                mPaintMode = PaintMode.DRAW;
                break;
            case R.id.rb_marker:
                mPaintMode = PaintMode.DRAW;
                break;
            case R.id.rb_text:
                mPaintMode = PaintMode.TEXT;
                break;
            default:
                break;
        }
    }

    //Palette items (colors)
    @Override
    public void onCheckedChanged(PaletteItem paletteItem) {
        PreferenceUtil.putInt(getContext().getString(R.string.key_checked_palette_item), paletteItem.getColor());
//        mPaintView.setBrushColor(paletteItem.getColor());
    }

    public Paint getPaint() {
        return mPaint;
    }

    public PaintMode getPaintMode() {
        return mPaintMode;
    }

    private void drawPreview(Paint paint){
        final Bitmap cacheCanvasBitmap = Bitmap.createBitmap(mPreviewImage.getMeasuredWidth(),
                mPreviewImage.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(cacheCanvasBitmap);
        canvas.drawBitmap(cacheCanvasBitmap, 0, 0, null);
        switch (mPaintToolsGroup.getCheckedRadioButtonId()) {
            case R.id.rb_text:
                int border = 30;
                canvas.drawText(getContext().getString(R.string.label_preview_text_dialog_palette),
                        mPreviewImage.getMeasuredWidth() / 4, mPreviewImage.getMeasuredHeight() - border, paint);
                break;
            case R.id.rb_brush:
                canvas.drawLine(0, mPreviewImage.getMeasuredHeight() / 2,
                        mPreviewImage.getMeasuredWidth(), mPreviewImage.getMeasuredHeight() / 2, paint);
                break;
            case R.id.rb_marker:
                Paint textPaint = new Paint();
                textPaint.setTextSize(PREVIEW_MARKER_TEXT_SIZE);
                textPaint.setColor(Color.BLACK);
                int borderPreviewText = 70;
                canvas.drawText(getContext().getString(R.string.label_preview_text_dialog_palette),
                        mPreviewImage.getMeasuredWidth() / 4, mPreviewImage.getMeasuredHeight() - borderPreviewText, textPaint);
                canvas.drawLine(0, mPreviewImage.getMeasuredHeight() / 2,
                        mPreviewImage.getMeasuredWidth(), mPreviewImage.getMeasuredHeight() / 2, paint);
                break;
            case R.id.rb_eraser:
                paint.setXfermode(null);
                Bitmap textureBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.alpha_erase_texture);
                BitmapShader shader = new BitmapShader(textureBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                paint.setShader(shader);
                canvas.drawLine(0, mPreviewImage.getMeasuredHeight() / 2,
                        mPreviewImage.getMeasuredWidth(), mPreviewImage.getMeasuredHeight() / 2, paint);
                break;
        }
        mPreviewImage.setImageBitmap(cacheCanvasBitmap);
    }

    private Paint getCurrentPaint(){
        Paint paint = new Paint();
        if (mMultilineRadioGroup.getChekedItem() != null) {
            paint.setColor(mMultilineRadioGroup.getChekedItem().getColor());
        }
        switch (mPaintToolsGroup.getCheckedRadioButtonId()) {
            case R.id.rb_text:
                paint.setTextSize(mThicknessBar.getProgress());
                break;
            case R.id.rb_brush:
                paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setStrokeWidth(mThicknessBar.getProgress());
                break;
            case R.id.rb_marker:
                paint.setFlags(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setAlpha(_50_PERCENT_ALPHA);
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setStrokeWidth(mThicknessBar.getProgress());
                break;
            case R.id.rb_eraser:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeCap(Paint.Cap.ROUND);
                paint.setStrokeJoin(Paint.Join.ROUND);
                paint.setStrokeWidth(mThicknessBar.getProgress());
                PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
                paint.setXfermode(porterDuffXfermode);
                break;
        }
        return paint;
    }
}
