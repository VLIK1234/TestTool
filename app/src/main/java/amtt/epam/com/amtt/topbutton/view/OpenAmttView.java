package amtt.epam.com.amtt.topbutton.view;

import android.content.Context;
import android.widget.Toast;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 12.05.2015.
 */
public class OpenAmttView extends TopUnitView {
    public OpenAmttView(Context context) {
        super(context, context.getString(R.string.label_open_amtt));
    }

    @Override
    protected void onTouchAction() {
        Toast.makeText(getContext(), getContext().getString(R.string.label_open_amtt) + " don't have logic yet.", Toast.LENGTH_LONG).show();
    }
}
