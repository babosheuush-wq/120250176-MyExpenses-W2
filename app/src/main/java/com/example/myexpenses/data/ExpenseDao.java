package com.example.myexpenses.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    void insert(Expense expense);

    @Delete
    void delete(Expense expense);

    // مصاريف تصنيف واحد من الاحدث للاقدم
    @Query("SELECT * FROM expenses WHERE category_id = :categoryId ORDER BY createdAt DESC")
    LiveData<List<Expense>> getByCategory(int categoryId);

    // للاضافي: مجموع كل المصاريف
    @Query("SELECT COALESCE(SUM(amount), 0) FROM expenses")
    LiveData<Double> getAllTotal();
}
