package amtt.epam.com.amtt.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;

/**
 * @author Iryna Monchanka
 * @version on 7/13/2015
 */

public class DetailActivity extends BaseActivity{

    public static final String TESTCASE_KEY = "testcase_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater factory = LayoutInflater.from(getBaseContext());
    }
}
