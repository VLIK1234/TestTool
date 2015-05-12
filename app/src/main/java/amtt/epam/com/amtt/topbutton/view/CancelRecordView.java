package amtt.epam.com.amtt.topbutton.view;

import android.content.Context;
import android.widget.Toast;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 12.05.2015.
 */
public class CancelRecordView extends TopUnitView {
    public CancelRecordView(Context context) {
        super(context, context.getString(R.string.label_cancel_record));
    }

    @Override
    protected void onTouchAction() {
        TopButtonView.setStartRecord(false);
        Toast.makeText(getContext(), getContext().getString(R.string.label_cancel_record), Toast.LENGTH_LONG).show();
    }
}
