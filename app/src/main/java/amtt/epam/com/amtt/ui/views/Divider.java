package amtt.epam.com.amtt.ui.views;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

import amtt.epam.com.amtt.util.ViewUtil;

/**
 * Created on 4/27/2015.
 * based on  https://github.com/rey5137/material/blob/master/lib/src/main/java/com/rey/material/drawable/DividerDrawable.java
 */

public class Divider extends Drawable implements Animatable {

    private boolean mRunning = false;
    private long mStartTime;
    private float mAnimProgress;
    private final int mAnimDuration;

    private final Paint mPaint;
    private ColorStateList mColorStateList;
    private final int mHeight;
    private int mPrevColor;
    private int mCurColor;

    private boolean mEnable = true;
    private PathEffect mPathEffect;
    private final Path mPath;

    private boolean mInEditMode = false;
    private boolean mAnimEnable = true;

    private final int mPaddingLeft;
    private final int mPaddingRight;

    public Divider(int height, int paddingLeft, int paddingRight, ColorStateList colorStateList, int animDuration){
        mHeight = height;
        mPaddingLeft = paddingLeft;
        mPaddingRight = paddingRight;
        mAnimDuration = animDuration;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mHeight);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mPath = new Path();

        mAnimEnable = false;
        setColor(colorStateList);
        mAnimEnable = true;
    }

    public int getPaddingLeft(){
        return mPaddingLeft;
    }

    public int getPaddingRight(){
        return mPaddingRight;
    }

    public void setInEditMode(boolean b){
        mInEditMode = b;
    }

    public void setAnimEnable(boolean b){
        mAnimEnable = b;
    }

    private void setColor(ColorStateList colorStateList){
        mColorStateList = colorStateList;
        onStateChange(getState());
    }

    private PathEffect getPathEffect(){
        if(mPathEffect == null)
            mPathEffect = new DashPathEffect(new float[]{0.2f, mHeight * 2}, 0f);

        return mPathEffect;
    }

    @Override
    public void draw(Canvas canvas) {
        if(mHeight == 0)
            return;

        Rect bounds = getBounds();
        float y = bounds.bottom - mHeight / 2;

        if(!isRunning()){
            mPath.reset();
            mPath.moveTo(bounds.left + mPaddingLeft, y);
            mPath.lineTo(bounds.right - mPaddingRight, y);
            mPaint.setPathEffect(mEnable ? null : getPathEffect());
            mPaint.setColor(mCurColor);
            canvas.drawPath(mPath, mPaint);
        }
        else{
            float centerX = (bounds.right + bounds.left - mPaddingRight + mPaddingLeft) / 2f;
            float start = centerX * (1f - mAnimProgress) + (bounds.left + mPaddingLeft) * mAnimProgress;
            float end = centerX * (1f - mAnimProgress) + (bounds.right + mPaddingRight) * mAnimProgress;

            mPaint.setPathEffect(null);

            if(mAnimProgress < 1f){
                mPaint.setColor(mPrevColor);
                mPath.reset();
                mPath.moveTo(bounds.left + mPaddingLeft, y);
                mPath.lineTo(start, y);
                mPath.moveTo(bounds.right - mPaddingRight, y);
                mPath.lineTo(end, y);
                canvas.drawPath(mPath, mPaint);
            }

            mPaint.setColor(mCurColor);
            mPath.reset();
            mPath.moveTo(start, y);
            mPath.lineTo(end, y);
            canvas.drawPath(mPath, mPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    protected boolean onStateChange(int[] state) {
        mEnable = ViewUtil.hasState(state, android.R.attr.state_enabled);
        int color = mColorStateList.getColorForState(state, mCurColor);

        if(mCurColor != color){
            if(!mInEditMode && mAnimEnable && mEnable){
                mPrevColor = isRunning() ? mPrevColor : mCurColor;
                mCurColor = color;
                start();
            }
            else{
                mPrevColor = color;
                mCurColor = color;
            }
            return true;
        }
        else if(!isRunning())
            mPrevColor = color;

        return false;
    }

    private void resetAnimation(){
        mStartTime = SystemClock.uptimeMillis();
        mAnimProgress = 0f;
    }

    @Override
    public void start() {
        resetAnimation();
        scheduleSelf(mUpdater, SystemClock.uptimeMillis() + ViewUtil.FRAME_DURATION);
        invalidateSelf();
    }

    @Override
    public void stop() {
        mRunning = false;
        unscheduleSelf(mUpdater);
        invalidateSelf();
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    @Override
    public void scheduleSelf(Runnable what, long when) {
        mRunning = true;
        super.scheduleSelf(what, when);
    }

    private final Runnable mUpdater = new Runnable() {

        @Override
        public void run() {
            update();
        }

    };

    private void update(){
        long curTime = SystemClock.uptimeMillis();
        mAnimProgress = Math.min(1f, (float)(curTime - mStartTime) / mAnimDuration);

        if(mAnimProgress == 1f)
            mRunning = false;

        if(isRunning())
            scheduleSelf(mUpdater, SystemClock.uptimeMillis() + ViewUtil.FRAME_DURATION);

        invalidateSelf();
    }
}
