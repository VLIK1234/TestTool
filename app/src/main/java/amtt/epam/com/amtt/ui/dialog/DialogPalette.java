package amtt.epam.com.amtt.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
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
import amtt.epam.com.amtt.ui.views.paintview.PaintView;

/**
 * @author Ivan Bakach
 * @version on 13.09.2015
 */
public class DialogPalette{

    private Paint mPaint;
    private TextView mTextValueThickness;
    private AlertDialog mAlertDialog;
    private SeekBar mThicknessBar;

    public DialogPalette(Context context, RadioGroup.OnCheckedChangeListener checkedChangeListener,
                            SeekBar.OnSeekBarChangeListener seekBarChangeListener,
                            MultilineRadioGroup.OnEntireGroupCheckedChangeListener entireGroupCheckedChangeListener) {
        initPaletteDialog(context, checkedChangeListener, seekBarChangeListener, entireGroupCheckedChangeListener);

    }

    private void initPaletteDialog(Context context, RadioGroup.OnCheckedChangeListener checkedChangeListener,
                                   SeekBar.OnSeekBarChangeListener seekBarChangeListener,
                                   MultilineRadioGroup.OnEntireGroupCheckedChangeListener entireGroupCheckedChangeListener) {
        final View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_palette, null);
        final ImageView previewImage = (ImageView) view.findViewById(R.id.iv_preview);
        mTextValueThickness = (TextView) view.findViewById(R.id.tv_thickness_value);

        mThicknessBar = (SeekBar) view.findViewById(R.id.sb_thickness);
        mThicknessBar.setProgress(PaintView.DEFAULT_BRUSH_THICKNESS);
        mThicknessBar.setMax(PaintView.DEFAULT_OPACITY);
        mThicknessBar.setOnSeekBarChangeListener(seekBarChangeListener);

        mTextValueThickness.setText(Integer.toString(mThicknessBar.getProgress()));

        final MultilineRadioGroup multilineRadioGroup = (MultilineRadioGroup) view.findViewById(R.id.multi_line_radio_group);
        multilineRadioGroup.setOnEntireGroupCheckedListener(entireGroupCheckedChangeListener);

        RadioGroup paintToolsGroup = (RadioGroup) view.findViewById(R.id.rg_paint_tools);
        paintToolsGroup.setOnCheckedChangeListener(checkedChangeListener);
        ((RadioButton) paintToolsGroup.findViewById(R.id.rb_pencil)).setChecked(true);

        previewImage.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final Bitmap cacheCanvasBitmap = Bitmap.createBitmap(previewImage.getMeasuredWidth(),
                        previewImage.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                final Canvas canvas = new Canvas(cacheCanvasBitmap);
                canvas.drawBitmap(cacheCanvasBitmap, 0, 0, null);
                mPaint = new Paint();
                mPaint.setStyle(Paint.Style.FILL);
                if (multilineRadioGroup.getChekedItem() != null) {
                    mPaint.setColor(multilineRadioGroup.getChekedItem().getColor());
                }
//                paint.setTextSize(thicknessBar.getProgress());
                int border = 30;
                mPaint.setStrokeWidth(mThicknessBar.getProgress());
                canvas.drawLine(0, previewImage.getMeasuredHeight() / 2,
                        previewImage.getMeasuredWidth(), previewImage.getMeasuredHeight() / 2, mPaint);
//                canvas.drawText("Text", previewImage.getMeasuredWidth()/4, previewImage.getMeasuredHeight() - border, paint);
                previewImage.setImageBitmap(cacheCanvasBitmap);
                return true;
            }
        });

        mAlertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.title_drawing_customization)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    public void show(){
        mAlertDialog.show();
    }

    public void setThickness(int thickness){
        mTextValueThickness.setText(thickness);
    }
}
