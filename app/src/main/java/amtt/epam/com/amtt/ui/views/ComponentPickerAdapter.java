package amtt.epam.com.amtt.ui.views;

import android.annotation.SuppressLint;
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
import java.util.List;

import amtt.epam.com.amtt.R;

public class ComponentPickerAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> componentList;
    private ArrayList<String> cloneComponentList;
    private LayoutInflater layoutInflater;
    private ArrayList<String> mAllItems;


    public ComponentPickerAdapter(Context context, int textViewResourceId, List<String> componentList) {
        super(context, textViewResourceId);
        this.componentList = (ArrayList<String>) componentList;
        this.cloneComponentList = (ArrayList<String>) this.componentList.clone();
        this.mAllItems = (ArrayList<String>) componentList;
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

    @SuppressLint("InflateParams")
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

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    componentList = (ArrayList<String>) results.values;
                    notifyDataSetChanged();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String sortValue = constraint == null ? "" : constraint.toString().toLowerCase();
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
                return (String) resultValue;
            }
        };
    }

    public void setComponentList(ArrayList<String> componentList) {
        this.componentList = componentList;
        this.cloneComponentList = componentList;
        notifyDataSetChanged();
    }

    public ArrayList<String> getComponentList() {
        return this.componentList;
    }

    public static class Holder {
        public TextView name;
    }

    public ArrayList<String> getAllItems(){
       return this.mAllItems;
    }
}
