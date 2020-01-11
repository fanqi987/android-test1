package com.test.activity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class TestDBOpenHelper extends SQLiteOpenHelper {

    private Context c;

    private static final String CREATE_BOOK_TABLE =
            "create table Book(" +
                    "id integer primary key autoincrement," +
                    "name text not null," +
                    "author text not null," +
                    "page integer not null," +
                    "price float not null," +
                    "category_id integer not null" +
                    ");";

    private static final String CREATE_BOOK_TYPE_TABLE =
            "create table Category(" +
                    "name text not null," +
                    "code integer not null," +
                    "id integer primary key autoincrement" +
                    ");";

    public TestDBOpenHelper(@Nullable Context context, @Nullable String name,
                            @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOK_TABLE);
        sqLiteDatabase.execSQL(CREATE_BOOK_TYPE_TABLE);
        Toast.makeText(c, "创建新的数据库和表成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV) {
        switch (oldV) {
            case 1:
                sqLiteDatabase.execSQL(CREATE_BOOK_TYPE_TABLE);
                sqLiteDatabase.execSQL("alter table Book add column category_id integer");
        }

    }
}
