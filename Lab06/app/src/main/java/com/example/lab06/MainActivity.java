package com.example.lab06;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private static String DATABASE_TABLE = "titles";
    private EditText txtTitle, txtPrice, txtNewPrice;
    private SQLiteDatabase db;
    private MyDBHelper dbHelper;
    TextView view2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDBHelper(this);
        db = dbHelper.getWritableDatabase();
        view2 =findViewById(R.id.lblOutput2);
        txtTitle=(EditText) findViewById(R.id.txtTitle);
        txtPrice=(EditText) findViewById(R.id.txtPrice);
        txtNewPrice=(EditText) findViewById(R.id.txtNewPrice);
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        db.close(); // 關閉資料庫
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @SuppressLint("Range")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_insert)// 新增
        {
            db.execSQL("INSERT INTO " + DATABASE_TABLE + " (" +"title, price) VALUES ('" + txtTitle.getText().toString() +"'," + txtPrice.getText().toString() + ")");
            view2.setText("成功新增1筆記錄 " );
            return true;
        }
        if (id == R.id.action_update)
        {  // 更新
            String title = txtTitle.getText().toString();
            ContentValues cv = new ContentValues();
            double price = Double.parseDouble(txtNewPrice.getText().toString());
            cv.put("price", price);
            int count = db.update(DATABASE_TABLE, cv, "title='" + title + "'", null);
            db.execSQL("UPDATE " + DATABASE_TABLE + " SET price =" +txtNewPrice.getText().toString() + " WHERE title='" +txtTitle.getText().toString() + "'");
            view2.setText("成功更新"+count+"筆記錄 ");
            return true;
        }
        if (id == R.id.action_delete)
        {  // 刪除
            String title = txtTitle.getText().toString();
            int count = db.delete(DATABASE_TABLE, "title='" + title + "'", null);
            db.execSQL("DELETE FROM " + DATABASE_TABLE + " WHERE title='" +txtTitle.getText().toString() + "'");
            view2.setText("成功刪除"+count+"筆記錄 ");
            return true;
        }
        if (id == R.id.action_queryAll)// 顯示全部記錄
        {
            String[] colNames=new String[]{"_id","title","price"};
            String str = "";
            Cursor c = db.query(DATABASE_TABLE, colNames,null, null, null, null,null);
            // 顯示欄位名稱
            for (int i = 0; i < colNames.length; i++)
            {
                str += colNames[i] + "\t\t";
            }
            str += "\n";
            c.moveToFirst();  // 第1筆
            // 顯示欄位值
            for (int i = 0; i < c.getCount(); i++)
            {
                str += c.getString(c.getColumnIndex(colNames[0])) + "\t\t";
                str += c.getString(1) + "\t\t";
                str += c.getString(2) + "\n";
                c.moveToNext();  // 下一筆
            }
            view2.setText(str.toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}