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
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.util.UIUtil;

/**
 * Created by Ivan_Bakach on 10.06.2015.
 */
public class StepsActivity extends AppCompatActivity implements StepsAdapter.ViewHolder.ClickListener{

    public static final int SPAN_COUNT = 3;
    private TextView emptyList;
    private StepsAdapter adapter;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        emptyList = (TextView) findViewById(android.R.id.empty);
        recyclerView = (RecyclerView) findViewById(R.id.list_step);
        recyclerView.setLayoutManager(getLayoutManger());
        ArrayList<Step> listStep = new ArrayList<>();
        StepsAdapter recyclerAdapter = new StepsAdapter(listStep, StepsActivity.this);
        recyclerView.setAdapter(recyclerAdapter);

        DbObjectManager.INSTANCE.getAll(new Step(), new IResult<List<DatabaseEntity>>() {
            @Override
            public void onResult(final List<DatabaseEntity> result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result!=null) {
                            adapter = new StepsAdapter((ArrayList) result, StepsActivity.this);
                            recyclerView.setAdapter(adapter);
                            if (result.size() == 0) {
                                recyclerView.setVisibility(View.GONE);
                                emptyList.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TopButtonService.sendActionChangeVisibilityTopbutton(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TopButtonService.sendActionChangeVisibilityTopbutton(true);
    }

    public RecyclerView.LayoutManager getLayoutManger(){
        if (UIUtil.getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            return new LinearLayoutManager(getBaseContext());
        }else{
            return new GridLayoutManager(getBaseContext(), SPAN_COUNT);
        }
    }

    @Override
    public void onItemRemove(int position) {
        adapter.removeItem(position);
        if (adapter.getItemCount() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemShow(int position) {
        Intent preview = new Intent(StepsActivity.this, PreviewActivity.class);
        preview.putExtra(PreviewActivity.FILE_PATH, adapter.getScreenPath(position));
        startActivity(preview);
    }
}
