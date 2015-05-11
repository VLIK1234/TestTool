package amtt.epam.com.amtt.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 11.05.2015.
 */
public class RecordButton extends Button{

    private String mTitle;

    public RecordButton(Context context) {
        super(context);
        setText("Record");
        setBackgroundCompat(this, getResources().getDrawable(R.drawable.background_circle));
        setTitle("Record");
        setWidth((int)getResources().getDimension(R.dimen.button_small_size));
        setHeight((int)getResources().getDimension(R.dimen.button_small_size));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                TopButtonView.setStartRecord(true);
                Toast.makeText(getContext(),"Record " + TopButtonView.getStartRecord(),Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onTouchEvent(event);
    }

    public void setTitle(String title) {
        mTitle = title;
        android.widget.TextView label = getLabelView();
        if (label != null) {
            label.setText(title);
        }
    }

    android.widget.TextView getLabelView() {
        return (android.widget.TextView) getTag(R.id.button_label);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setBackgroundCompat(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
