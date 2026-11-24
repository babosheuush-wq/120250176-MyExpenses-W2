package com.example.myexpenses.data;

// كلاس عادي (مش جدول) عشان نستقبل فيه نتيجة الاستعلام
// اسماء الحقول لازم تطابق اسماء الاعمدة في SELECT
public class CategoryTotal {
    public int id;
    public String name;
    public double budget;
    public double total;
}
