package amtt.epam.com.amtt.view.popup;

import android.app.ActionBar;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import amtt.epam.com.amtt.R;

/**
 * Created by Artsiom_Kaliaha on 13.04.2015.
 */
public class Popup {

    private final PopupWindow mPopupWindow;
    private TextView mHeaderTextView;
    private TextView mBodyTextView;

    public Popup(View contentView, Drawable background) {
        mPopupWindow = new PopupWindow(contentView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(background);
    }

    public void close() {
        mPopupWindow.dismiss();
    }

    public void setButton(View button, View.OnClickListener onClickListener) {
        button.setOnClickListener(onClickListener);
    }

    public void setHeaderTextView(View textView) {
        mHeaderTextView = (TextView) textView;
    }

    public void setHeaderText(String text) {
        mHeaderTextView.setText(text);
    }


    public void setBodyTextView(View textView) {
        mBodyTextView = (TextView) textView;
    }

    public void setBodyText(String text) {
        mBodyTextView.setText(text);
    }

    public void show(Display display) {
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        mPopupWindow.getContentView().findViewById(R.id.scroll_container).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 2));
        mPopupWindow.showAtLocation(mPopupWindow.getContentView(), Gravity.CENTER, 0, 0);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) (width * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.getContentView().setLayoutParams(layoutParams);
    }

    public View getLayout() {
        return mPopupWindow.getContentView();
    }

}
