package amtt.epam.com.amtt.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;

/**
 * @author Iryna Monchanka
 * @version on 03.06.2015
 */

public class AskExitActivity  extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_exit);

        Button buttonReturn = (Button) findViewById(R.id.btn_return);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopButtonService.sendActionShowButton();
                finish();
            }
        });

        Button buttonClose = (Button) findViewById(R.id.btn_close_amtt);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopButtonService.close(getBaseContext());
                finish();
            }
        });

    }

}
