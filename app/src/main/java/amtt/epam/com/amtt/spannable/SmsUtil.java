package amtt.epam.com.amtt.spannable;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class SmsUtil {

	public static HashMap<String, String> selectedContact = new HashMap<String, String>();


	public static ArrayList<Component> getContacts(Context context,
			boolean addAllConatct) {
		ArrayList<Component> components = new ArrayList<Component>();
		try {

			Cursor cursor = context.getContentResolver()
					.query(Phone.CONTENT_URI,
							new String[] { Phone._ID, Phone.DISPLAY_NAME,
									Phone.NUMBER }, null, null, null);
			cursor.moveToFirst();
			while (cursor.moveToNext()) {
				Component component = new Component();
				component.contactName = cursor.getString(cursor
						.getColumnIndex(Phone.DISPLAY_NAME));
				component.num = cursor.getString(cursor
						.getColumnIndex(Phone.NUMBER));
				if (selectedContact.get(component.num) == null)
					components.add(component);
			}

			Collections.sort(components, new Comparator<Component>() {

				@Override
				public int compare(Component object1, Component object2) {
					// TODO Auto-generated method stub
					return object1.contactName
							.compareToIgnoreCase(object2.contactName);
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
