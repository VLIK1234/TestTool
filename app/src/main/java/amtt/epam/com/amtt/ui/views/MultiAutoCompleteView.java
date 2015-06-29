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
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.util.Logger;
import amtt.epam.com.amtt.util.UIUtil;

public class MultiAutoCompleteView extends MultiAutoCompleteTextView {

    private final String TAG = this.getClass().getSimpleName();
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	public boolean mIsContactAddedFromDb = false;
	public boolean mIsTextAdditionInProgress = false;
	public boolean mCheckValidation = true;
	public boolean mIsTextDeletedFromTouch = false;
	private int mBeforeChangeIndex = 0;
    private int mStringLength = 0;
	private String mChangeString = "";
    public static ArrayList<String> mSelectedItem = new ArrayList<>();

    public MultiAutoCompleteView(Context context) {
		super(context);
		init(context);
	}

	public MultiAutoCompleteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MultiAutoCompleteView(Context context, AttributeSet attrs, int defStyle) {
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
                String item = (String) parent.getItemAtPosition(position);
                mSelectedItem.add(item);
                updateQuickContactList();
            }
        });
    }

    private void onClickItem(OnItemClickListener l) {
        this.setOnItemClickListener(l);
    }

    @Override
	protected void replaceText(CharSequence text) {
		mCheckValidation = false;
		super.replaceText(text);
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence text, int start, int before, int count) {
			String addedString = text.toString();
			if (!mIsTextAdditionInProgress) {
				if (mStringLength < addedString.length()) {
                    if (!TextUtils.isEmpty(addedString.trim())) {
						int startIndex = mIsContactAddedFromDb ? addedString.length() : MultiAutoCompleteView.this.getSelectionEnd();
						startIndex = startIndex < 1 ? 1 : startIndex;
						String charAtStartIndex = Character.toString(addedString.charAt(startIndex - 1));
						if (charAtStartIndex.equals(",")) {
							mIsTextAdditionInProgress = true;
							addOrCheckSpannable(text, startIndex);
						}
					}
				}
			}
			mStringLength = addedString.length();
		}

		@Override
		public void beforeTextChanged(CharSequence text, int start, int count, int after) {
			mBeforeChangeIndex = MultiAutoCompleteView.this.getSelectionStart();
			mChangeString = text.toString();
		}

		@Override
		public void afterTextChanged(Editable text) {
            int afterChangeIndex = MultiAutoCompleteView.this.getSelectionEnd();
			if (!mIsTextDeletedFromTouch && text.toString().length() < mChangeString.length()
                    && !mIsTextAdditionInProgress) {
				String deletedString = "";
				try {
					deletedString = mChangeString.substring(afterChangeIndex, mBeforeChangeIndex);
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
                        if (mCheckValidation) {
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
				MultiAutoCompleteView.this.setText(spannable);
				MultiAutoCompleteView.this.setSelection(MultiAutoCompleteView.this.getText().toString().length());
				resetFlags();
			}
		}, 20);
	}

	private void deleteString() {
		int[] startEnd = getSelectionStartAndEnd();
		int i = startEnd[0];
		int j = startEnd[1];
		mIsTextDeletedFromTouch = true;
		mIsTextAdditionInProgress = true;
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
				MultiAutoCompleteView.this.setText(spannableStringBuilder);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						mIsTextAdditionInProgress = false;
						mStringLength = MultiAutoCompleteView.this.getText().toString().length();
						mIsTextDeletedFromTouch = false;
						MultiAutoCompleteView.this.setMovementMethod(LinkMovementMethod.getInstance());
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
		mIsContactAddedFromDb = false;
		mIsTextAdditionInProgress = false;
		mCheckValidation = true;
	}

	private void deleteFromHashMap(String subString) {
		ArrayList<String> selectedContactClone =  this.mSelectedItem;
		for (int i = 0; i < selectedContactClone.size(); i++){
			if (subString.equals(selectedContactClone.get(i))) {
				mSelectedItem.remove(selectedContactClone.get(i));
			}
		}
		updateQuickContactList();
	}

	public void updateQuickContactList() {
        ArrayList<String> listItems = ((ComponentPickerAdapter) this.getAdapter()).getAllItems();
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
                if (mSelectedItem.indexOf(component) < 0)
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

    public ArrayList<String> getSelectedItems(){
        return mSelectedItem;
    }

    public void setSelectedItems(ArrayList<String> items){
        if (items != null) {
            if (items.size() != 0) {
                String text = "";
                for (int i = 0; i < items.size(); i++) {
                    text = items.get(i);
                    this.setText(text);
                    mSelectedItem.add(items.get(i));
                    updateQuickContactList();
                }
            }
        }
    }

}