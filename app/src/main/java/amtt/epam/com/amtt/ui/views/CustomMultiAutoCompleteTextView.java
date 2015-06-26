package amtt.epam.com.amtt.ui.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.Logger;
import amtt.epam.com.amtt.util.UIUtil;

public class CustomMultiAutoCompleteTextView extends MultiAutoCompleteTextView {

    private final String TAG = this.getClass().getSimpleName();
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	public boolean isContactAddedFromDb = false;
	public boolean isTextAdditionInProgress = false;
	public boolean checkValidation = true;
	public boolean isTextDeletedFromTouch = false;
	private int beforeChangeIndex = 0;
    private int stringLength = 0;
	private String mChangeString = "";
    public static SparseArrayCompat<String> mSelectedItem = new SparseArrayCompat<>();

    public CustomMultiAutoCompleteTextView(Context context) {
		super(context);
		init(context);
	}

	public CustomMultiAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CustomMultiAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public void init(Context context) {
		this.mContext = context;
		mLayoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		this.addTextChangedListener(textWatcher);
		this.setThreshold(0);
		this.setTokenizer(new CustomCommaTokenizer());
        onClickItem(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String component = (String) parent.getItemAtPosition(position);
                mSelectedItem.put(position, component);
                updateQuickContactList();
            }
        });
    }

    private void onClickItem(OnItemClickListener l) {
        this.setOnItemClickListener(l);
    }

    @Override
	protected void replaceText(CharSequence text) {
		checkValidation = false;
		super.replaceText(text);
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence text, int start, int before, int count) {
			String addedString = text.toString();
			if (!isTextAdditionInProgress) {
				if (stringLength < addedString.length()) {
                    if (!TextUtils.isEmpty(addedString.trim())) {
						int startIndex = isContactAddedFromDb ? addedString.length() : CustomMultiAutoCompleteTextView.this.getSelectionEnd();
						startIndex = startIndex < 1 ? 1 : startIndex;
						String charAtStartIndex = Character.toString(addedString.charAt(startIndex - 1));
						if (charAtStartIndex.equals(",")) {
							isTextAdditionInProgress = true;
							addOrCheckSpannable(text, startIndex);
						}
					}
				}
			}
			stringLength = addedString.length();
		}

		@Override
		public void beforeTextChanged(CharSequence text, int start, int count, int after) {
			beforeChangeIndex = CustomMultiAutoCompleteTextView.this.getSelectionStart();
			mChangeString = text.toString();
		}

		@Override
		public void afterTextChanged(Editable text) {
            int afterChangeIndex = CustomMultiAutoCompleteTextView.this.getSelectionEnd();
			if (!isTextDeletedFromTouch && text.toString().length() < mChangeString.length()
                    && !isTextAdditionInProgress) {
				String deletedString = "";
				try {
					deletedString = mChangeString.substring(afterChangeIndex, beforeChangeIndex);
				} catch (Exception e) {
                    Logger.e(TAG, e.getMessage(), e);
				}
				if (deletedString.length() > 0 && deletedString.contains(","))
					deletedString = deletedString.replace(",", "");
				if (!TextUtils.isEmpty(deletedString.trim()))
					deleteFromHashMap(deletedString);
			}
		}
	};

	public void addOrCheckSpannable(CharSequence text, int startIndex) {
		boolean checkSpannable = false;
		String overallString;
		if (text == null) {
			checkSpannable = true;
			text = this.getText();
			startIndex = this.getSelectionEnd();
			startIndex = startIndex < 1 ? 1 : startIndex;
			overallString = this.getText().toString();
			if (TextUtils.isEmpty(overallString.trim()))
				return;
		} else {
			overallString = text.toString();
			startIndex = startIndex - 1;
		}
		int spanEnd = 0;
		for (int i = startIndex - 1; i >= 0; i--) {
			Character character = overallString.charAt(i);
			if (character == ',') {
				spanEnd = i;
				break;
			}
		}
		SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
		int cursorCurrentPoint = this.getSelectionEnd();
		boolean addedFromFirst = cursorCurrentPoint < overallString.length();
		ClickableSpan[] clickableSpans = spannableStringBuilder.getSpans(0, addedFromFirst ? spanEnd : spannableStringBuilder.length(), ClickableSpan.class);
		boolean someUnknownChange = false;
		if (clickableSpans.length > 0) {
			int k = 0;
            for (ClickableSpan someSpan : clickableSpans) {
                int end = spannableStringBuilder.getSpanEnd(someSpan);
                if (k < end)
                    k = end;
            }
			spanEnd = k;
			if (spanEnd < overallString.length()) {
				Character character = overallString.charAt(spanEnd);
				if (character == ',') {
					spanEnd += 1;
				} else {
					spannableStringBuilder.insert(spanEnd, ",");
					spanEnd += 1;
					startIndex += 1;
					someUnknownChange = true;
				}
			}
		}
		if (startIndex > -1 && spanEnd > -1) {
			if (checkSpannable) {
				ClickableSpan[] span = someUnknownChange ? spannableStringBuilder.getSpans(spanEnd - 1, startIndex - 1, ClickableSpan.class)
                        : spannableStringBuilder.getSpans(spanEnd, startIndex, ClickableSpan.class);
                if ((span.length <= 0) && (startIndex > spanEnd)) {
                    spannableStringBuilder.replace(spanEnd, startIndex, "");
                }
                return;
			} else {
				if ((Math.abs(spanEnd - 1 - startIndex) > 1)) {
					String userInputString = someUnknownChange
                            ? overallString.substring(spanEnd - 1, startIndex - 1)
                            : overallString.substring(spanEnd, startIndex);
					String trimString = userInputString.trim();
					if (trimString.length() == 0) {
						spannableStringBuilder.replace(spanEnd, startIndex + 1, "");
					} else {
						if (userInputString.charAt(userInputString.length() - 1) == ','
                                && spanEnd - 1 >= 0
                                && startIndex - 1 >= 0)
							userInputString = overallString.substring(spanEnd - 1, startIndex - 1);
                        if (checkValidation) {
                            spannableStringBuilder.replace(spanEnd, startIndex + 1, "");
                        } else {
                            BitmapDrawable bmpDrawable = getBitmapFromText(userInputString);
							bmpDrawable.setBounds(0, 0, bmpDrawable.getIntrinsicWidth(), bmpDrawable.getIntrinsicHeight());
							spannableStringBuilder.setSpan(new ImageSpan(bmpDrawable), spanEnd, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							this.setMovementMethod(LinkMovementMethod.getInstance());
							ClickableSpan clickSpan = new ClickableSpan() {

								@Override
								public void onClick(View view) {
									deleteString();
								}

							};
							spannableStringBuilder.setSpan(clickSpan, spanEnd, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
					}
				}
			}
			setSpannableText(spannableStringBuilder);
		}
	}

	public void setSpannableText(final Spannable spannable) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				CustomMultiAutoCompleteTextView.this.setText(spannable);
				CustomMultiAutoCompleteTextView.this.setSelection(CustomMultiAutoCompleteTextView.this.getText().toString().length());
				resetFlags();
			}
		}, 20);
	}

	private void deleteString() {
		int[] startEnd = getSelectionStartAndEnd();
		int i = startEnd[0];
		int j = startEnd[1];
		isTextDeletedFromTouch = true;
		isTextAdditionInProgress = true;
		final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.getText());
		String deletedSubString = spannableStringBuilder.subSequence(Math.min(i, j), Math.max(i, j)).toString();
		deleteFromHashMap(deletedSubString);
		boolean hasCommaAtLast = true;
		try {
			spannableStringBuilder.subSequence(Math.min(i, j + 1), Math.max(i, j + 1));
		} catch (Exception e) {
			hasCommaAtLast = false;
		}
		spannableStringBuilder.replace(Math.min(i, hasCommaAtLast ? j + 1 : j), Math.max(i, hasCommaAtLast ? j + 1 : j), "");
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				CustomMultiAutoCompleteTextView.this.setText(spannableStringBuilder);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						isTextAdditionInProgress = false;
						stringLength = CustomMultiAutoCompleteTextView.this.getText().toString().length();
						isTextDeletedFromTouch = false;
						CustomMultiAutoCompleteTextView.this.setMovementMethod(LinkMovementMethod.getInstance());
					}
				}, 50);
			}
		}, 10);
	}

	private BitmapDrawable getBitmapFromText(String message) {
		@SuppressLint("InflateParams")
        TextView textView = (TextView) mLayoutInflater.inflate(R.layout.textview, null);
		textView.setText(message);
		textView.setTextSize((int) UIUtil.spToPixels(mContext, 16));
		textView.setHeight((int) UIUtil.dipToPixels(mContext, 48));
        return (BitmapDrawable) extractBitmapFromTextView(textView);
	}

	public void resetFlags() {
		isContactAddedFromDb = false;
		isTextAdditionInProgress = false;
		checkValidation = true;
	}

	private void deleteFromHashMap(String subString) {
		SparseArrayCompat<String> selectedContactClone = mSelectedItem.clone();
		for (int i = 0; i<selectedContactClone.size(); i++){
			if (subString.equals(selectedContactClone.get(i))) {
				mSelectedItem.remove(selectedContactClone.keyAt(i));
			}
		}
		updateQuickContactList();
	}

	public void updateQuickContactList() {
        ArrayList<String> listItems = ((ComponentPickerAdapter) this.getAdapter()).getComponentList();
		ArrayList<String> componentList = getContacts(listItems);
		((ComponentPickerAdapter) this.getAdapter()).setComponentList(componentList);
	}

	private int[] getSelectionStartAndEnd() {
		int[] startEnd = new int[2];
		startEnd[0] = this.getSelectionStart() < 0 ? 0 : this.getSelectionStart();
		startEnd[1] = this.getSelectionEnd() < 0 ? 0 : this.getSelectionEnd();
		return startEnd;
	}

	public class CustomCommaTokenizer extends CommaTokenizer {
		@NonNull
        @Override
		public CharSequence terminateToken(@NonNull CharSequence text) {
			CharSequence charSequence = super.terminateToken(text);
			return charSequence.subSequence(0, charSequence.length() - 1);
		}
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
		addOrCheckSpannable(null, 0);
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

    public static ArrayList<String> getContacts(ArrayList<String> sorted) {
        ArrayList<String> components = new ArrayList<>();
        try {
            for (int i = 0; i < sorted.size(); i++) {
                String component = sorted.get(i);
                if (mSelectedItem.indexOfValue(component) >= 0)
                    components.add(component);
            }
            Collections.sort(components, new Comparator<String>() {
                @Override
                public int compare(String object1, String object2) {
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
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(viewBmp);
    }

    public SparseArrayCompat<String> getSelectedItems(){
        return mSelectedItem;
    }

    public void setSelectedItems(ArrayList<String> items){
        if (items != null) {
            if (items.size() != 0) {
                for (int i = 0; i < items.size(); i++) {
                    mSelectedItem.put(i, items.get(i));
                    updateQuickContactList();
                }
            }
        }
    }
}