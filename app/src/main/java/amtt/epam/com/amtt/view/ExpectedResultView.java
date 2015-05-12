package amtt.epam.com.amtt.view;

import android.content.Context;
import android.widget.Toast;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 12.05.2015.
 */
public class ExpectedResultView extends TopUnitView{

    public ExpectedResultView(Context context) {
        super(context, context.getString(R.string.label_expected_result));
    }

    @Override
    protected void onTouchAction() {
        Toast.makeText(getContext(), getContext().getString(R.string.label_expected_result)+" Вова, что здесь должно быть?", Toast.LENGTH_LONG).show();
    }
}
