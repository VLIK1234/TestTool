package amtt.epam.com.amtt.app;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.http.MimeType;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.UIUtil;
import amtt.epam.com.amtt.view.PaintView;
import amtt.epam.com.amtt.view.PaletteItem;

/**
 * Created by Ivan_Bakach on 09.06.2015.
 */
public class PaintActivity extends BaseActivity {

    public static final String FILE_PATH = "filePath";

    private String mScreenshotPath;

    private TextView mTextPreview;
    private PaintView mPaintView;

    private LayoutInflater mLayoutInflater;
    private int mLastBrushColor;
    private AlertDialog mPaletteDialog;
    private List<PaletteItem> mPaletteItems;
    private int mLastSelectedPaletteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

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
                if (mPaletteDialog == null) {
                    initPaletteDialog();
                }
                mPaletteDialog.show();
                return true;
            case R.id.action_erase:
                mPaintView.setEraseMode(true);
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

    private void initPaintView() {
        mPaintView = (PaintView) findViewById(R.id.paint_view);
        mPaintView.setBrushColor(getResources().getColor(R.color.red_paint));
    }

    private void initPaletteDialog() {
        final View view = mLayoutInflater.inflate(R.layout.dialog_palette, null);
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                if (mPaintView.isEraseMode()) {
                    return;
                }

                PaletteItem paletteItem = (PaletteItem) v.findViewById(mLastSelectedPaletteItem);
                paletteItem.setSelected();
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });

        mPaletteItems = new ArrayList<PaletteItem>() {{
            add((PaletteItem) view.findViewById(mLastSelectedPaletteItem = R.id.pi_red));
            add((PaletteItem) view.findViewById(R.id.pi_blue));
            add((PaletteItem) view.findViewById(R.id.pi_green));
            add((PaletteItem) view.findViewById(R.id.pi_indigo));
            add((PaletteItem) view.findViewById(R.id.pi_orange));
            add((PaletteItem) view.findViewById(R.id.pi_violet));
            add((PaletteItem) view.findViewById(R.id.pi_white));
            add((PaletteItem) view.findViewById(R.id.pi_yellow));
        }};
        for (PaletteItem paletteItem : mPaletteItems) {
            paletteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (PaletteItem paletteItem : mPaletteItems) {
                        if (paletteItem.isItemSelected()) {
                            paletteItem.setUnselected();
                            break;
                        }
                    }

                    PaletteItem paletteItem = (PaletteItem) v;
                    paletteItem.setSelected();
                    int newBrushColor = paletteItem.getColor();
                    if (newBrushColor != mLastBrushColor) {
                        mPaintView.setBrushColor(newBrushColor);
                        mLastBrushColor = newBrushColor;
                        mLastSelectedPaletteItem = paletteItem.getId();
                    }

                    if (mPaintView.isEraseMode()) {
                        mPaintView.setEraseMode(false);
                    }
                    mPaletteDialog.dismiss();
                }
            });

//            ImageButton eraserButton = view.findViewById(R.id)

            mPaletteDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.title_choose_color)
                    .setView(view)
                    .create();
        }
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
