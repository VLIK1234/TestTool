package amtt.epam.com.amtt.topbutton.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import android.widget.TextView;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 11.05.2015.
 */
public abstract class TopUnitView extends LinearLayout{

    private String title;

    public TopUnitView(Context context, String title) {
        super(context);
        this.title = title;
        //Change when will be support landscape orientation
//        if (orientation==VERTICAL||orientation==HORIZONTAL) {
//            setOrientation(orientation);
//        }else{
//            Toast.makeText(context,"Wrong orientation! Set default value = HORIZONTAL",Toast.LENGTH_LONG).show();
//        }
        setOrientation(HORIZONTAL);
        this.setMargin(this, (int) getResources().getDimension(R.dimen.margin_buttons_bar), 0, 0, 0);
        addView(getButton());
        CardView cardView = new CardView(context);
        LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        cardView.setLayoutParams(params);
        cardView.setUseCompatPadding(true);
        cardView.setRadius((int) getResources().getDimension(R.dimen.card_corner_radius));
        cardView.addView(getTextView(this.title));
        cardView.setCardElevation((int)getResources().getDimension(R.dimen.card_elevation));
        addView(cardView);
    }

    private void setMargin(View view, int left, int right, int top, int bottom){
        LinearLayout.LayoutParams params = new LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(left, top, right, bottom);
        view.setLayoutParams(params);
    }

    private void setBackgroundCompat(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
    private Button getButton(){
        Button button = new Button(getContext());
        setBackgroundCompat(button, getResources().getDrawable(R.drawable.background_main_button));
        button.setClickable(false);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                (int) getResources().getDimension(R.dimen.button_small_size),
                (int) getResources().getDimension(R.dimen.button_small_size)));
        return button;
    }

    private TextView getTextView(String title){
        TextView textView = new TextView(getContext());
        textView.setText(title);
        textView.setTextColor(getResources().getColor(R.color.black));
        return textView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
    switch (event.getAction()){
        case MotionEvent.ACTION_DOWN:
            onTouchAction();
            break;
    }
        return super.onTouchEvent(event);
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }

    protected abstract void onTouchAction();
}
