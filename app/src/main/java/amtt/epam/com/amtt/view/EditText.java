package amtt.epam.com.amtt.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.MultiAutoCompleteTextView;
import java.util.ArrayList;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.ThemeUtil;
import amtt.epam.com.amtt.util.ViewUtil;

/**
 * Created on 4/27/2015.
 * based on https://github.com/rey5137/material/blob/master/lib/src/main/java/com/rey/material/widget/EditText.java
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
	private Divider mDivider;

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
        boolean mDividerCompoundPadding = a.getBoolean(R.styleable.EditText_et_dividerCompoundPadding, true);
        mInputView.setPadding(0, 0, 0, dividerPadding + dividerHeight);

        mDivider = new Divider(dividerHeight, mDividerCompoundPadding ? mInputView.getTotalPaddingLeft() : 0, mDividerCompoundPadding ? mInputView.getTotalPaddingRight() : 0, mDividerColors, dividerAnimDuration);
        mDivider.setInEditMode(isInEditMode());
        mDivider.setAnimEnable(false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            mInputView.setBackground(mDivider);}
        else {
            mInputView.setBackgroundDrawable(mDivider);
        }
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
                    mSupportHelper = ThemeUtil.getCharSequence(a, R.styleable.EditText_et_helper, supportHelper);
                    setError(ThemeUtil.getCharSequence(a, R.styleable.EditText_et_error, supportError));
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

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int tempWidthSpec = widthMode == MeasureSpec.UNSPECIFIED ? widthMeasureSpec : MeasureSpec.makeMeasureSpec(widthSize - getPaddingLeft() - getPaddingRight(), widthMode);
		int tempHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

		int labelWidth = 0;
		int labelHeight = 0;
		int inputWidth;
		int inputHeight;
		int supportWidth = 0;
		int supportHeight = 0;

		if(mLabelView != null){
			mLabelView.measure(tempWidthSpec, tempHeightSpec);
			labelWidth = mLabelView.getMeasuredWidth();
			labelHeight = mLabelView.getMeasuredHeight();
		}

		mInputView.measure(tempWidthSpec, tempHeightSpec);
		inputWidth = mInputView.getMeasuredWidth();
		inputHeight = mInputView.getMeasuredHeight();

		if(mSupportView != null){
			mSupportView.measure(tempWidthSpec, tempHeightSpec);
			supportWidth = mSupportView.getMeasuredWidth();
			supportHeight = mSupportView.getMeasuredHeight();
		}

		int width = 0;
		int height = 0;

		switch (widthMode) {
			case MeasureSpec.UNSPECIFIED:
				width = Math.max(labelWidth, Math.max(inputWidth, supportWidth)) + getPaddingLeft() + getPaddingRight();
				break;
			case MeasureSpec.AT_MOST:
				width = Math.min(widthSize, Math.max(labelWidth, Math.max(inputWidth, supportWidth)) + getPaddingLeft() + getPaddingRight());
				break;
			case MeasureSpec.EXACTLY:
				width = widthSize;
				break;
		}

		switch (heightMode) {
			case MeasureSpec.UNSPECIFIED:
				height = labelHeight + inputHeight + supportHeight + getPaddingTop() + getPaddingBottom();
				break;
			case MeasureSpec.AT_MOST:
				height = Math.min(heightSize, labelHeight + inputHeight + supportHeight + getPaddingTop() + getPaddingBottom());
				break;
			case MeasureSpec.EXACTLY:
				height = heightSize;
				break;
		}

		setMeasuredDimension(width, height);

		tempWidthSpec = MeasureSpec.makeMeasureSpec(width - getPaddingLeft() - getPaddingRight(),  MeasureSpec.EXACTLY);
		if(mLabelView != null)
			mLabelView.measure(tempWidthSpec, tempHeightSpec);

		mInputView.measure(tempWidthSpec, tempHeightSpec);

		if(mSupportView != null)
			mSupportView.measure(tempWidthSpec, tempHeightSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = getPaddingLeft();
		int childRight = r - l - getPaddingRight();
		int childTop = getPaddingTop();
		int childBottom = b - t - getPaddingBottom();

		if(mLabelView != null){
			mLabelView.layout(childLeft, childTop, childRight, childTop + mLabelView.getMeasuredHeight());
			childTop += mLabelView.getMeasuredHeight();
		}

		if(mSupportView != null){
			mSupportView.layout(childLeft, childBottom - mSupportView.getMeasuredHeight(), childRight, childBottom);
			childBottom -= mSupportView.getMeasuredHeight();
		}

		mInputView.layout(childLeft, childTop, childRight, childBottom);
	}

	public void setHelper(CharSequence helper){
		mSupportHelper = helper;
		setError(mSupportError);
	}

	public CharSequence getHelper(){
		return mSupportHelper;
	}

	public void setError(CharSequence error){
		mSupportError = error;

		if(mSupportMode != SUPPORT_MODE_HELPER && mSupportMode != SUPPORT_MODE_HELPER_WITH_ERROR)
			return;

		if(mSupportError != null){
			mSupportView.setTextColor(mSupportErrorColors);
			mDivider.setColor(mDividerErrorColors);
			mSupportView.setText(mSupportMode == SUPPORT_MODE_HELPER ? mSupportError : TextUtils.concat(mSupportHelper, ", ", mSupportError));
		}
		else{
			mSupportView.setTextColor(mSupportColors);
			mDivider.setColor(mDividerColors);
			mSupportView.setText(mSupportHelper);
		}
	}

	public CharSequence getError(){
		return mSupportError;
	}

	public void clearError(){
		setError(null);
	}

	private void updateCharCounter(int count){
		if(count == 0){
			mSupportView.setTextColor(mSupportColors);
			mDivider.setColor(mDividerColors);
			mSupportView.setText(null);
		}
		else{
			if(mSupportMaxChars > 0){
				mSupportView.setTextColor(count > mSupportMaxChars ? mSupportErrorColors : mSupportColors);
				mDivider.setColor(count > mSupportMaxChars ? mDividerErrorColors : mDividerColors);
				mSupportView.setText(count + " / " + mSupportMaxChars);
			}
    		else
    			mSupportView.setText(String.valueOf(count));
		}
	}

    protected CharSequence convertSelectionToString(Object selectedItem) {
        switch (mAutoCompleteMode){
            case AUTOCOMPLETE_MODE_SINGLE:
                return ((InternalAutoCompleteTextView)mInputView).superConvertSelectionToString(selectedItem);
            case AUTOCOMPLETE_MODE_MULTI:
                return ((InternalMultiAutoCompleteTextView)mInputView).superConvertSelectionToString(selectedItem);
            default:
                return null;
        }
    }

    protected void performFiltering(CharSequence text, int keyCode) {
        switch (mAutoCompleteMode){
            case AUTOCOMPLETE_MODE_SINGLE:
                ((InternalAutoCompleteTextView)mInputView).superPerformFiltering(text, keyCode);
                break;
            case AUTOCOMPLETE_MODE_MULTI:
                ((InternalMultiAutoCompleteTextView)mInputView).superPerformFiltering(text, keyCode);
                break;
        }
    }

    protected void replaceText(CharSequence text) {
        switch (mAutoCompleteMode){
            case AUTOCOMPLETE_MODE_SINGLE:
                ((InternalAutoCompleteTextView)mInputView).superReplaceText(text);
                break;
            case AUTOCOMPLETE_MODE_MULTI:
                ((InternalMultiAutoCompleteTextView)mInputView).superReplaceText(text);
                break;
        }
    }

    protected Filter getFilter() {
        switch (mAutoCompleteMode){
            case AUTOCOMPLETE_MODE_SINGLE:
                return ((InternalAutoCompleteTextView)mInputView).superGetFilter();
            case AUTOCOMPLETE_MODE_MULTI:
                return ((InternalMultiAutoCompleteTextView)mInputView).superGetFilter();
            default:
                return null;
        }
    }

    protected void performFiltering(CharSequence text, int start, int end, int keyCode) {
        if(mAutoCompleteMode == AUTOCOMPLETE_MODE_MULTI)
            ((InternalMultiAutoCompleteTextView)mInputView).superPerformFiltering(text, start, end, keyCode);
    }

    @Override
    public void setEnabled(boolean enabled){
        mInputView.setEnabled(enabled);
    }

	public Editable getText (){
		return mInputView.getText();
	}

	public void setText (CharSequence text, TextView.BufferType type){
		mInputView.setText(text, type);
	}

	public void cancelLongPress (){
		mInputView.cancelLongPress();
	}

	@Override
	public void computeScroll (){
		mInputView.computeScroll();
	}

	@Override
	public void debug (int depth){
		mInputView.debug(depth);
	}

	@Override
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void findViewsWithText (@NonNull ArrayList<View> outViews, CharSequence searched, int flags){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			mInputView.findViewsWithText(outViews, searched, flags);
	}

	@Override
	public int getBaseline (){
		return mInputView.getBaseline();
	}

	@Override
	public void getFocusedRect (@NonNull Rect r){
		mInputView.getFocusedRect(r);
	}

	public final Layout getLayout (){
		return mInputView.getLayout();
	}

	public float getTextSize (){
		return mInputView.getTextSize();
	}

	@Override
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public boolean hasOverlappingRendering (){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && mInputView.hasOverlappingRendering();
    }

	public void onCommitCompletion (CompletionInfo text){
        if(mAutoCompleteMode == AUTOCOMPLETE_MODE_NONE)
            ((InternalEditText)mInputView).superOnCommitCompletion(text);
        else if(mAutoCompleteMode == AUTOCOMPLETE_MODE_SINGLE)
            ((InternalAutoCompleteTextView)mInputView).superOnCommitCompletion(text);
        else
            ((InternalMultiAutoCompleteTextView)mInputView).superOnCommitCompletion(text);
	}

	public void onCommitCorrection (CorrectionInfo info){
        if(mAutoCompleteMode == AUTOCOMPLETE_MODE_NONE)
            ((InternalEditText)mInputView).superOnCommitCorrection(info);
        else if(mAutoCompleteMode == AUTOCOMPLETE_MODE_SINGLE)
            ((InternalAutoCompleteTextView)mInputView).superOnCommitCorrection(info);
        else
            ((InternalMultiAutoCompleteTextView)mInputView).superOnCommitCorrection(info);
	}

	@Override
	public InputConnection onCreateInputConnection (EditorInfo outAttrs){
        if(mAutoCompleteMode == AUTOCOMPLETE_MODE_NONE)
            return ((InternalEditText)mInputView).superOnCreateInputConnection(outAttrs);
        else if(mAutoCompleteMode == AUTOCOMPLETE_MODE_SINGLE)
            return ((InternalAutoCompleteTextView)mInputView).superOnCreateInputConnection(outAttrs);
        else
            return ((InternalMultiAutoCompleteTextView)mInputView).superOnCreateInputConnection(outAttrs);
	}

	public void onEditorAction (int actionCode){
        if(mAutoCompleteMode == AUTOCOMPLETE_MODE_NONE)
            ((InternalEditText)mInputView).superOnEditorAction(actionCode);
        else if(mAutoCompleteMode == AUTOCOMPLETE_MODE_SINGLE)
            ((InternalAutoCompleteTextView)mInputView).superOnEditorAction(actionCode);
        else
            ((InternalMultiAutoCompleteTextView)mInputView).superOnEditorAction(actionCode);
	}

	@Override
	public boolean onKeyDown (int keyCode, @NonNull KeyEvent event){
        if(mAutoCompleteMode == AUTOCOMPLETE_MODE_NONE)
            return ((InternalEditText)mInputView).superOnKeyDown(keyCode, event);
        else if(mAutoCompleteMode == AUTOCOMPLETE_MODE_SINGLE)
            return ((InternalAutoCompleteTextView)mInputView).superOnKeyDown(keyCode, event);
        else
            return ((InternalMultiAutoCompleteTextView)mInputView).superOnKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyMultiple (int keyCode, int repeatCount, KeyEvent event){
        if(mAutoCompleteMode == AUTOCOMPLETE_MODE_NONE)
            return ((InternalEditText)mInputView).superOnKeyMultiple(keyCode, repeatCount, event);
        else if(mAutoCompleteMode == AUTOCOMPLETE_MODE_SINGLE)
            return ((InternalAutoCompleteTextView)mInputView).superOnKeyMultiple(keyCode, repeatCount, event);
        else
            return ((InternalMultiAutoCompleteTextView)mInputView).superOnKeyMultiple(keyCode, repeatCount, event);
	}

	@Override
	public boolean onKeyPreIme (int keyCode, KeyEvent event){
        if(mAutoCompleteMode == AUTOCOMPLETE_MODE_NONE)
            return ((InternalEditText)mInputView).superOnKeyPreIme(keyCode, event);
        else if(mAutoCompleteMode == AUTOCOMPLETE_MODE_SINGLE)
            return ((InternalAutoCompleteTextView)mInputView).superOnKeyPreIme(keyCode, event);
        else
            return ((InternalMultiAutoCompleteTextView)mInputView).superOnKeyPreIme(keyCode, event);
	}

	@Override
	public boolean onKeyShortcut (int keyCode, KeyEvent event){
        if(mAutoCompleteMode == AUTOCOMPLETE_MODE_NONE)
            return ((InternalEditText)mInputView).superOnKeyShortcut(keyCode, event);
        else if(mAutoCompleteMode == AUTOCOMPLETE_MODE_SINGLE)
            return ((InternalAutoCompleteTextView)mInputView).superOnKeyShortcut(keyCode, event);
        else
            return ((InternalMultiAutoCompleteTextView)mInputView).superOnKeyShortcut(keyCode, event);
	}

	@Override
	public boolean onKeyUp (int keyCode, KeyEvent event){
        if(mAutoCompleteMode == AUTOCOMPLETE_MODE_NONE)
            return ((InternalEditText)mInputView).superOnKeyUp(keyCode, event);
        else if(mAutoCompleteMode == AUTOCOMPLETE_MODE_SINGLE)
            return ((InternalAutoCompleteTextView)mInputView).superOnKeyUp(keyCode, event);
        else
            return ((InternalMultiAutoCompleteTextView)mInputView).superOnKeyUp(keyCode, event);
	}

    protected void onSelectionChanged(int selStart, int selEnd) {
        if(mInputView == null)
            return;

        if(mAutoCompleteMode == AUTOCOMPLETE_MODE_NONE)
            ((InternalEditText)mInputView).superOnSelectionChanged(selStart, selEnd);
        else if(mAutoCompleteMode == AUTOCOMPLETE_MODE_SINGLE)
            ((InternalAutoCompleteTextView)mInputView).superOnSelectionChanged(selStart, selEnd);
        else
            ((InternalMultiAutoCompleteTextView)mInputView).superOnSelectionChanged(selStart, selEnd);

        if(mOnSelectionChangedListener != null)
            mOnSelectionChangedListener.onSelectionChanged(this, selStart, selEnd);
    }

	@Override
    public void setOnKeyListener(OnKeyListener l) {
    	mInputView.setOnKeyListener(l);
    }

	@Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
    	mInputView.setOnFocusChangeListener(l);
    }

	@Override
	public void setSelected (boolean selected){
		mInputView.setSelected(selected);
	}

	public final void setText (int resid){
		mInputView.setText(resid);
	}

	public final void setText (CharSequence text){
		mInputView.setText(text);
	}

    public void onFilterComplete(int count) {
        if(mAutoCompleteMode == AUTOCOMPLETE_MODE_SINGLE)
            ((InternalAutoCompleteTextView)mInputView).superOnFilterComplete(count);
        else if(mAutoCompleteMode == AUTOCOMPLETE_MODE_MULTI)
            ((InternalMultiAutoCompleteTextView)mInputView).superOnFilterComplete(count);
    }

	/* Inner class */

	private class InputTextWatcher implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
        	if(!mLabelEnable)
        		return;

        	int count = s.length();

            if(count == 0){
            	if(mLabelView.getVisibility() == View.VISIBLE){
            		if(mLabelOutAnimId > 0){
                		Animation anim = AnimationUtils.loadAnimation(getContext(), mLabelOutAnimId);
                		anim.setAnimationListener(new Animation.AnimationListener() {

    						@Override
    						public void onAnimationStart(Animation animation) {}

    						@Override
    						public void onAnimationRepeat(Animation animation) {}

    						@Override
    						public void onAnimationEnd(Animation animation) {
    							mLabelView.setVisibility(View.INVISIBLE);
    						}

    					});
                		mLabelView.startAnimation(anim);
                	}
                	else
                		mLabelView.setVisibility(View.INVISIBLE);
            	}

            	if(mSupportMode == SUPPORT_MODE_CHAR_COUNTER)
            		updateCharCounter(count);
            } else{
            	if(mLabelView.getVisibility() == View.INVISIBLE){
            		if(mLabelInAnimId > 0){
                		Animation anim = AnimationUtils.loadAnimation(getContext(), mLabelInAnimId);
                		anim.setAnimationListener(new Animation.AnimationListener() {

    						@Override
    						public void onAnimationStart(Animation animation) {
    							mLabelView.setVisibility(View.VISIBLE);
    						}

    						@Override
    						public void onAnimationRepeat(Animation animation) {}

    						@Override
    						public void onAnimationEnd(Animation animation) {}
    					});
                		mLabelView.startAnimation(anim);
                	}
                	else
                		mLabelView.setVisibility(View.VISIBLE);
            	}

            	if(mSupportMode == SUPPORT_MODE_CHAR_COUNTER)
            		updateCharCounter(count);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }

	private class LabelView extends TextView{

		public LabelView(Context context) {
			super(context);
		}

		@Override
		protected int[] onCreateDrawableState(int extraSpace) {
			return mInputView.getDrawableState();
		}

	}

	private class InternalEditText extends android.widget.EditText{

		public InternalEditText(Context context) {
			super(context);
		}

		public InternalEditText(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public InternalEditText(Context context, AttributeSet attrs, int defStyleAttr) {
			super(context, attrs, defStyleAttr);
		}

		@Override
		public void refreshDrawableState() {
			super.refreshDrawableState();

			if(mLabelView != null)
				mLabelView.refreshDrawableState();

			if(mSupportView != null)
				mSupportView.refreshDrawableState();
		}

        @Override
        public void onCommitCompletion(CompletionInfo text) {
            EditText.this.onCommitCompletion(text);
        }

        @Override
        public void onCommitCorrection(@NonNull CorrectionInfo info) {
            EditText.this.onCommitCorrection(info);
        }

        @Override
        public InputConnection onCreateInputConnection(@NonNull EditorInfo outAttrs) {
            return EditText.this.onCreateInputConnection(outAttrs);
        }

        @Override
        public void onEditorAction(int actionCode) {
            EditText.this.onEditorAction(actionCode);
        }

        @Override
        public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
            return EditText.this.onKeyDown(keyCode, event);
        }

        @Override
        public boolean onKeyMultiple(int keyCode, int repeatCount, @NonNull KeyEvent event) {
            return EditText.this.onKeyMultiple(keyCode, repeatCount, event);
        }

        @Override
        public boolean onKeyPreIme(int keyCode, @NonNull KeyEvent event) {
            return EditText.this.onKeyPreIme(keyCode, event);
        }

        @Override
        public boolean onKeyShortcut(int keyCode, @NonNull KeyEvent event) {
            return EditText.this.onKeyShortcut(keyCode, event);
        }

        @Override
        public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
            return EditText.this.onKeyUp(keyCode, event);
        }

        @Override
        protected void onSelectionChanged(int selStart, int selEnd) {
            EditText.this.onSelectionChanged(selStart, selEnd);
        }

        void superOnCommitCompletion(CompletionInfo text) {
            super.onCommitCompletion(text);
        }

        void superOnCommitCorrection(CorrectionInfo info) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                super.onCommitCorrection(info);
        }

        InputConnection superOnCreateInputConnection(EditorInfo outAttrs) {
            return super.onCreateInputConnection(outAttrs);
        }

        void superOnEditorAction(int actionCode) {
            super.onEditorAction(actionCode);
        }

        boolean superOnKeyDown(int keyCode, KeyEvent event) {
            return super.onKeyDown(keyCode, event);
        }

        boolean superOnKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
            return super.onKeyMultiple(keyCode, repeatCount, event);
        }

        boolean superOnKeyPreIme(int keyCode, KeyEvent event) {
            return super.onKeyPreIme(keyCode, event);
        }

        boolean superOnKeyShortcut(int keyCode, KeyEvent event) {
            return super.onKeyShortcut(keyCode, event);
        }

        boolean superOnKeyUp(int keyCode, KeyEvent event) {
            return super.onKeyUp(keyCode, event);
        }

        void superOnSelectionChanged(int selStart, int selEnd) {
            super.onSelectionChanged(selStart, selEnd);
        }
    }

    private class InternalAutoCompleteTextView extends AutoCompleteTextView{

        public InternalAutoCompleteTextView(Context context) {
            super(context);
        }

        public InternalAutoCompleteTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public InternalAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public void refreshDrawableState() {
            super.refreshDrawableState();

            if(mLabelView != null)
                mLabelView.refreshDrawableState();

            if(mSupportView != null)
                mSupportView.refreshDrawableState();
        }

        @Override
        public void onCommitCompletion(@NonNull CompletionInfo text) {
            EditText.this.onCommitCompletion(text);
        }

        @Override
        public void onCommitCorrection(@NonNull CorrectionInfo info) {
            EditText.this.onCommitCorrection(info);
        }

        @Override
        public InputConnection onCreateInputConnection(@NonNull EditorInfo outAttrs) {
            return EditText.this.onCreateInputConnection(outAttrs);
        }

        @Override
        public void onEditorAction(int actionCode) {
            EditText.this.onEditorAction(actionCode);
        }

        @Override
        public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
            return EditText.this.onKeyDown(keyCode, event);
        }

        @Override
        public boolean onKeyMultiple(int keyCode, int repeatCount, @NonNull KeyEvent event) {
            return EditText.this.onKeyMultiple(keyCode, repeatCount, event);
        }

        @Override
        public boolean onKeyPreIme(int keyCode, @NonNull KeyEvent event) {
            return EditText.this.onKeyPreIme(keyCode, event);
        }

        @Override
        public boolean onKeyShortcut(int keyCode, @NonNull KeyEvent event) {
            return EditText.this.onKeyShortcut(keyCode, event);
        }

        @Override
        public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
            return EditText.this.onKeyUp(keyCode, event);
        }

        @Override
        protected void onSelectionChanged(int selStart, int selEnd) {
            EditText.this.onSelectionChanged(selStart, selEnd);
        }

        @Override
        protected CharSequence convertSelectionToString(Object selectedItem) {
            return EditText.this.convertSelectionToString(selectedItem);
        }

        @Override
        protected void performFiltering(CharSequence text, int keyCode) {
            EditText.this.performFiltering(text, keyCode);
        }

        @Override
        protected void replaceText(CharSequence text) {
            EditText.this.replaceText(text);
        }

        @Override
        protected Filter getFilter() {
            return EditText.this.getFilter();
        }

        @Override
        public void onFilterComplete(int count) {
            EditText.this.onFilterComplete(count);
        }

        void superOnCommitCompletion(CompletionInfo text) {
            super.onCommitCompletion(text);
        }

        void superOnCommitCorrection(CorrectionInfo info) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                super.onCommitCorrection(info);
        }

        InputConnection superOnCreateInputConnection(EditorInfo outAttrs) {
            return super.onCreateInputConnection(outAttrs);
        }

        void superOnEditorAction(int actionCode) {
            super.onEditorAction(actionCode);
        }

        boolean superOnKeyDown(int keyCode, @NonNull KeyEvent event) {
            return super.onKeyDown(keyCode, event);
        }

        boolean superOnKeyMultiple(int keyCode, int repeatCount, @NonNull KeyEvent event) {
            return super.onKeyMultiple(keyCode, repeatCount, event);
        }

        boolean superOnKeyPreIme(int keyCode, @NonNull KeyEvent event) {
            return super.onKeyPreIme(keyCode, event);
        }

        boolean superOnKeyShortcut(int keyCode, @NonNull KeyEvent event) {
            return super.onKeyShortcut(keyCode, event);
        }

        boolean superOnKeyUp(int keyCode, @NonNull KeyEvent event) {
            return super.onKeyUp(keyCode, event);
        }

        void superOnFilterComplete(int count) {
            super.onFilterComplete(count);
        }

        CharSequence superConvertSelectionToString(Object selectedItem) {
            return super.convertSelectionToString(selectedItem);
        }

        void superPerformFiltering(CharSequence text, int keyCode) {
            super.performFiltering(text, keyCode);
        }

        void superReplaceText(CharSequence text) {
            super.replaceText(text);
        }

        Filter superGetFilter() {
            return super.getFilter();
        }

        void superOnSelectionChanged(int selStart, int selEnd) {
            super.onSelectionChanged(selStart, selEnd);
        }
    }

    private class InternalMultiAutoCompleteTextView extends MultiAutoCompleteTextView{

        public InternalMultiAutoCompleteTextView(Context context) {
            super(context);
        }

        public InternalMultiAutoCompleteTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public InternalMultiAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public void refreshDrawableState() {
            super.refreshDrawableState();

            if(mLabelView != null)
                mLabelView.refreshDrawableState();

            if(mSupportView != null)
                mSupportView.refreshDrawableState();
        }

        @Override
        public void onCommitCompletion(@NonNull CompletionInfo text) {
            EditText.this.onCommitCompletion(text);
        }

        @Override
        public void onCommitCorrection(@NonNull CorrectionInfo info) {
            EditText.this.onCommitCorrection(info);
        }

        @Override
        public InputConnection onCreateInputConnection(@NonNull EditorInfo outAttrs) {
            return EditText.this.onCreateInputConnection(outAttrs);
        }

        @Override
        public void onEditorAction(int actionCode) {
            EditText.this.onEditorAction(actionCode);
        }

        @Override
        public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
            return EditText.this.onKeyDown(keyCode, event);
        }

        @Override
        public boolean onKeyMultiple(int keyCode, int repeatCount, @NonNull KeyEvent event) {
            return EditText.this.onKeyMultiple(keyCode, repeatCount, event);
        }

        @Override
        public boolean onKeyPreIme(int keyCode, @NonNull KeyEvent event) {
            return EditText.this.onKeyPreIme(keyCode, event);
        }

        @Override
        public boolean onKeyShortcut(int keyCode, @NonNull KeyEvent event) {
            return EditText.this.onKeyShortcut(keyCode, event);
        }

        @Override
        public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
            return EditText.this.onKeyUp(keyCode, event);
        }

        @Override
        protected void onSelectionChanged(int selStart, int selEnd) {
            EditText.this.onSelectionChanged(selStart, selEnd);
        }

        @Override
        public void onFilterComplete(int count) {
            EditText.this.onFilterComplete(count);
        }

        @Override
        protected CharSequence convertSelectionToString(Object selectedItem) {
            return EditText.this.convertSelectionToString(selectedItem);
        }

        @Override
        protected void performFiltering(@NonNull CharSequence text, int keyCode) {
            EditText.this.performFiltering(text, keyCode);
        }

        @Override
        protected void replaceText(CharSequence text) {
            EditText.this.replaceText(text);
        }

        @Override
        protected Filter getFilter() {
            return EditText.this.getFilter();
        }

        @Override
        protected void performFiltering(@NonNull CharSequence text, int start, int end, int keyCode){
            EditText.this.performFiltering(text, start, end, keyCode);
        }

        void superOnCommitCompletion(CompletionInfo text) {
            super.onCommitCompletion(text);
        }

        void superOnCommitCorrection(CorrectionInfo info) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                super.onCommitCorrection(info);
        }

        InputConnection superOnCreateInputConnection(EditorInfo outAttrs) {
            return super.onCreateInputConnection(outAttrs);
        }

        void superOnEditorAction(int actionCode) {
            super.onEditorAction(actionCode);
        }

        boolean superOnKeyDown(int keyCode, KeyEvent event) {
            return super.onKeyDown(keyCode, event);
        }

        boolean superOnKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
            return super.onKeyMultiple(keyCode, repeatCount, event);
        }

        boolean superOnKeyPreIme(int keyCode, KeyEvent event) {
            return super.onKeyPreIme(keyCode, event);
        }

        boolean superOnKeyShortcut(int keyCode, KeyEvent event) {
            return super.onKeyShortcut(keyCode, event);
        }

        boolean superOnKeyUp(int keyCode, KeyEvent event) {
            return super.onKeyUp(keyCode, event);
        }

        void superOnFilterComplete(int count) {
            super.onFilterComplete(count);
        }

        CharSequence superConvertSelectionToString(Object selectedItem) {
            return super.convertSelectionToString(selectedItem);
        }

        void superPerformFiltering(CharSequence text, int keyCode) {
            super.performFiltering(text, keyCode);
        }

        void superReplaceText(CharSequence text) {
            super.replaceText(text);
        }

        Filter superGetFilter() {
            return super.getFilter();
        }

        void superPerformFiltering(CharSequence text, int start, int end, int keyCode){
            super.performFiltering(text, start, end, keyCode);
        }

        void superOnSelectionChanged(int selStart, int selEnd) {
            super.onSelectionChanged(selStart, selEnd);
        }
    }
}
