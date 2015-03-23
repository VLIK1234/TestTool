package amtt.epam.com.amtt.view;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import amtt.epam.com.amtt.R;

/**
 * Created by Ivan_Bakach on 23.03.2015.
 */
public class TopButtonView extends FrameLayout{

    private final static int NO_VIEW_FLAG = 0;

    private Button button;
    private FrameLayout frameLayout;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private final static String LOG_TAG = "TAG";

    public TopButtonView(Context context, WindowManager windowManager, WindowManager.LayoutParams layoutParams) {
        super(context);
        initComponent();
        this.windowManager = windowManager;
        this.layoutParams = layoutParams;
    }


    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.top_hud, this);
        button = (Button) findViewById(R.id.test_top_button);
        frameLayout = (FrameLayout) findViewById(R.id.top_hud);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item(LOG_TAG);

                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData dragData = new ClipData(LOG_TAG,
                        mimeTypes, item);

                // Instantiates the drag shadow builder.
                DragShadowBuilder myShadow = new DragShadowBuilder(button);

                // Starts the drag
                v.startDrag(dragData,  // the data to be dragged
                        myShadow,  // the drag shadow builder
                        null,      // no need to use local data
                        NO_VIEW_FLAG          // flags (not currently used, set to 0)
                );
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "TOUCH");
            }
        });

        // Create and set the drag event listener for the View
        frameLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int x_cord;
                int y_cord;

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.d(LOG_TAG, "Action is DragEvent.ACTION_DRAG_STARTED");
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(LOG_TAG, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d(LOG_TAG, "Action is DragEvent.ACTION_DRAG_EXITED");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        Log.d(LOG_TAG, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d(LOG_TAG, "Action is DragEvent.ACTION_DRAG_ENDED");
                        y_cord = (int) event.getY();
                        x_cord = (int) event.getX();
                        layoutParams.x = (int) event.getX();
                        layoutParams.y = (int) event.getY();

                        Log.d(LOG_TAG, x_cord+" "+ y_cord);

                        windowManager.updateViewLayout(TopButtonView.this,layoutParams);
                        break;
                    case DragEvent.ACTION_DROP:
                        Log.d(LOG_TAG, "ACTION_DROP event");
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
}
