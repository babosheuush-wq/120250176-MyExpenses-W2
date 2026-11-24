package com.example.myexpenses.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Category.class, Expense.class}, version = 1, exportSchema = false)
public abstract class ExpenseDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();

    public abstract ExpenseDao expenseDao();

    private static volatile ExpenseDatabase INSTANCE;

    // خيط في الخلفية للكتابة (IO) عشان ما نعلق الواجهة
    public static final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();

    // Singleton - نسخة وحدة بس من القاعدة
    public static ExpenseDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ExpenseDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ExpenseDatabase.class, "expenses.db").build();
                }
            }
        }
        return INSTANCE;
    }
}
