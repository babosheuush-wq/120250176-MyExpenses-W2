package com.example.myexpenses;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myexpenses.data.Category;
import com.example.myexpenses.data.CategoryTotal;
import com.example.myexpenses.data.ExpenseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etName, etBudget;
    Button btnAdd;
    ListView lvCategories;

    ExpenseDatabase db;
    ArrayList<CategoryTotal> categories = new ArrayList<>();
    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etBudget = findViewById(R.id.etBudget);
        btnAdd = findViewById(R.id.btnAdd);
        lvCategories = findViewById(R.id.lvCategories);

        db = ExpenseDatabase.getInstance(this);

        adapter = new CategoryAdapter(this, categories);
        lvCategories.setAdapter(adapter);

        // القائمة بتتحدث لحالها لما تتغير البيانات
        db.categoryDao().getCategoriesWithTotal().observe(this, list -> {
            categories.clear();
            categories.addAll(list);
            adapter.notifyDataSetChanged();
        });

        // اضافي: مجموع كل المصاريف في العنوان
        db.expenseDao().getAllTotal().observe(this, total -> {
            if (total == null) total = 0.0;
            setTitle("Total: " + format(total));
        });

        btnAdd.setOnClickListener(v -> addCategory());

        // الضغط على تصنيف يفتح شاشة المصاريف
        lvCategories.setOnItemClickListener((parent, view, position, id) -> {
            CategoryTotal c = categories.get(position);
            Intent intent = new Intent(MainActivity.this, ExpensesActivity.class);
            intent.putExtra("category_id", c.id);
            intent.putExtra("category_name", c.name);
            startActivity(intent);
        });

        // الضغط المطول = حذف بعد التأكيد
        lvCategories.setOnItemLongClickListener((parent, view, position, id) -> {
            CategoryTotal c = categories.get(position);
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("حذف تصنيف")
                    .setMessage("هل تريد حذف تصنيف \"" + c.name + "\"؟\nسيتم حذف مصاريفه أيضاً.")
                    .setPositiveButton("حذف", (dialog, which) -> deleteCategory(c))
                    .setNegativeButton("إلغاء", null)
                    .show();
            return true;
        });
    }

    private void addCategory() {
        String name = etName.getText().toString().trim();
        String budgetText = etBudget.getText().toString().trim();

        if (name.isEmpty() || budgetText.isEmpty()) {
            Toast.makeText(this, "عبي الاسم والميزانية", Toast.LENGTH_SHORT).show();
            return;
        }

        double budget;
        try {
            budget = Double.parseDouble(budgetText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "الميزانية لازم تكون رقم", Toast.LENGTH_SHORT).show();
            return;
        }

        Category category = new Category(name, budget);
        ExpenseDatabase.ioExecutor.execute(() -> db.categoryDao().insert(category));

        etName.setText("");
        etBudget.setText("");
    }

    private void deleteCategory(CategoryTotal c) {
        Category category = new Category(c.name, c.budget);
        category.id = c.id; // الحذف بيعتمد على المفتاح الاساسي
        ExpenseDatabase.ioExecutor.execute(() -> db.categoryDao().delete(category));
    }

    // عشان 100.0 تطلع 100 و 240.5 تضل 240.5
    public static String format(double d) {
        if (d == (long) d) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }
}
