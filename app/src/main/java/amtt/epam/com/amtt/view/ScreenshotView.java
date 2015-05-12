package amtt.epam.com.amtt.view;

import android.content.Context;
import android.widget.Toast;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 12.05.2015.
 */
public class ScreenshotView extends TopUnitView {
    public ScreenshotView(Context context) {
        super(context, context.getString(R.string.label_screenshot));
    }

    @Override
    protected void onTouchAction() {
        Toast.makeText(getContext(), getContext().getString(R.string.label_screenshot), Toast.LENGTH_LONG).show();
    }
}
