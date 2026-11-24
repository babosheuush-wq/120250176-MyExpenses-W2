package com.example.myexpenses;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myexpenses.data.Expense;
import com.example.myexpenses.data.ExpenseDatabase;

import java.util.ArrayList;

public class ExpensesActivity extends AppCompatActivity {

    EditText etTitle, etAmount;
    Button btnAddExpense;
    ListView lvExpenses;

    ExpenseDatabase db;
    ArrayList<Expense> expenses = new ArrayList<>();
    ExpenseAdapter adapter;

    int categoryId;
    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        // رقم واسم التصنيف جايين من MainActivity عبر Intent
        categoryId = getIntent().getIntExtra("category_id", -1);
        categoryName = getIntent().getStringExtra("category_name");

        if (categoryId == -1) {
            Toast.makeText(this, "تصنيف غير موجود", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setTitle(categoryName);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etTitle = findViewById(R.id.etTitle);
        etAmount = findViewById(R.id.etAmount);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        lvExpenses = findViewById(R.id.lvExpenses);

        db = ExpenseDatabase.getInstance(this);

        adapter = new ExpenseAdapter(this, expenses);
        lvExpenses.setAdapter(adapter);

        // مصاريف هذا التصنيف فقط
        db.expenseDao().getByCategory(categoryId).observe(this, list -> {
            expenses.clear();
            expenses.addAll(list);
            adapter.notifyDataSetChanged();
        });

        btnAddExpense.setOnClickListener(v -> addExpense());

        lvExpenses.setOnItemLongClickListener((parent, view, position, id) -> {
            Expense e = expenses.get(position);
            new AlertDialog.Builder(ExpensesActivity.this)
                    .setTitle("حذف مصروف")
                    .setMessage("هل تريد حذف \"" + e.title + "\"؟")
                    .setPositiveButton("حذف", (dialog, which) ->
                            ExpenseDatabase.ioExecutor.execute(() -> db.expenseDao().delete(e)))
                    .setNegativeButton("إلغاء", null)
                    .show();
            return true;
        });
    }

    private void addExpense() {
        String title = etTitle.getText().toString().trim();
        String amountText = etAmount.getText().toString().trim();

        if (title.isEmpty() || amountText.isEmpty()) {
            Toast.makeText(this, "عبي الوصف والمبلغ", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "المبلغ لازم يكون رقم", Toast.LENGTH_SHORT).show();
            return;
        }

        Expense expense = new Expense(categoryId, title, amount, System.currentTimeMillis());
        ExpenseDatabase.ioExecutor.execute(() -> db.expenseDao().insert(expense));

        etTitle.setText("");
        etAmount.setText("");
    }

    // زر الرجوع في الشريط
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
