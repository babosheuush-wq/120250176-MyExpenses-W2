package com.example.myexpenses;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myexpenses.data.CategoryTotal;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<CategoryTotal> {

    public CategoryAdapter(Context context, List<CategoryTotal> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);
        }

        CategoryTotal item = getItem(position);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvTotal = convertView.findViewById(R.id.tvTotal);

        tvName.setText(item.name);
        // مثلا 85 / 100
        tvTotal.setText(MainActivity.format(item.total) + " / " + MainActivity.format(item.budget));

        // اذا المجموع تجاوز الميزانية يصير الرقم احمر
        if (item.total > item.budget) {
            tvTotal.setTextColor(Color.RED);
        } else {
            tvTotal.setTextColor(Color.DKGRAY);
        }

        return convertView;
    }
}
