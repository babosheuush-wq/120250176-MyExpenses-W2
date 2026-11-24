package com.example.myexpenses.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public double budget; // الميزانية الشهرية

    public Category(String name, double budget) {
        this.name = name;
        this.budget = budget;
    }
}
