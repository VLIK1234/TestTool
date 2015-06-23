package amtt.epam.com.amtt.spannable;

import android.app.Activity;
import android.os.Bundle;

import amtt.epam.com.amtt.R;

public class ComponentActivity extends Activity {
	private CustomMultiAutoCompleteTextView phoneNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_component);
		
		phoneNum = (CustomMultiAutoCompleteTextView)
				findViewById(R.id.editText);
		ComponentPickerAdapter componentPickerAdapter = new ComponentPickerAdapter(this,
				android.R.layout.simple_list_item_1, SmsUtil.getContacts(
					this, false));
		phoneNum.setAdapter(componentPickerAdapter);
	}

}
