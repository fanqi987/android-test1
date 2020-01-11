package com.test.activity;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.test.activity.db.TestDBOpenHelper;

public class TestContentProvider extends ContentProvider {

    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final int CATEGORY_DIR = 2;
    public static final int CATEGORY_ITEM = 3;


    //todo 引用权限串
    public static final String AUTHORITY = "com.test.activity.provider";
    private static UriMatcher uriMatcher;

    private TestDBOpenHelper dbOpenHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //todo 路径（表名）使用小写
        uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_ITEM);
//        uriMatcher.addURI(AUTHORITY, "Book", BOOK_DIR);
//        uriMatcher.addURI(AUTHORITY, "Book/#", BOOK_ITEM);
//        uriMatcher.addURI(AUTHORITY, "Category", CATEGORY_DIR);
//        uriMatcher.addURI(AUTHORITY, "Category", CATEGORY_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new TestDBOpenHelper(getContext(), "Book.db", null, 2);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor c = null;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        String id;
        //todo match返回的是URI串匹配“格式(BOOK_DIR)“
//        if (uriMatcher.match(uri) != -1) {
        switch (uriMatcher.match(uri)) {
//            int i = 0;
//            String id = null;
            case BOOK_DIR:
                c = db.query("Book", strings, s, strings1, null, null, s1);
                break;
            case BOOK_ITEM:
                //todo 通过getPathSegment返回表名，和列id值
                id = uri.getPathSegments().get(1);
                //id = uri.getPath();
                c = db.query("Book", strings, "id=?", new String[]{id}, null, null, s1);
                break;
            case CATEGORY_DIR:
                c = db.query("Category", strings, s, strings1, null, null, s1);

                break;
            case CATEGORY_ITEM:
                //todo 通过getPathSegment返回表名，和列id值
                id = uri.getPathSegments().get(1);
                //id = uri.getPath();
                c = db.query("Category", strings, "id=?", new String[]{id}, null, null, s1);
                break;
        }
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //todo match返回的是URI串I匹配格式（BOOK_DIR）
//        int i = uri.getPort();
//        switch (i) {
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                //todo vnd.android.cursor.(表类<dir>或数据项类<item>)/vnd.（authority）.（path）,下同
//                return "vnd.android.type.book.dir/vnd.com.test.activity.provider.book";
                return "vnd.android.cursor.dir/vnd.com.test.activity.provider.book";
            case BOOK_ITEM:
//                return "vnd.android.type.book.item/vnd.com.test.activity.provider.book";
                return "vnd.android.cursor.item/vnd.com.test.activity.provider.book";
            case CATEGORY_DIR:
//                return "vnd.android.type.category.dir/vnd.com.test.activity.provider.category";
                return "vnd.android.cursor.dir/vnd.com.test.activity.provider.category";
            case CATEGORY_ITEM:
//                return "vnd.android.type.category.item/vnd.com.test.activity.provider.category";
                return "vnd.android.cursor.item/vnd.com.test.activity.provider.category";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri uri2 = null;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        long newID = 0;
        //todo match返回的是URI串匹配“格式(BOOK_DIR)“
//        if (uriMatcher.match(uri) != -1) {
//            int i = 0;
//            switch (i) {
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                //todo db返回的是新id，而provider返回的是uri，所以将新id拼到uri
                newID = db.insert("Book", null, contentValues);
                uri2 = Uri.parse("content://" + AUTHORITY + "/book/" + newID);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                //todo db返回的是新id，而provider返回的是uri，所以将新id拼到uri
                newID = db.insert("Category", null, contentValues);
                uri2 = Uri.parse("content://" + AUTHORITY + "/category/" + newID);
                break;
        }
//        uri2 = Uri.parse(uri2.toString() + "/" + newID);
        return uri2;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int columns = 0;
        String delID;
        //todo match返回的是URI串匹配“格式(BOOK_DIR)“
        switch (uriMatcher.match(uri)) {
            //        int i = 0;
            //        if (uriMatcher.match(uri) != -1) {//            switch (i) {

            //todo 删除时，若uri中指定了删除条件（id）,则形参的删除条件就没用了
            case BOOK_DIR:
                columns = db.delete("Book", s, strings);
                break;
            case BOOK_ITEM:
//                columns = db.delete("Book", s, strings);
                delID = uri.getPathSegments().get(1);
                columns = db.delete("Book", "id=?", new String[]{delID});
                break;
            case CATEGORY_DIR:
                columns = db.delete("Category", s, strings);
                break;
            case CATEGORY_ITEM:
                delID = uri.getPathSegments().get(1);
                columns = db.delete("Category", "id=?", new String[]{delID});
                break;
        }
        return columns;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//        int i = 0;
        int columns = 0;
        String updateID = null;
        //todo match返回的是URI串匹配“格式(BOOK_DIR)“
//        if (uriMatcher.match(uri) != -1) {
//            switch (i) {
        switch (uriMatcher.match(uri)) {
            //todo 更新时，若uri中指定了更新条件（id），那么形参更新条件就无效了。
            case BOOK_DIR:
                columns = db.update("Book", contentValues, s, strings);
                break;

            case BOOK_ITEM:
//                columns = db.update("Book", contentValues, s, strings);
                updateID = uri.getPathSegments().get(1);
                columns = db.update("Book", contentValues, "id=?", new String[]{updateID});
                break;

            case CATEGORY_DIR:
                columns = db.update("Category", contentValues, s, strings);
                break;

            case CATEGORY_ITEM:
                columns = db.update("Category", contentValues, "id=?", new String[]{updateID});
//                columns = db.update("Category", contentValues, s, strings);
                break;

        }
        return columns;
    }
}
