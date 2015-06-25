package amtt.epam.com.amtt.spannable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class SmsUtil {

    public static HashMap<Integer, String> selectedContact = new HashMap<>();

	public static ArrayList<String> getContacts(ArrayList<String> sorted) {
		ArrayList<String> components = new ArrayList<>();
		try {
            for (int i = 0; i < sorted.size(); i++) {
				String component = sorted.get(i);
				if (!selectedContact.containsValue(component))
					components.add(component);
			}

			Collections.sort(components, new Comparator<String>() {

				@Override
				public int compare(String object1, String object2) {
					// TODO Auto-generated method stub
					return object1.compareToIgnoreCase(object2);
				}

			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.i("contactLength", String.valueOf(components.size()));
		return components;
	}


	public static Object extractBitmapFromTextView(View view) {

		int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		view.measure(spec, spec);
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		c.translate(-view.getScrollX(), -view.getScrollY());
		view.draw(c);
		view.setDrawingCacheEnabled(true);
		Bitmap cacheBmp = view.getDrawingCache();
		Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
		view.destroyDrawingCache();
		return new BitmapDrawable(viewBmp);

	}

}
