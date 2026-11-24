package com.example.myexpenses.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses",
        foreignKeys = @ForeignKey(entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index("category_id"))
public class Expense {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "category_id")
    public int categoryId;

    public String title;

    public double amount;

    public long createdAt;

    public Expense(int categoryId, String title, double amount, long createdAt) {
        this.categoryId = categoryId;
        this.title = title;
        this.amount = amount;
        this.createdAt = createdAt;
    }
}
