package com.ternobo.pikano.tools;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ternobo.pikano.RESTobjects.Grade;

import java.util.ArrayList;

public class SpinAdapter extends ArrayAdapter<Grade> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (Grade)
    private ArrayList<Grade> values;

    public SpinAdapter(Context context, int textViewResourceId,
                       ArrayList<Grade> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Grade getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public boolean isEnabled(int position) {
        return position != 0;
    }

    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Grades array) and the current position
        // You can NOW reference each method you has created in your bean object (Grade class)
        label.setText(values.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getName());

        return label;
    }
}
