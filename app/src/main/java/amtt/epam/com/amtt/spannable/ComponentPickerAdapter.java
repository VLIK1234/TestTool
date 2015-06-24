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

public class ComponentPickerAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> componentList, cloneComponentList;
    private LayoutInflater layoutInflater;

    @SuppressWarnings("unchecked")
    public ComponentPickerAdapter(Context context, int textViewResourceId, ArrayList<String> componentList) {
        super(context, textViewResourceId);
        this.componentList = componentList;
        this.cloneComponentList = (ArrayList<String>) this.componentList.clone();
        layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return componentList.size();
    }

    @Override
    public String getItem(int position) {
        return componentList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.spinner_dropdown_item, null);
            holder = new Holder();
            holder.name = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        String component = getItem(position);
        holder.name.setText(component);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                if (results.values != null) {
                    componentList = (ArrayList<String>) results.values;
                    notifyDataSetChanged();
                }

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String sortValue;
                if (constraint == null) {
                    sortValue = "";
                } else {
                    sortValue = constraint.toString().toLowerCase();
                }
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(sortValue.trim())) {
                    ArrayList<String> sortedComponentList = new ArrayList<>();
                    for (String contact : cloneComponentList) {
                        if (contact.toLowerCase().contains(sortValue))
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
                return ((String) resultValue);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public void setComponentList(ArrayList<String> componentList) {
        // this isn't the efficient method
        // need to improvise on this
        this.componentList = componentList;
        this.cloneComponentList = (ArrayList<String>) this.componentList.clone();
        notifyDataSetChanged();
    }

    public static class Holder {
        public TextView name;
    }

}
