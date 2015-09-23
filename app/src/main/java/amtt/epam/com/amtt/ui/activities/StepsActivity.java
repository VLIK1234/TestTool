package amtt.epam.com.amtt.ui.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.StepsAdapter;
import amtt.epam.com.amtt.bo.ticket.Step;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.database.util.ContentFromDatabase;
import amtt.epam.com.amtt.database.util.LocalContent;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.Logger;
import amtt.epam.com.amtt.util.UIUtil;

/**
 @author Ivan_Bakach
 @version on 10.06.2015
 */

public class StepsActivity extends BaseActivity implements StepsAdapter.ViewHolder.ClickListener {

    private static final String TAG = StepsActivity.class.getSimpleName();
    private static final int SPAN_COUNT = 3;
    private TextView emptyList;
    private StepsAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        emptyList = (TextView) findViewById(android.R.id.empty);
        recyclerView = (RecyclerView) findViewById(R.id.list_step);
        recyclerView.setLayoutManager(getLayoutManger());
        ArrayList<Step> listStep = new ArrayList<>();
        StepsAdapter recyclerAdapter = new StepsAdapter(listStep, StepsActivity.this);
        recyclerView.setAdapter(recyclerAdapter);
        ContentFromDatabase.getAllSteps(new Callback<List<Step>>() {
            @Override
            public void onLoadStart() {}

            @Override
            public void onLoadExecuted(final List<Step> steps) {
                if (steps != null) {
                    mAdapter = new StepsAdapter((ArrayList<Step>) steps, StepsActivity.this);
                    recyclerView.setAdapter(mAdapter);
                    if (steps.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        emptyList.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onLoadError(Exception e) {
                Logger.e(TAG, e.getMessage(), e);
            }
        });
    }

    private RecyclerView.LayoutManager getLayoutManger() {
        if (UIUtil.getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            return new LinearLayoutManager(getBaseContext());
        } else {
            return new GridLayoutManager(getBaseContext(), SPAN_COUNT);
        }
    }

    @Override
    public void onItemRemove(int position) {
        Step step = mAdapter.removeItem(position);
        LocalContent.removeStep(step);
        if (mAdapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemShow(int position) {
        Intent paintActivityIntent = new Intent(StepsActivity.this, PaintActivity.class);
        paintActivityIntent.putExtra(PaintActivity.KEY_STEP_ID, mAdapter.getStepId(position));
        startActivity(paintActivityIntent);
    }

}
