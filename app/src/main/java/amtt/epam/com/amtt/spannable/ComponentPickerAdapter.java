package amtt.epam.com.amtt.spannable;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import amtt.epam.com.amtt.R;

public class ComponentPickerAdapter extends ArrayAdapter<Component> implements
		Filterable {

	private ArrayList<Component> componentList, cloneComponentList;
	private LayoutInflater layoutInflater;

	@SuppressWarnings("unchecked")
	public ComponentPickerAdapter(Context context, int textViewResourceId,
								  ArrayList<Component> componentList) {
		super(context, textViewResourceId);
		this.componentList = componentList;
		this.cloneComponentList = (ArrayList<Component>) this.componentList.clone();
		layoutInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {

		return componentList.size();
	}

	@Override
	public Component getItem(int position) {

		return componentList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Holder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.component_list_item,
					null);
			holder = new Holder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.phone = (TextView) convertView.findViewById(R.id.phone);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		Component component = getItem(position);
		holder.name.setText(component.contactName);
		holder.phone.setText(component.num);
		return convertView;
	}

	@Override
	public Filter getFilter() {
		Filter contactFilter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results.values != null) {
					componentList = (ArrayList<Component>) results.values;
					notifyDataSetChanged();
				}

			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				String sortValue = constraint == null ? "" : constraint
						.toString().toLowerCase();
				FilterResults filterResults = new FilterResults();
				if (!TextUtils.isEmpty(sortValue.trim())) {
					ArrayList<Component> sortedComponentList = new ArrayList<Component>();
					for (Component contact : cloneComponentList) {
						if (contact.contactName.toLowerCase().contains(
								sortValue)
								|| contact.num.toLowerCase()
										.contains(sortValue))
							sortedComponentList.add(contact);
					}

					filterResults.values = sortedComponentList;
					filterResults.count = sortedComponentList.size();

				}
				return filterResults;
			}

			@Override
			public CharSequence convertResultToString(Object resultValue) {
			// need to save this to saved contact
				return ((Component) resultValue).contactName;
			}
		};

		return contactFilter;
	}

	@SuppressWarnings("unchecked")
	public void setComponentList(ArrayList<Component> componentList) {
		// this isn't the efficient method
		// need to improvise on this
		this.componentList = componentList;
		this.cloneComponentList = (ArrayList<Component>) this.componentList.clone();
		notifyDataSetChanged();
	}

	public static class Holder {
		public TextView phone, name;
	}

}
