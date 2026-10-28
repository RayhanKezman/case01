package com.example.case01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "student_db";
    private static final String TB_NAME = "students";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_STUDENT_ID = "student_id";
    private static final String COL_MAJOR = "major";

    private static final String CREATE_TABLE = "CREATE TABLE " + TB_NAME + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAME + " TEXT NOT NULL, "
            + COL_STUDENT_ID + " TEXT NOT NULL, "
            + COL_MAJOR + " TEXT NOT NULL);";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }

    public void addStudent(StudentModel studentModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, studentModel.getName());
        values.put(COL_STUDENT_ID, studentModel.getStudentId());
        values.put(COL_MAJOR, studentModel.getMajor());
        db.insert(TB_NAME, null, values);
    }

    public List<StudentModel> getStudentList() {
        String query = "SELECT " + COL_ID + ", " + COL_NAME + ", " + COL_STUDENT_ID + ", " + COL_MAJOR + " FROM " + TB_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        List<StudentModel> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String studentId = cursor.getString(2);
                String major = cursor.getString(3);
                list.add(new StudentModel(id, name, studentId, major));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void updateStudent(StudentModel studentModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, studentModel.getName());
        values.put(COL_STUDENT_ID, studentModel.getStudentId());
        values.put(COL_MAJOR, studentModel.getMajor());
        db.update(TB_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(studentModel.getId())});
    }

    public void deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TB_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }
}