package com.example.myexpenses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myexpenses.data.Expense;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends ArrayAdapter<Expense> {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);

    public ExpenseAdapter(Context context, List<Expense> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_expense, parent, false);
        }

        Expense e = getItem(position);
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);
        TextView tvDate = convertView.findViewById(R.id.tvDate);

        tvTitle.setText(e.title);
        tvAmount.setText(MainActivity.format(e.amount));
        tvDate.setText(sdf.format(new Date(e.createdAt)));

        return convertView;
    }
}
