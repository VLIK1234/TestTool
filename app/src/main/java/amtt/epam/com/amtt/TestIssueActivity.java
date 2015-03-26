package amtt.epam.com.amtt;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import amtt.epam.com.amtt.bo.CreateIssue;
import io.fabric.sdk.android.Fabric;


public class TestIssueActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_test_issue);
        TextView testIssue = (TextView)findViewById(R.id.test_issue);
        CreateIssue issue = new CreateIssue();
        testIssue.setText(issue.createSimpleIssue());






    }




}
