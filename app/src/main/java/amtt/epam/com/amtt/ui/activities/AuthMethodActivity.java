package amtt.epam.com.amtt.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.ActiveUser;

/**
 * @author IvanBakach
 * @version on 12.10.2015
 */
public class AuthMethodActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_method);
        ImageView backgroundImage = (ImageView) findViewById(R.id.iv_background);
        backgroundImage.setImageDrawable(getResources().getDrawable(R.drawable.background_auth_method));
        Button loginButton = (Button) findViewById(R.id.bt_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(AuthMethodActivity.this, MainActivity.class);
                startActivity(intentLogin);
                finish();
            }
        });
        Button notNowButton = (Button) findViewById(R.id.bt_not_now);
        notNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopButtonService.start(getBaseContext());
                ActiveUser.getInstance().clearActiveUser();
                finish();
            }
        });
    }
}
