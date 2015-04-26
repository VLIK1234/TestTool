package amtt.epam.com.amtt.view;

import amtt.epam.com.amtt.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;

import android.support.v4.view.GravityCompat;

import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

/**
 * Created by Iryna_Monchanka on 4/25/2015.
 */
public class EditText extends FrameLayout {

    private boolean mLabelEnable;
    private int mSupportMode;
    private int mAutoCompleteMode;

    public static final int SUPPORT_MODE_NONE 					= 0;
    public static final int SUPPORT_MODE_HELPER 				= 1;
    public static final int SUPPORT_MODE_HELPER_WITH_ERROR 		= 2;
    public static final int SUPPORT_MODE_CHAR_COUNTER 			= 3;

    public static final int AUTOCOMPLETE_MODE_NONE 				= 0;
    public static final int AUTOCOMPLETE_MODE_SINGLE 			= 1;
    public static final int AUTOCOMPLETE_MODE_MULTI      		= 2;

    private ColorStateList mDividerColors;
    private ColorStateList mDividerErrorColors;
    private boolean mDividerCompoundPadding;

    private ColorStateList mSupportColors;
    private ColorStateList mSupportErrorColors;
    private int mSupportMaxChars;
    private CharSequence mSupportHelper;
    private CharSequence mSupportError;

    private int mLabelInAnimId;
    private int mLabelOutAnimId;

    protected LabelView mLabelView;
    protected android.widget.EditText mInputView;
    protected LabelView mSupportView;
    private DividerDrawable mDivider;

    private TextView.OnSelectionChangedListener mOnSelectionChangedListener;


    public EditText(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        applyStyle(context, attrs, defStyleAttr, defStyleRes);
    }

    public void applyStyle(int resId){
        applyStyle(getContext(), null, 0, resId);
    }


    private void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        CharSequence text = mInputView == null ? null : mInputView.getText();
        CharSequence supportHelper = getHelper();
        CharSequence supportError = getError();
        removeAllViews();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditText, defStyleAttr, defStyleRes);

        mLabelEnable = a.getBoolean(R.styleable.EditText_et_labelEnable, false);
        mSupportMode = a.getInteger(R.styleable.EditText_et_supportMode, SUPPORT_MODE_NONE);
        mAutoCompleteMode = a.getInteger(R.styleable.EditText_et_autoCompleteMode, AUTOCOMPLETE_MODE_NONE);

        switch (mAutoCompleteMode){
            case AUTOCOMPLETE_MODE_SINGLE:
                mInputView = new InternalAutoCompleteTextView(context, attrs, defStyleAttr);
                break;
            case AUTOCOMPLETE_MODE_MULTI:
                mInputView = new InternalMultiAutoCompleteTextView(context, attrs, defStyleAttr);
                break;
            default:
                mInputView = new InternalEditText(context, attrs, defStyleAttr);
                break;
        }

        int inputId = a.getResourceId(R.styleable.EditText_et_inputId, 0);
        mInputView.setId(inputId != 0 ? inputId : ViewUtil.generateViewId());
        mInputView.setVisibility(View.VISIBLE);
        mInputView.setFocusableInTouchMode(true);
        mDividerColors = a.getColorStateList(R.styleable.EditText_et_dividerColor);
        mDividerErrorColors = a.getColorStateList(R.styleable.EditText_et_dividerErrorColor);
        if(mDividerColors == null){
            int[][] states = new int[][]{
                new int[]{-android.R.attr.state_focused},
                new int[]{android.R.attr.state_focused, android.R.attr.state_enabled},
            };
            int[] colors = new int[]{
                ThemeUtil.colorControlNormal(context, 0xFF000000),
                ThemeUtil.colorControlActivated(context, 0xFF000000),
            };

            mDividerColors = new ColorStateList(states, colors);
        }

        if(mDividerErrorColors == null)
            mDividerErrorColors = ColorStateList.valueOf(ThemeUtil.colorAccent(context, 0xFFFF0000));

        int dividerHeight = a.getDimensionPixelOffset(R.styleable.EditText_et_dividerHeight, 0);
        int dividerPadding = a.getDimensionPixelOffset(R.styleable.EditText_et_dividerPadding, 0);
        int dividerAnimDuration = a.getInteger(R.styleable.EditText_et_dividerAnimDuration, context.getResources().getInteger(android.R.integer.config_shortAnimTime));
        mDividerCompoundPadding = a.getBoolean(R.styleable.EditText_et_dividerCompoundPadding, true);
        mInputView.setPadding(0, 0, 0, dividerPadding + dividerHeight);

        mDivider = new DividerDrawable(dividerHeight, mDividerCompoundPadding ? mInputView.getTotalPaddingLeft() : 0, mDividerCompoundPadding ? mInputView.getTotalPaddingRight() : 0, mDividerColors, dividerAnimDuration);
        mDivider.setInEditMode(isInEditMode());
        mDivider.setAnimEnable(false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mInputView.setBackground(mDivider);
        else
            mInputView.setBackgroundDrawable(mDivider);
        mDivider.setAnimEnable(true);

        if(text != null)
            mInputView.setText(text);
        mInputView.addTextChangedListener(new InputTextWatcher());
        addView(mInputView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if(mLabelEnable){
            mLabelView = new LabelView(context);
            mLabelView.setGravity(GravityCompat.START);
            mLabelView.setSingleLine(true);
            int labelPadding = a.getDimensionPixelOffset(R.styleable.EditText_et_labelPadding, 0);
            int labelTextSize = a.getDimensionPixelSize(R.styleable.EditText_et_labelTextSize, 0);
            ColorStateList labelTextColor = a.getColorStateList(R.styleable.EditText_et_labelTextColor);
            int labelTextAppearance = a.getResourceId(R.styleable.EditText_et_labelTextAppearance, 0);
            int labelEllipsize = a.getInteger(R.styleable.EditText_et_labelEllipsize, 0);
            mLabelInAnimId = a.getResourceId(R.styleable.EditText_et_labelInAnim, 0);
            mLabelOutAnimId = a.getResourceId(R.styleable.EditText_et_labelOutAnim, 0);

            mLabelView.setPadding(mDivider.getPaddingLeft(), 0, mDivider.getPaddingRight(), labelPadding);
            if(labelTextAppearance > 0)
                mLabelView.setTextAppearance(context, labelTextAppearance);
            if(labelTextSize > 0)
                mLabelView.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize);
            if(labelTextColor != null)
                mLabelView.setTextColor(labelTextColor);

            switch (labelEllipsize) {
                case 1:
                    mLabelView.setEllipsize(TruncateAt.START);
                    break;
                case 2:
                    mLabelView.setEllipsize(TruncateAt.MIDDLE);
                    break;
                case 3:
                    mLabelView.setEllipsize(TruncateAt.END);
                    break;
                case 4:
                    mLabelView.setEllipsize(TruncateAt.MARQUEE);
                    break;
                default:
                    mLabelView.setEllipsize(TruncateAt.END);
                    break;
            }
            addView(mLabelView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        if(mSupportMode != SUPPORT_MODE_NONE){
            mSupportView = new LabelView(context);
            int supportPadding = a.getDimensionPixelOffset(R.styleable.EditText_et_supportPadding, 0);
            int supportTextSize = a.getDimensionPixelSize(R.styleable.EditText_et_supportTextSize, 0);
            mSupportColors  = a.getColorStateList(R.styleable.EditText_et_supportTextColor);
            mSupportErrorColors = a.getColorStateList(R.styleable.EditText_et_supportTextErrorColor);
            int supportTextAppearance = a.getResourceId(R.styleable.EditText_et_supportTextAppearance, 0);
            int supportEllipsize = a.getInteger(R.styleable.EditText_et_supportEllipsize, 0);
            int supportMaxLines = a.getInteger(R.styleable.EditText_et_supportMaxLines, 0);
            int supportLines = a.getInteger(R.styleable.EditText_et_supportLines, 0);
            boolean supportSingleLine = a.getBoolean(R.styleable.EditText_et_supportSingleLine, false);

            mSupportView.setPadding(mDivider.getPaddingLeft(), supportPadding, mDivider.getPaddingRight(), 0);
            mSupportView.setTextSize(TypedValue.COMPLEX_UNIT_PX, supportTextSize);
            mSupportView.setTextColor(mSupportColors);
            if(supportTextAppearance > 0)
                mSupportView.setTextAppearance(context, supportTextAppearance);
            mSupportView.setSingleLine(supportSingleLine);
            if(supportMaxLines > 0)
                mSupportView.setMaxLines(supportMaxLines);
            if(supportLines > 0)
                mSupportView.setLines(supportLines);

            switch (supportEllipsize) {
                case 1:
                    mSupportView.setEllipsize(TruncateAt.START);
                    break;
                case 2:
                    mSupportView.setEllipsize(TruncateAt.MIDDLE);
                    break;
                case 3:
                    mSupportView.setEllipsize(TruncateAt.END);
                    break;
                case 4:
                    mSupportView.setEllipsize(TruncateAt.MARQUEE);
                    break;
                default:
                    mSupportView.setEllipsize(TruncateAt.END);
                    break;
            }

            switch (mSupportMode) {
                case SUPPORT_MODE_CHAR_COUNTER:
                    mSupportMaxChars = a.getInteger(R.styleable.EditText_et_supportMaxChars, 0);
                    mSupportView.setGravity(GravityCompat.END);
                    updateCharCounter(mInputView.getText().length());
                    break;
                case SUPPORT_MODE_HELPER:
                case SUPPORT_MODE_HELPER_WITH_ERROR:
                    mSupportView.setGravity(GravityCompat.START);
                    mSupportHelper = ThemeUtil.getString(a, R.styleable.EditText_et_helper, supportHelper);
                    setError(ThemeUtil.getString(a, R.styleable.EditText_et_error, supportError));
                    break;
            }
            addView(mSupportView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        a.recycle();

        if(mLabelEnable){
            mLabelView.setText(mInputView.getHint());
            mLabelView.setVisibility(TextUtils.isEmpty(mInputView.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
        }
    }

    private class LabelView extends android.widget.TextView{

        public LabelView(Context context) {
            super(context);
        }

        @Override
        protected int[] onCreateDrawableState(int extraSpace) {
            return mInputView.getDrawableState();
        }

    }

}
