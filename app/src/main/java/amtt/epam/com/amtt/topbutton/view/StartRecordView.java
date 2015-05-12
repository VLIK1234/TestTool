package amtt.epam.com.amtt.topbutton.view;

import android.content.Context;
import android.widget.Toast;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 12.05.2015.
 */
public class StartRecordView extends TopUnitView {
    public StartRecordView(Context context) {
        super(context, context.getString(R.string.label_start_record));
    }

    @Override
    protected void onTouchAction() {
        TopButtonView.setStartRecord(true);
        Toast.makeText(getContext(), getContext().getString(R.string.label_start_record), Toast.LENGTH_LONG).show();
    }
}
