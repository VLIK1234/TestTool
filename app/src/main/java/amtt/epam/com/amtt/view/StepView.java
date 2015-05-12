package amtt.epam.com.amtt.view;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.app.StepsActivity;

/**
 * Created by Ivan_Bakach on 12.05.2015.
 */
public class StepView extends TopUnitView {
    public StepView(Context context) {
        super(context, context.getString(R.string.label_step_view));
    }

    @Override
    protected void onTouchAction() {
        Toast.makeText(getContext(), getContext().getString(R.string.label_step_view), Toast.LENGTH_LONG).show();
        Intent intentStep = new Intent(getContext(), StepsActivity.class);
        intentStep.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().getApplicationContext().startActivity(intentStep);
    }
}
