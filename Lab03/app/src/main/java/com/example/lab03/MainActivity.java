package com.example.lab03;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
public class MainActivity extends AppCompatActivity
{
    String PREF_AMOUNT = "Amount";
    String PREF_RATE = "Rate";
    Button Btn1,Btn2,Btn3,Btn4,Btn5;
    TextView show1,show2;
    EditText txtAmount, txtRate;
    SharedPreferences prefs;
    private static final int READ_BLOCK_SIZE = 100;
    private final String fileName = "note.txt";//內部寫入檔案
    private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtAmount = findViewById(R.id.edit);
        txtRate = findViewById(R.id.edit2);
        Btn1 = findViewById(R.id.btn1);
        Btn2 = findViewById(R.id.btn2);
        Btn3 = findViewById(R.id.btn3);
        Btn4 = findViewById(R.id.btn4);
        Btn5 = findViewById(R.id.btn5);
        show1 =findViewById(R.id.showAns);//顯示兌換金額
        prefs =	getPreferences(MODE_PRIVATE);// 取得SharedPreferences物件
        //檢查是否有外部SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            Toast.makeText(this, "沒有外部SK卡", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        try
        {
            // 在SD卡建立檔案
            File sd = Environment.getExternalStorageDirectory();
            String filePath = "/Download/test";
            File dir = new File(sd.getAbsolutePath() + filePath);
            if (!dir.exists()) dir.mkdir();
            file = new File(dir, fileName);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    @Override
    protected void onResume()//讀取偏好設定資料
    {
        super.onResume();
        String amount = prefs.getString(PREF_AMOUNT, "10000");
        txtAmount.setText(amount);
        float rate = prefs.getFloat(PREF_RATE, 4.11F);
        txtRate.setText(String.valueOf(rate));
    }
    @Override
    protected void onPause()//儲存偏好設定資料
    {
        super.onPause();
        float rate;
        // 取得Editor物件
        SharedPreferences.Editor prefEdit = prefs.edit();
        // 存入偏好設定資料至Editor物件
        prefEdit.putString(PREF_AMOUNT, txtAmount.getText().toString());
        rate = (float) Double.parseDouble(txtRate.getText().toString());
        prefEdit.putFloat(PREF_RATE, rate);
        prefEdit.apply(); // 寫入檔案
    }
    // 偏好Button的事件處理
    @SuppressLint("SetTextI18n")
    public void clickBtn1(View view)
    {
        double amount, rate, result;
        amount = Double.parseDouble(txtAmount.getText().toString());
        rate = Double.parseDouble(txtRate.getText().toString());
        result = amount / rate;//計算兌換金額
        show1.setText("港幣: " + result);
        Toast.makeText(MainActivity.this, "已兌換", Toast.LENGTH_SHORT).show();

    }
    //儲存內部檔案Button的事件處理
    public void clickBtn2(View view)
    {
        EditText input = findViewById(R.id.edit1);
        String str = input.getText().toString();
        try
        {
            // 開啟寫入檔案
            FileOutputStream out = openFileOutput(fileName,MODE_PRIVATE);
            OutputStreamWriter sw = new OutputStreamWriter(out);
            sw.write(str);  // 將字串寫入串流
            sw.flush();     // 輸出串流資料
            sw.close();     // 關閉串流
            Toast.makeText(this, "成功把兌換金額寫入內部檔案裡", Toast.LENGTH_SHORT).show();
            input.setText("");  // 清除EditText元件的內容
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    //讀取內部檔案Button的事件處理
    @SuppressLint("SetTextI18n")
    public void clickBtn3(View view)
    {
        try
        {
            // 開啟讀取檔案
            FileInputStream in = openFileInput(fileName);
            InputStreamReader sr = new InputStreamReader(in);
            char[] buffer = new char[READ_BLOCK_SIZE];
            StringBuilder str = new StringBuilder();
            int count;
            // 讀取檔案內容
            while ((count = sr.read(buffer)) > 0)
            {
                String s = String.copyValueOf(buffer,0, count);
                str.append(s);
                buffer = new char[READ_BLOCK_SIZE];
            }
            sr.close();     // 關閉串流
            Toast.makeText(this, "成功讀取內部檔案", Toast.LENGTH_SHORT).show();
            show2 =findViewById(R.id.showAns);
            show2.setText("讀取內部檔案內容:" + str);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    //檢查外部SD卡是否可寫入
    private static boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    // 儲存外部檔案Button的事件處理
    public void clickBtn4(View view)
    {
        EditText input = findViewById(R.id.edit1);
        String str = input.getText().toString();
        if (isExternalStorageWritable())
        {
            try
            {
                // 開啟寫入檔案
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter sw = new OutputStreamWriter(fos);
                sw.write(str);  // 將字串寫入串流
                sw.flush();     // 輸出串流資料
                sw.close();     // 關閉串流
                Toast.makeText(this, "成功寫入外部檔案.", Toast.LENGTH_SHORT).show();
                input.setText("");  // 清除EditText元件的內容
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                Toast.makeText(this, "寫入外部檔案失敗.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "無法寫入外部檔案，請檢查SD卡是否可用.", Toast.LENGTH_SHORT).show();
        }
    }

    // 讀取外部檔案Button的事件處理
    @SuppressLint("SetTextI18n")
    public void clickBtn5(View view)
    {
        if (isExternalStorageWritable())
        {
            try
            {
                // 開啟讀取檔案
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader sr = new InputStreamReader(fis);
                char[] buffer = new char[READ_BLOCK_SIZE];
                StringBuilder str = new StringBuilder();
                int ch;
                // 讀取檔案內容
                while ((ch = sr.read(buffer)) > 0)
                {
                    String s = String.copyValueOf(buffer, 0, ch);
                    str.append(s);
                    buffer = new char[READ_BLOCK_SIZE];
                }
                sr.close();     // 關閉串流
                Toast.makeText(this, "成功讀取外部檔案", Toast.LENGTH_SHORT).show();
                show2 = findViewById(R.id.showAns);
                show2.setText("讀取外部檔案:" + str);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                Toast.makeText(this, "讀取外部檔案失敗.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "無法讀取外部檔案，請檢查SD卡是否可用.", Toast.LENGTH_SHORT).show();
        }
    }

}
