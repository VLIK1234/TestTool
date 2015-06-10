package amtt.epam.com.amtt.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.adapter.StepRecyclerAdapter;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbObjectManger;
import amtt.epam.com.amtt.database.object.IResult;

/**
 * Created by Ivan_Bakach on 10.06.2015.
 */
public class StepsRecyclerActivity extends AppCompatActivity implements StepRecyclerAdapter.ViewHolder.ClickListener{

    private TextView emptyList;
    public StepRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_recycler);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        emptyList = (TextView) findViewById(android.R.id.empty);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_step);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new StepRecyclerAdapter(new ArrayList<Step>(), StepsRecyclerActivity.this));
        DbObjectManger.INSTANCE.getAll(new Step(), new IResult<List<DatabaseEntity>>() {
            @Override
            public void onResult(List<DatabaseEntity> result) {
                if (result.size()==0) {
                    emptyList.setVisibility(View.VISIBLE);
                }
                adapter = new StepRecyclerAdapter((ArrayList) result, StepsRecyclerActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void onItemRemove(int position) {
        adapter.removeItem(position);
    }

    @Override
    public void onItemShow(int position) {

    }
}
