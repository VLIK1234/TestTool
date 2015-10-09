package amtt.epam.com.amtt.topbutton.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.UIUtil;

/**
 @author Ivan_Bakach
 @version on 11.05.2015
 */

@SuppressLint("ViewConstructor")
public class TopUnitView extends LinearLayout {

    private final amtt.epam.com.amtt.topbutton.view.OnTouchListener mTouchAction;
    private final int mBackgroundIconId;
    private CardView mCardView;

    private String mTitle;
    private TextView mTextView;

    public TopUnitView(Context context, String title, int backgroundIconId, amtt.epam.com.amtt.topbutton.view.OnTouchListener touchAction) {
        super(context);
        this.mTouchAction = touchAction;
        mBackgroundIconId = backgroundIconId;
        setOrientation(HORIZONTAL);
        setMargin((int) getResources().getDimension(R.dimen.margin_buttons_bar), 0, 0, 0);
        addView(getButton());
        mTitle = title;
        addCardView(context, mTitle);
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                addCardWithOrientationCheck();
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                addCardWithOrientationCheck();
            }
        });
    }

    private void addCardView(Context context, String title) {
        mCardView = new CardView(context);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mCardView.setLayoutParams(params);
        mCardView.setUseCompatPadding(true);
        mCardView.setRadius((int) getResources().getDimension(R.dimen.card_corner_radius));
        mTextView = getTextView(title);
        mCardView.addView(mTextView);
        mCardView.setCardElevation((int) getResources().getDimension(R.dimen.card_elevation));
        addView(mCardView);
    }

    private void addCardWithOrientationCheck() {
        if (UIUtil.getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            if (mCardView.getParent() == null) {
                addView(mCardView);
            }
        } else {
            removeView(mCardView);
        }
    }

    private void setMargin(int left, int right, int top, int bottom) {
        LinearLayout.LayoutParams params = new LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(left, top, right, bottom);
        this.setLayoutParams(params);
    }

    private Button getButton() {
        Button button = new Button(getContext());
        UIUtil.setBackgroundCompat(button, getResources().getDrawable(mBackgroundIconId));
        button.setClickable(false);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.button_small_size),
                (int) getResources().getDimension(R.dimen.button_small_size)));
        return button;
    }

    private TextView getTextView(String title) {
        TextView textView = new TextView(getContext());
        textView.setText(title);
        textView.setTextColor(getResources().getColor(R.color.black));
        return textView;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                mTouchAction.onTouch();
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setTitle(String title) {
        mTitle = title;
        mTextView.setText(mTitle);
    }

    public String getTitle() {
        return mTitle;
    }
}
