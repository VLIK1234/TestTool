package amtt.epam.com.amtt;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.service.HUD;

/**
 * Created by Ivan_Bakach on 19.03.2015.
 */
public class CoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService( new Intent(this, HUD.class));
    }
}
