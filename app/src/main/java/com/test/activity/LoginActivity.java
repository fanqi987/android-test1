package com.test.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.test.activity.db.TestDBOpenHelper;
import com.test.activity.mybroadcastreceiver.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by hasse on 2020/1/8.
 */

public class LoginActivity extends BaseActivity {

    private EditText accountEt;
    private EditText passwordEt;
    private Button loginBtn;

    private EditText writeTofileEt;
    private Button writeTofileBtn;

    private TestDBOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        accountEt = (EditText) findViewById(R.id.account_et);
        passwordEt = (EditText) findViewById(R.id.password_et);
        loginBtn = (Button) findViewById(R.id.login_btn);
        writeTofileEt = (EditText) findViewById(R.id.write_to_file_et);
        writeTofileBtn = (Button) findViewById(R.id.write_to_file_btn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = accountEt.getText().toString();
                String password = passwordEt.getText().toString();

                if (account != null && password != null) {
                    if (account.equals("admin") && password.equals("123")) {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "输入错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "请输入账号密码", Toast.LENGTH_SHORT).show();
                }
            }
        });


        writeTofileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(writeTofileEt.getText().toString());
            }
        });

        String s = load();
        if (!TextUtils.isEmpty(s)) {
            writeTofileEt.setText(s);
            //todo 移动光标
            writeTofileEt.setSelection(s.length());
            Toast.makeText(this, "读取文件持久化数据成功", Toast.LENGTH_SHORT).show();
        }

        helper = new TestDBOpenHelper(LoginActivity.this,
                "Book.db", null, 1);

        findViewById(R.id.create_db_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = helper.getWritableDatabase();
//                Toast.makeText(LoginActivity.this, "表创建成功", Toast.LENGTH_SHORT).show();

            }
        });

        findViewById(R.id.insert_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = helper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("name", "语文书");
                cv.put("author", "语文老师");
                cv.put("page", 80);
                cv.put("price", "12.2");
                db.insert("Book", null, cv);
                cv.clear();
                db.close();
                Toast.makeText(LoginActivity.this, "数据已经添加", Toast.LENGTH_SHORT).show();


            }
        });

        findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = helper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("price", "9.9");
                db.update("Book", cv, "name = ?", new String[]{"语文书"});
                db.close();
                Toast.makeText(LoginActivity.this, "数据已经更新", Toast.LENGTH_SHORT).show();

            }
        });

        findViewById(R.id.del_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = helper.getWritableDatabase();
                db.delete("Book", "name=?", new String[]{"语文书"});
                db.close();
                Toast.makeText(LoginActivity.this, "数据已经删除", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.select_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    db = helper.getWritableDatabase();
                    Cursor c = db.query("Book", null, null, null,
                            null, null, null);
                    if (c.moveToFirst()) {
                        do {
                            Log.i("name", c.getString(c.getColumnIndex("name")));
                            Log.i("author", c.getString(c.getColumnIndex("author")));
                            Log.i("price", Float.toString(c.getFloat(c.getColumnIndex("price"))));
                            Log.i("page", Integer.toString(c.getInt(c.getColumnIndex("page"))));
                        } while (c.moveToNext());
                    }
                    c.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.close();
                }
            }
        });

        findViewById(R.id.transaction_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = helper.getWritableDatabase();
                db.beginTransaction();
                try {
                    ContentValues cv = new ContentValues();
                    cv.put("name", "shuxueshu");
                    cv.put("author", "shuxuelaoshi");
                    cv.put("page", 123);
                    cv.put("price", 20.1);
                    db.insert("Book", null, cv);
                    db.setTransactionSuccessful();
                    Toast.makeText(LoginActivity.this, "提交新增事务成功", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                    db.close();
                }
            }
        });

        findViewById(R.id.upgrade_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    helper = new TestDBOpenHelper(LoginActivity.this, "Book.db", null, 2);
                    helper.getWritableDatabase();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.reset_table_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = helper.getWritableDatabase();
                db.execSQL("drop table if exists Book;");
                db.close();
                Toast.makeText(LoginActivity.this, "重置表成功", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private String load() {
        FileInputStream fi = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            fi = openFileInput("file_test");
            br = new BufferedReader(new InputStreamReader(fi));
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private void save(String writeTofileString) {
        if (writeTofileString.isEmpty()) {
            Toast.makeText(LoginActivity.this, "写入不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        FileOutputStream fs = null;
        BufferedWriter bw = null;
        try {
            fs = openFileOutput("file_test", MODE_PRIVATE);
            bw = new BufferedWriter(new OutputStreamWriter(fs));
            bw.write(writeTofileString);
            Toast.makeText(getApplicationContext(), "写入文件成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "写入文件失败", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save(writeTofileEt.getText().toString());
    }
}
