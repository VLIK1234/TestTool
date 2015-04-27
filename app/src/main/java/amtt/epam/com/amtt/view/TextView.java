package amtt.epam.com.amtt.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Iryna_Monchanka on 4/27/2015.
 */
public class TextView extends android.widget.TextView {

        private RippleManager mRippleManager = new RippleManager();

        public interface OnSelectionChangedListener{
            void onSelectionChanged(View v, int selStart, int selEnd);
        }

        private OnSelectionChangedListener mOnSelectionChangedListener;

        public TextView(Context context) {
            super(context);

            init(context, null, 0, 0);
        }

        public TextView(Context context, AttributeSet attrs) {
            super(context, attrs);

            init(context, attrs, 0, 0);
        }

        public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);

            init(context, attrs, defStyleAttr, 0);
        }

        public TextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr);

            init(context, attrs, defStyleAttr, defStyleRes);
        }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        applyStyle(context, attrs, defStyleAttr, defStyleRes);
    }

    private void applyStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        mRippleManager.onCreate(this, context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setOnClickListener(View.OnClickListener l) {
        if(l == mRippleManager)
            super.setOnClickListener(l);
        else{
            mRippleManager.setOnClickListener(l);
            setOnClickListener(mRippleManager);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        boolean result = super.onTouchEvent(event);
        return  mRippleManager.onTouchEvent(event) || result;
    }
    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);

        if(mOnSelectionChangedListener != null)
            mOnSelectionChangedListener.onSelectionChanged(this, selStart, selEnd);
    }



}


