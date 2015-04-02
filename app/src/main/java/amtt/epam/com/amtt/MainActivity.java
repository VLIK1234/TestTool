package amtt.epam.com.amtt;

import amtt.epam.com.amtt.asynctask.ShowUserDataTask;
import amtt.epam.com.amtt.bo.issue.createmeta.JMetaResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.util.CreateMetaUtil;
import amtt.epam.com.amtt.callbacks.ShowUserDataCallback;
import amtt.epam.com.amtt.service.TopButtonService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends ActionBarActivity implements ShowUserDataCallback {

    private SharedPreferences sharedPreferences;
    private ArrayList<String> projectsNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        startService(new Intent(this, TopButtonService.class));
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onIssueClick(View view) {
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String username, password, url;
        username = sharedPreferences.getString("username", "");
        password = sharedPreferences.getString("password", "");
        url = sharedPreferences.getString("url", "");
        new ShowUserDataTask(username, password, url, MainActivity.this).execute();


        startActivity(new Intent(this, CreateIssueActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShowUserDataResult(JMetaResponse result) {


        CreateMetaUtil createMetaUtil = new CreateMetaUtil();

        projectsNames = createMetaUtil.getProjectsNames(result);

        Set pNames = new HashSet();
        for (int i = 0; i < projectsNames.size(); i++) {

            pNames.add(projectsNames.get(i));
        }
        sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putStringSet("projectsNames", pNames);

        ed.commit();
    }
}
